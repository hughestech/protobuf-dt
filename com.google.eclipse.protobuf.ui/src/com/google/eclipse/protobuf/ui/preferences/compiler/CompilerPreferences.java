/*
 * Copyright (c) 2011 Google Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * License v1.0 which accompanies this distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.google.eclipse.protobuf.ui.preferences.compiler;

import static com.google.eclipse.protobuf.ui.preferences.compiler.PreferenceNames.COMPILE_PROTO_FILES;
import static com.google.eclipse.protobuf.ui.preferences.compiler.PreferenceNames.CPP_CODE_GENERATION_ENABLED;
import static com.google.eclipse.protobuf.ui.preferences.compiler.PreferenceNames.CPP_OUTPUT_DIRECTORY;
import static com.google.eclipse.protobuf.ui.preferences.compiler.PreferenceNames.DESCRIPTOR_FILE_PATH;
import static com.google.eclipse.protobuf.ui.preferences.compiler.PreferenceNames.ENABLE_PROJECT_SETTINGS_PREFERENCE_NAME;
import static com.google.eclipse.protobuf.ui.preferences.compiler.PreferenceNames.JAVA_CODE_GENERATION_ENABLED;
import static com.google.eclipse.protobuf.ui.preferences.compiler.PreferenceNames.JAVA_OUTPUT_DIRECTORY;
import static com.google.eclipse.protobuf.ui.preferences.compiler.PreferenceNames.PROTOC_FILE_PATH;
import static com.google.eclipse.protobuf.ui.preferences.compiler.PreferenceNames.PYTHON_CODE_GENERATION_ENABLED;
import static com.google.eclipse.protobuf.ui.preferences.compiler.PreferenceNames.PYTHON_OUTPUT_DIRECTORY;
import static com.google.eclipse.protobuf.ui.preferences.compiler.PreferenceNames.REFRESH_OUTPUT_DIRECTORY;
import static com.google.eclipse.protobuf.ui.preferences.compiler.PreferenceNames.REFRESH_PROJECT;
import static com.google.eclipse.protobuf.ui.preferences.compiler.PreferenceNames.REFRESH_RESOURCES;
import static com.google.eclipse.protobuf.ui.preferences.compiler.PreferenceNames.USE_PROTOC_IN_CUSTOM_PATH;
import static com.google.eclipse.protobuf.ui.preferences.compiler.PreferenceNames.USE_PROTOC_IN_SYSTEM_PATH;

import com.google.eclipse.protobuf.preferences.DefaultPreservingInitializer;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.xtext.ui.editor.preferences.IPreferenceStoreAccess;

/**
 * @author alruiz@google.com (Alex Ruiz)
 */
public class CompilerPreferences {
  public static CompilerPreferences compilerPreferences(IPreferenceStoreAccess storeAccess, IProject project) {
    IPreferenceStore store = storeAccess.getWritablePreferenceStore(project);
    boolean enableProjectSettings = store.getBoolean(ENABLE_PROJECT_SETTINGS_PREFERENCE_NAME);
    if (!enableProjectSettings) {
      store = storeAccess.getWritablePreferenceStore();
    }
    return new CompilerPreferences(store, project);
  }

  private final IPreferenceStore store;
  private final IProject project;
  private final CodeGenerationPreference javaCodeGenerationPreference;
  private final CodeGenerationPreference cppCodeGenerationPreference;
  private final CodeGenerationPreference pythonCodeGenerationPreference;

  private CompilerPreferences(IPreferenceStore store, IProject project) {
    this.store = store;
    this.project = project;
    javaCodeGenerationPreference = new JavaCodeGenerationPreference(store, project);
    cppCodeGenerationPreference = new CppCodeGenerationPreference(store, project);
    pythonCodeGenerationPreference = new PythonCodeGenerationPreference(store, project);
  }

  public boolean shouldCompileProtoFiles() {
    return store.getBoolean(COMPILE_PROTO_FILES);
  }

  public String protocPath() {
    return (useProtocInSystemPath()) ? "protoc" : store.getString(PROTOC_FILE_PATH);
  }

  private boolean useProtocInSystemPath() {
    return store.getBoolean(USE_PROTOC_IN_SYSTEM_PATH);
  }

  public String descriptorPath() {
    return store.getString(DESCRIPTOR_FILE_PATH);
  }

  public CodeGenerationPreference javaCodeGeneration() {
    return javaCodeGenerationPreference;
  }

  public CodeGenerationPreference cppCodeGeneration() {
    return cppCodeGenerationPreference;
  }

  public CodeGenerationPreference pythonCodeGeneration() {
    return pythonCodeGenerationPreference;
  }

  public boolean refreshResources() {
    return store.getBoolean(REFRESH_RESOURCES);
  }

  public boolean refreshProject() {
    return store.getBoolean(REFRESH_PROJECT);
  }

  public IProject project() {
    return project;
  }

  public static class Initializer extends DefaultPreservingInitializer {
    private static final String DEFAULT_OUTPUT_DIRECTORY = "src-gen";

    @Override
    public void setDefaults() {
      setDefault(ENABLE_PROJECT_SETTINGS_PREFERENCE_NAME, false);
      setDefault(USE_PROTOC_IN_SYSTEM_PATH, true);
      setDefault(USE_PROTOC_IN_CUSTOM_PATH, false);
      setDefault(JAVA_CODE_GENERATION_ENABLED, false);
      setDefault(CPP_CODE_GENERATION_ENABLED, false);
      setDefault(PYTHON_CODE_GENERATION_ENABLED, false);
      setDefault(JAVA_OUTPUT_DIRECTORY, DEFAULT_OUTPUT_DIRECTORY);
      setDefault(CPP_OUTPUT_DIRECTORY, DEFAULT_OUTPUT_DIRECTORY);
      setDefault(PYTHON_OUTPUT_DIRECTORY, DEFAULT_OUTPUT_DIRECTORY);
      setDefault(REFRESH_RESOURCES, true);
      setDefault(REFRESH_PROJECT, true);
      setDefault(REFRESH_OUTPUT_DIRECTORY, false);
    }
 }
}
