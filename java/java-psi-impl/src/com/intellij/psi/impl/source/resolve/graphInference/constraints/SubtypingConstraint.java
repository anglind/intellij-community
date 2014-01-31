/*
 * Copyright 2000-2014 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.psi.impl.source.resolve.graphInference.constraints;

import com.intellij.psi.*;
import com.intellij.psi.impl.source.resolve.graphInference.InferenceBound;
import com.intellij.psi.impl.source.resolve.graphInference.InferenceSession;
import com.intellij.psi.impl.source.resolve.graphInference.InferenceVariable;

import java.util.List;

public class SubtypingConstraint implements ConstraintFormula {
  private PsiType myS;
  private PsiType myT;

  public SubtypingConstraint(PsiType t, PsiType s) {
    myT = t;
    myS = s;
  }

  @Override
  public void apply(PsiSubstitutor substitutor) {
    myT = substitutor.substitute(myT);
    myS = substitutor.substitute(myS);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SubtypingConstraint that = (SubtypingConstraint)o;

    if ( myS instanceof PsiCapturedWildcardType && myS != that.myS) return false;

    if (myS != null ? !myS.equals(that.myS) : that.myS != null) return false;
    if (myT != null ? !myT.equals(that.myT) : that.myT != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = myS != null ? myS.hashCode() : 0;
    result = 31 * result + (myT != null ? myT.hashCode() : 0);
    return result;
  }

  @Override
  public boolean reduce(InferenceSession session, List<ConstraintFormula> constraints) {
    if (myT instanceof PsiWildcardType) {
      final PsiType tBound = ((PsiWildcardType)myT).getBound();
      if (tBound == null) {
        return true;
      }

      if (myS instanceof PsiCapturedWildcardType) {
        myS = ((PsiCapturedWildcardType)myS).getWildcard();
      }

      if (((PsiWildcardType)myT).isExtends()) {
        if (tBound.equalsToText(CommonClassNames.JAVA_LANG_OBJECT)) {
          return true;
        }

        if (myS instanceof PsiWildcardType) {
          final PsiType sBound = ((PsiWildcardType)myS).getBound();
          if (sBound == null) {
            return true;
          }

          if (((PsiWildcardType)myS).isExtends()) {
            constraints.add(new StrictSubtypingConstraint(tBound, sBound));
            return true;
          }
        } else {
          constraints.add(new StrictSubtypingConstraint(tBound, myS));
          return true;
        }
        return false;
      } else {

        if (myS instanceof PsiWildcardType) {
          final PsiType sBound = ((PsiWildcardType)myS).getBound();
          if (sBound != null && ((PsiWildcardType)myS).isSuper()) {
            constraints.add(new StrictSubtypingConstraint(sBound, tBound));
            return true;
          }
        } else {
          constraints.add(new StrictSubtypingConstraint(myS, tBound));
          return true;
        }
      }
      return false;
    } else {
      InferenceVariable inferenceVariable = session.getInferenceVariable(myT);
      if (myS instanceof PsiWildcardType) {
        return inferenceVariable != null;
      } else {
        final InferenceVariable inferenceVariableS = session.getInferenceVariable(myS);
        if (inferenceVariableS != null) {
          inferenceVariableS.addBound(myT, InferenceBound.EQ);
          return true;
        }

        if (inferenceVariable != null) {
          inferenceVariable.addBound(myS, InferenceBound.EQ);
          return true;
        }
        constraints.add(new StrictSubtypingConstraint(myT, myS));
        return true;
      }
    }
  }
}
