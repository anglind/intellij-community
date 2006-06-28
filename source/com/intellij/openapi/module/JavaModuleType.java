/*
 * Copyright (c) 2004 JetBrains s.r.o. All  Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * -Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 * -Redistribution in binary form must reproduct the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the distribution.
 *
 * Neither the name of JetBrains or IntelliJ IDEA
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. JETBRAINS AND ITS LICENSORS SHALL NOT
 * BE LIABLE FOR ANY DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT
 * OF OR RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL JETBRAINS OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL,
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN
 * IF JETBRAINS HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 */
package com.intellij.openapi.module;

import com.intellij.ide.util.projectWizard.*;
import com.intellij.openapi.project.ProjectBundle;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.IconLoader;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.NonNls;

import javax.swing.*;

public class JavaModuleType extends ModuleType<JavaModuleBuilder> {
  private static final Icon JAVA_MODULE_ICON = IconLoader.getIcon("/modules/javaModule.png");
  private static final Icon JAVA_MODULE_NODE_ICON_OPEN = IconLoader.getIcon("/nodes/ModuleOpen.png");
  private static final Icon JAVA_MODULE_NODE_ICON_CLOSED = IconLoader.getIcon("/nodes/ModuleClosed.png");
  //private static final Icon JAVA_MODULE_ICON_NORMAL = IconLoader.getIcon("/nodes/javaModule.png");
  private static final Icon WIZARD_ICON = IconLoader.getIcon("/add_java_modulewizard.png");

  public JavaModuleType() {
    this("JAVA_MODULE");
  }

  protected JavaModuleType(@NonNls String id) {
    super(id);
  }

  public JavaModuleBuilder createModuleBuilder() {
    return new JavaModuleBuilder();
  }

  public String getName() {
    return ProjectBundle.message("module.type.java.name");
  }

  public String getDescription() {
    return ProjectBundle.message("module.type.java.description");
  }

  public Icon getBigIcon() {
    return JAVA_MODULE_ICON;
  }

  public Icon getNodeIcon(boolean isOpened) {
    return isOpened ? JAVA_MODULE_NODE_ICON_OPEN : JAVA_MODULE_NODE_ICON_CLOSED;
  }

  public ModuleWizardStep[] createWizardSteps(WizardContext wizardContext, JavaModuleBuilder moduleBuilder,
                                              ModulesProvider modulesProvider) {
    final NameLocationStep nameLocationStep = new NameLocationStep(wizardContext, moduleBuilder, modulesProvider, WIZARD_ICON,
                                                                   "project.creatingModules.page2");
    final ModuleWizardStep[] wizardSteps = new ModuleWizardStep[]{nameLocationStep,
      new SourcePathsStep(nameLocationStep, moduleBuilder, WIZARD_ICON, "project.creatingModules.page3"),};
    return ArrayUtil.mergeArrays(wizardSteps, super.createWizardSteps(wizardContext, moduleBuilder, modulesProvider), ModuleWizardStep.class);
  }
}