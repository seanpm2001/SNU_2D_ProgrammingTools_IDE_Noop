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

import com.google.inject.Inject;

import noop.model.LanguageElement;
import noop.model.Library;
import noop.operations.EditNodeOperation;
import noop.operations.MutationOperation;
import noop.operations.NewEdgeOperation;
import noop.operations.NewProjectOperation;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Controller {
  private Workspace workspace;
  private final ModelVisitor addVertices;

  @Inject
  public Controller(Workspace workspace, VertexCreatingVisitor addVertices) {
    this.workspace = workspace;
    this.addVertices = addVertices;
  }

  public void editNode(EditNodeOperation operation) {
    Library library = workspace.lookupLibrary(operation.vertex.libraryUid);
    LanguageElement currentValue = library.getElements().get(operation.vertex.index);
    if (currentValue.getClass() != operation.newValue.getClass()) {
      throw new IllegalArgumentException(String.format("Cannot edit node %s with %s because the current type is %s",
          operation.vertex, operation.newValue, currentValue.getClass()));
    }
    library.replace(operation.vertex.index, operation.newValue);
  }

  public void addEdge(NewEdgeOperation operation) {
    if (operation.src.vertex == Vertex.NONE) {
      throw new IllegalArgumentException("src element has no vertex");
    }
    if (operation.dest.vertex == Vertex.NONE) {
      throw new IllegalArgumentException("dest element has no vertex");
    }
    Library srcLibrary = workspace.lookupLibrary(operation.src.vertex.libraryUid);
    srcLibrary.addEdge(new Edge(operation.src.vertex, operation.type, operation.dest.vertex));
  }

  public void addProject(NewProjectOperation operation) {
    workspace.addProject(operation.project);
    operation.project.accept(addVertices);
  }

  public void apply(MutationOperation... operations) {
    for (MutationOperation operation : operations) {
      operation.execute(this);
    }
  }
}
