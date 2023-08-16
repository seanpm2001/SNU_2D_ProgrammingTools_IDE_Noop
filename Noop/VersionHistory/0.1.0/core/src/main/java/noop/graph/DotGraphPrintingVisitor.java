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

import static java.lang.System.identityHashCode;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class DotGraphPrintingVisitor extends PrintingVisitor {
  private final PrintStream out;

  public DotGraphPrintingVisitor(PrintStream out) {
    this.out = out;
  }

  @Override
  public void visit(Workspace workspace) {
    this.workspace = workspace;
    out.format("digraph workspace\n{\n");
    out.format("workspace [label=\"%s\" %s]\n", "Workspace", "shape=house");
    for (Project project : workspace.getProjects()) {
      out.format("workspace -> %s\n", identityHashCode(project));
    }
  }

  public void print(LanguageElement element, String label, String... params) {
    out.format("%s [label=\"%s\"]\n", element.vertex.hashCode(), escape(String.format(label, params)));
    for (Edge edge : library.edgesFrom(element.vertex)) {
      out.format("%s -> %s ", edge.src.hashCode(), edge.dest.hashCode());
      out.println("[label=\"" + edge.type.name().toLowerCase() + "\", style=dashed]");
    }
    if (getParent() != null && getParent().vertex != Vertex.NONE) {
      out.format("%s -> %s\n", getParent().vertex.hashCode(), element.vertex.hashCode());
    }
  }

  @Override
  public void visit(Project project) {
    out.format("%s [label=\"%s\"%s]\n", identityHashCode(project),
        String.format("%s -> %s", project.getNamespace(), project.getName()), "shape=box");
    for (Library library : project.getLibraries()) {
      out.format("%s -> %s\n", identityHashCode(project), library.vertex.hashCode());
    }
    out.format("%s [label=\"%s\", shape=none]\n", identityHashCode(project.getCopyright()),
        escape(project.getCopyright()));
    out.format("%s -> %s\n", identityHashCode(project), identityHashCode(project.getCopyright()));
  }

  @Override
  public void visit(IdentifierDeclaration identifierDeclaration) {
    print(identifierDeclaration, identifierDeclaration.name);
  }

  @Override
  public void visit(Documentation documentation) {
    if (documentation != Documentation.NONE) {
      out.format("%s [label=\"%s\"%s]\n", identityHashCode(documentation), escape(documentation.summary), "shape=none");
    }
  }

  protected String escape(String value) {
    String escaped = super.escape(value);
    if (escaped.length() > 30) {
      escaped = escaped.substring(0, 27) + "...";
    }
    escaped = escaped.replaceAll("\\\"", "\\\\\"");
    return escaped;
  }

  @Override
  public void leave(LanguageElement element) {
    super.leave(element);
    if (currentDepth == 0) {
      out.println("}");
    }
  }
}
