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

package noop.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import noop.graph.Edge;
import noop.graph.ModelVisitor;
import noop.graph.Vertex;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Library extends LanguageElement<Library> {
  public final UUID uid;
  public final String name;
  private final List<List<Edge>> edges = newArrayList();
  private final List<LanguageElement> elements = newArrayList();
  private final List<Clazz> classes = newArrayList();
  private final List<Block> functions = newArrayList();

  public Library(UUID uid, String name) {
    this.uid = uid;
    this.name = name;
  }

  public void addClazz(Clazz clazz) {
    this.classes.add(clazz);
  }

  public void addFunction(Function function) {
    this.functions.add(function);
  }

  @Override
  public void accept(ModelVisitor v) {
    v.visit(this);
    for (Block function : functions) {
      v.enter(function);
      function.accept(v);
      v.leave(function);
    }
    for (Clazz clazz : classes) {
      v.enter(clazz);
      clazz.accept(v);
      v.leave(clazz);
    }
    super.accept(v);
  }


  public Iterable<Edge> edgesFrom(final Vertex src) {
    if (src.index >= edges.size()) {
      return emptyList();
    }
    return edges.get(src.index);
  }

  public List<LanguageElement> getElements() {
    return ImmutableList.copyOf(elements);
  }

  public int add(LanguageElement languageElement) {
    elements.add(languageElement);
    return elements.size() - 1;
  }

  public <T extends LanguageElement> void replace(int index, LanguageElement<T> newValue) {
    elements.get(index).setPreviousVersion(newValue);
    elements.set(index, newValue);
  }

  public void addEdge(Edge edge) {
    int index = edge.src.index;
    while (edges.size() <= index) {
      edges.add(Lists.<Edge>newLinkedList());
    }
    edges.get(index).add(edge);
  }
}
