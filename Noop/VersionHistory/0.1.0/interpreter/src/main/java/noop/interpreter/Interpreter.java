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

import java.io.FileNotFoundException;
import java.util.UUID;

import noop.graph.Controller;
import noop.graph.ModelVisitor;
import noop.graph.VertexCreatingVisitor;
import noop.graph.Workspace;
import noop.interpreter.config.InterpreterOptions;
import noop.model.Block;
import noop.model.Library;
import noop.model.Project;
import noop.operations.NewProjectOperation;
import noop.persistence.LibraryRepository;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Interpreter {
  private final InterpreterOptions options;
  private final ModelVisitor visitor;
  private final Workspace workspace;
  private final LibraryRepository repository;

  @Inject
  public Interpreter(InterpreterOptions options, ModelVisitor visitor, Workspace workspace,
                     LibraryRepository repository) {
    this.options = options;
    this.visitor = visitor;
    this.workspace = workspace;
    this.repository = repository;
  }

  public int run() throws FileNotFoundException {
    Controller controller = new Controller(workspace, new VertexCreatingVisitor());

    for (String libraryPath : options.getLibraryPaths()) {
      CommandLineLibraryNameParser parser = new CommandLineLibraryNameParser(libraryPath).invoke();
      Project project = parser.getProject();
      Library library = repository.load(project, parser.getLibraryName());
      project.addLibrary(library);
      controller.addProject(new NewProjectOperation(project));
    }

    Library mainLib = workspace.lookupLibrary(UUID.fromString(options.getMainLib()));
    if (mainLib == null) {
      throw new IllegalArgumentException("No library found with id " + options.getMainLib());
    }

    Block entryPoint = (Block) mainLib.getElements().get(options.getEntryPoint());
    if (entryPoint == null) {
      throw new IllegalArgumentException("No block found named " + options.getEntryPoint());
    }
    
    System.out.println("entryPoint to execute: " + entryPoint.name);
    return 0;
  }
}
