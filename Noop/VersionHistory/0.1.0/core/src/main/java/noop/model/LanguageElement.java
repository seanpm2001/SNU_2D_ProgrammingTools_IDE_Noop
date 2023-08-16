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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import noop.graph.ModelVisitor;
import noop.graph.Vertex;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public abstract class LanguageElement<T> implements Serializable, Visitable {
  public Vertex vertex = Vertex.NONE;
  protected Documentation documentation = Documentation.NONE;
  protected List<Comment> comments = Lists.newArrayList();  
  protected Set<UnitTest> unitTests = Sets.newHashSet();
  protected T previousVersion;

  @Override
  public void accept(ModelVisitor v) {
    v.enter(documentation);
    documentation.accept(v);
    v.leave(documentation);
    for (Comment comment : comments) {
      v.enter(comment);
      comment.accept(v);
      v.leave(comment);
    }
  }

  public T getPreviousVersion() {
    return previousVersion;
  }

  public void setPreviousVersion(T previousVersion) {
    this.previousVersion = previousVersion;
  }

  public void setDocumentation(Documentation documentation) {
    this.documentation = documentation;
  }

  public void addComment(Comment comment) {
    this.comments.add(comment);
  }

  // TODO: not sure that unit tests can be attached literally anywhere
  public void addUnitTest(UnitTest unitTest) {
    unitTests.add(unitTest);
  }
}
