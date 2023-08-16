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

package noop.operations;

import noop.graph.Controller;
import noop.graph.Edge.EdgeType;
import noop.model.LanguageElement;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class NewEdgeOperation implements MutationOperation {
  public final LanguageElement src;
  public final EdgeType type;
  public final LanguageElement dest;

  public NewEdgeOperation(LanguageElement src, EdgeType type, LanguageElement dest) {
    this.src = src;
    this.type = type;
    this.dest = dest;
  }

  @Override
  public void execute(Controller controller) {
    controller.addEdge(this);
  }
}
