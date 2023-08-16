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

import noop.model.*;
import noop.operations.EditNodeOperation;
import noop.operations.NewProjectOperation;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class ControllerTest {
  private Controller controller;
  private Workspace workspace;
  private Project p;

  @Before
  public void setUp() {
    p = new Project("p", "ns", "copyright");
    workspace = new Workspace();
    controller = new Controller(workspace, new VertexCreatingVisitor());
  }

  @Test public void shouldMakeNewProject() {
    Project project = new Project("helloWorld", "com.google", "");
    controller.apply(new NewProjectOperation(project));
    assertTrue(workspace.getProjects().contains(project));
  }

  @Test public void shouldAllowAddingAMethodToAClazz() {
    Library l = new Library(UUID.randomUUID(), "l");
    p.addLibrary(l);
    Clazz c = new Clazz("c");
    l.addClazz(c);
    Method m = new Method("m");
    c.addBlock(m);
    controller.apply(new NewProjectOperation(p));

    Clazz c2 = new Clazz("c2");
    Method m2 = new Method("m2");
    c2.addBlock(m);
    c2.addBlock(m2);

    controller.apply(new EditNodeOperation(c.vertex, c2));
  }

  @Test public void shouldErrorWhenEditingWithWrongType() {
    Library l = new Library(UUID.randomUUID(), "l");
    p.addLibrary(l);
    Clazz c = new Clazz("c");
    l.addClazz(c);
    controller.apply(new NewProjectOperation(p));
    
    try {
      controller.apply(new EditNodeOperation(c.vertex, new StringLiteral("String is not Clazz")));
      fail("should throw IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage(), e.getMessage().contains("Clazz"));
    }
  }
}
