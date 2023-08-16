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

package noop.interpreter;

import com.google.inject.Inject;
import noop.graph.DefaultModelVisitor;
import noop.graph.Edge;
import noop.graph.ModelVisitor;
import noop.graph.Workspace;
import noop.model.LanguageElement;
import noop.model.Library;
import noop.model.MethodInvocation;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class InterpreterVisitor extends DefaultModelVisitor {
  private final Context context;
  private final Workspace workspace;
  private final ModelVisitor modelVisitor;

  @Inject
  public InterpreterVisitor(Context context, Workspace workspace, ModelVisitor modelVisitor) {
    this.context = context;
    this.workspace = workspace;
    this.modelVisitor = modelVisitor;
  }

  @Override
  public void visit(MethodInvocation methodInvocation) {
    Library library = workspace.lookupLibrary(methodInvocation.vertex.libraryUid);
    Iterable<Edge> edges = library.edgesFrom(methodInvocation.vertex);
    LanguageElement target = null;
    for (Edge edge : edges) {
      switch (edge.type) {
        case TARGET:
          target = workspace.resolve(edge.dest);
          break;
      }
    }

    modelVisitor.enter(target);
    target.accept(modelVisitor);
    modelVisitor.leave(target);
    
  }
}
