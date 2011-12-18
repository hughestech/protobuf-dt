/*
 * Copyright (c) 2011 Google Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.google.eclipse.protobuf.scoping;

import static com.google.eclipse.protobuf.junit.IEObjectDescriptions.descriptionsIn;
import static com.google.eclipse.protobuf.junit.core.Setups.integrationTestSetup;
import static com.google.eclipse.protobuf.junit.core.XtextRule.createWith;
import static com.google.eclipse.protobuf.junit.matchers.ContainAllFieldsInMessage.containAllFieldsIn;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.scoping.IScope;
import org.junit.*;

import com.google.eclipse.protobuf.junit.core.XtextRule;
import com.google.eclipse.protobuf.protobuf.*;

/**
 * Tests for <code>{@link ProtobufScopeProvider#scope_FieldName_target(FieldName, EReference)}</code>
 *
 * @author alruiz@google.com (Alex Ruiz)
 */
public class ProtobufScopeProvider_scope_FieldName_target_with_NormalFieldName_Test {

  private static EReference reference;

  @BeforeClass public static void setUpOnce() {
    reference = mock(EReference.class);
  }

  @Rule public XtextRule xtext = createWith(integrationTestSetup());

  private ProtobufScopeProvider provider;

  @Before public void setUp() {
    provider = xtext.getInstanceOf(ProtobufScopeProvider.class);
  }

  // syntax = "proto2";
  //
  // import "google/protobuf/descriptor.proto";
  //
  // message Type {
  //   optional int32 code = 1;
  // }
  //
  // extend google.protobuf.FileOptions {
  //   optional Type type = 15478479;
  // }
  //
  // option (type) = {
  //   code: 68
  // };
  @Test public void should_provide_sources_for_aggregate_field() {
    ValueField field = xtext.find("code", ":", ValueField.class);
    NormalFieldName name = (NormalFieldName) field.getName();
    IScope scope = provider.scope_FieldName_target(name, reference);
    Message message = xtext.find("Type", " {", Message.class);
    assertThat(descriptionsIn(scope), containAllFieldsIn(message));
  }

  // syntax = "proto2";
  //
  // import "google/protobuf/descriptor.proto";
  //
  // message Type {
  //   optional int32 code = 1;
  //   optional Names name = 2;
  // }
  //
  // message Names {
  //   optional string value = 1;
  // }
  //
  // extend google.protobuf.FileOptions {
  //   optional Type type = 15478479;
  // }
  //
  // option (type) = {
  //   name { value: 'Address' }
  // };
  @Test public void should_provide_sources_for_nested_field_notation_in_option() {
    ValueField field = xtext.find("value", ":", ValueField.class);
    NormalFieldName name = (NormalFieldName) field.getName();
    IScope scope = provider.scope_FieldName_target(name, reference);
    Message message = xtext.find("Names", " {", Message.class);
    assertThat(descriptionsIn(scope), containAllFieldsIn(message));
  }

  // syntax = "proto2";
  //
  // import "google/protobuf/descriptor.proto";
  //
  // message Type {
  //   optional int32 code = 1;
  // }
  //
  // extend google.protobuf.FieldOptions {
  //   optional Type type = 15478479;
  // }
  //
  // message Address {
  //   optional int target = 1 [(type) = { code: 68 }];
  // }
  @Test public void should_provide_sources_for_field_notation_in_field_option() {
    ValueField field = xtext.find("code", ":", ValueField.class);
    NormalFieldName name = (NormalFieldName) field.getName();
    IScope scope = provider.scope_FieldName_target(name, reference);
    Message message = xtext.find("Type", " {", Message.class);
    assertThat(descriptionsIn(scope), containAllFieldsIn(message));
  }

  // syntax = "proto2";
  //
  // import "google/protobuf/descriptor.proto";
  //
  // message Type {
  //   optional int32 code = 1;
  //   optional Names name = 2;
  // }
  //
  // message Names {
  //   optional string value = 1;
  // }
  //
  // extend google.protobuf.FieldOptions {
  //   optional Type type = 15478479;
  // }
  //
  // message Address {
  //   optional int target = 1 [(type) = { name: { value: 'Address' } }];
  // }
  @Test public void should_provide_sources_for_nested_field_notation_in_field_option() {
    ValueField field = xtext.find("value", ":", ValueField.class);
    NormalFieldName name = (NormalFieldName) field.getName();
    IScope scope = provider.scope_FieldName_target(name, reference);
    Message message = xtext.find("Names", " {", Message.class);
    assertThat(descriptionsIn(scope), containAllFieldsIn(message));
  }
}