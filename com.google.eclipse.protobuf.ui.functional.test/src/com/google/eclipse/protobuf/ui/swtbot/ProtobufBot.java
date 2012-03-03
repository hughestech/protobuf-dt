/*
 * Copyright (c) 2012 Google Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.google.eclipse.protobuf.ui.swtbot;

import static com.google.eclipse.protobuf.ui.util.Workspaces.workspaceRoot;

import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.*;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.*;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

/**
 * @author alruiz@google.com (Alex Ruiz)
 */
public class ProtobufBot extends SWTWorkbenchBot {
  private static final NullProgressMonitor NULL_MONITOR = new NullProgressMonitor();

  public void resetAll() throws CoreException {
    resetWorkbench();
    deleteProjects();
    closeWelcomeView();
  }

  private void deleteProjects() throws CoreException {
    for (IProject project : workspaceRoot().getProjects()) {
      project.delete(true, true, NULL_MONITOR);
    }
  }

  private void closeWelcomeView() {
    try {
      viewByTitle("Welcome").close();
    } catch (WidgetNotFoundException ignored) {}
  }

  public void createGeneralProject(String name) {
    menu("File").menu("New").menu("Project...").click();
    SWTBotShell shell = shell("New Project");
    shell.activate();
    tree().expandNode("General").select("Project");
    button("Next >").click();
    textWithLabel("Project name:").setText(name);
    button("Finish").click();
  }

  public SWTBotEclipseEditor createFile(String name) {
    menu("File").menu("New").menu("File").click();
    SWTBotShell shell = shell("New File");
    shell.activate();
    textWithLabel("File name:").setText(name);
    button("Finish").click();
    return editorByTitle(name).toTextEditor();
  }

  public void saveAndCloseAllEditors() {
    List<? extends SWTBotEditor> editors = editors();
    for (SWTBotEditor editor : editors) {
      editor.saveAndClose();
    }
  }
}
