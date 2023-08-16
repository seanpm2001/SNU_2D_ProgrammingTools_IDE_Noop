/*
 * Copyright 2010 Google Inc.
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

package noop.graph;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import noop.model.Library;
import noop.persistence.ModelSerializer;
import noop.persistence.ModelSerializer.SerializationFormat;
import noop.stdlib.StandardLibraryBuilder;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class HelloWorldExampleTest {
  private PrintStream out = new PrintStream(new ByteArrayOutputStream());
  private Library library;

  @Before
  public void setUp() {
    Workspace workspace = new Workspace();
    StandardLibraryBuilder stdLib = new StandardLibraryBuilder();
    Controller controller = new Controller(workspace, new VertexCreatingVisitor());
    stdLib.build(controller);
    HelloWorldExample helloWorldExample = new HelloWorldExample(stdLib);
    helloWorldExample.createProgram(controller);
    library = workspace.lookupLibrary(helloWorldExample.uid);
  }

  @Test
  public void shouldCreateHelloWorldDot() {
    new ModelSerializer(SerializationFormat.DOT, null).write(library, out);
  }

  @Test
  public void shouldCreateHelloWorldOutline() {
    new ModelSerializer(SerializationFormat.TXT, null).write(library, out);
  }
}
