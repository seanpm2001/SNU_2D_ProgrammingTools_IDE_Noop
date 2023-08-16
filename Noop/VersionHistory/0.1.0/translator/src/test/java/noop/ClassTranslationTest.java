/**
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package noop;

import junit.framework.Assert;
import org.antlr.stringtemplate.CommonGroupLoader;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.tool.ErrorManager;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * @author rdionne@google.com (Robert Dionne)
 *
 * TODO(rdionne): Write the code generation classes that use noop.model.* and test those,
 *     not String Template.
 */
public class ClassTranslationTest {
  private StringTemplateGroup group;

  @Before public void setUp() {
    group = StringTemplateGroup.loadGroup("Java");
  }

  @Test public void shouldOutputAFileTemplate() {
    StringTemplate file = group.getInstanceOf("file");

    file.setAttribute("namespace", "com.google");
    file.setAttribute("imports", "org.json.JSONObject");
    file.setAttribute("class", "class Empty {}");

    assertEquals("package com.google;\n\nimport org.json.JSONObject;\n\nclass Empty {}", file.toString());

  }

  @Test public void shouldOutputAClassTemplate() {
    StringTemplate clazz = group.getInstanceOf("class");

    clazz.setAttribute("documentation", "/**\n * @author\n */");
    clazz.setAttribute("modifiers", "public final");
    clazz.setAttribute("name", "MyClass");
    clazz.setAttribute("interfaces", "List");
    clazz.setAttribute("methods", "void add(int a, int b) {}");

    assertEquals("/**\n * @author\n */\npublic final class MyClass implements List {\n  void add(int a, int b) {}\n}",
        clazz.toString());
  }
}
