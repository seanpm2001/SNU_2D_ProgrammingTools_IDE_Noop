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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;

import java.util.List;
import java.util.UUID;

import noop.model.LanguageElement;
import noop.model.Library;
import noop.model.Project;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
@Singleton
public class Workspace extends LanguageElement<Workspace> {
  private List<Project> projects = Lists.newArrayList();

  public void addProject(Project project) {
    projects.add(project);
  }

  @Override
  public void accept(ModelVisitor v) {
    v.enter(this);
    v.visit(this);
    for (Project project : projects) {
      v.enter(project);
      project.accept(v);
      v.leave(project);
    }
    v.leave(this);
  }

  public List<Project> getProjects() {
    return ImmutableList.copyOf(projects);
  }

  // TODO: cache these lookups
  public Library lookupLibrary(UUID libraryUid) {
    for (Project project : projects) {
      for (Library library : project.getLibraries()) {
        if (library.uid.equals(libraryUid)) {
          return library;
        }
      }
    }
    return null;
  }

  // Look up the language element from the target pointer
  public LanguageElement resolve(Vertex target) {
    return lookupLibrary(target.libraryUid).getElements().get(target.index);
  }
}
