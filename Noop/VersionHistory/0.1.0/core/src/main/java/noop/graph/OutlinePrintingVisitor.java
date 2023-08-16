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

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class OutlinePrintingVisitor extends PrintingVisitor {
  private final PrintStream out;

  public OutlinePrintingVisitor(PrintStream out) {
    this.out = out;
  }

  @Override
  public void visit(Workspace workspace) {
    this.workspace = workspace;
    print(workspace, "Contents of workspace %s at %s",
        System.getProperty("user.name"), new SimpleDateFormat().format(new Date()));
  }

  @Override
  public void visit(Project project) {
    print(project, "Project \"%s\"->\"%s\" (copyright: \"%s\")",
        project.getNamespace(), project.getName(), escape(project.getCopyright()));
  }

  @Override
  public void visit(Documentation documentation) {
    if (documentation != Documentation.NONE)
    out.format("%s%s\n", indent(), String.format("Documentation: %s", documentation.summary));
  }

  @Override
  public void visit(IdentifierDeclaration identifierDeclaration) {
    print(identifierDeclaration, "Declare %s", identifierDeclaration.name);
  }

  @Override
  public void visit(Comment comment) {
    print(comment, "// %s --%s %s", comment.text, comment.user, comment.timestamp.toString());
  }

  public void print(LanguageElement element, String message, String... params) {
    out.format("%s%s [#%s]", indent(), String.format(message, params), element.vertex);
    if (element.vertex != Vertex.NONE) {
      for (Edge edge : library.edgesFrom(element.vertex)) {
        out.format(" %s -> #%s", edge.type.name(), edge.dest);
      }
    }
    out.println();
  }

  private String indent() {
    StringBuilder builder = new StringBuilder((currentDepth - 1) * 2);
    for (int i = 0; i < currentDepth - 1; i++) {
      builder.append("  ");
    }
    return builder.toString();
  }
}
