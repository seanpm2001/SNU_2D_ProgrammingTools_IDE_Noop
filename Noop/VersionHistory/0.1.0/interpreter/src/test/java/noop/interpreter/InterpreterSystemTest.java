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

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import noop.graph.Controller;
import noop.interpreter.config.InterpreterModule;
import noop.interpreter.config.OptionsBuilder;
import noop.model.Function;
import noop.model.Library;
import noop.model.Project;
import noop.operations.NewProjectOperation;
import noop.persistence.LibraryRepository;
import noop.stdlib.StandardLibraryBuilder;
import static org.junit.Assert.assertEquals;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class InterpreterSystemTest {
  private Controller controller;
  private LibraryRepository repository;

  @Before public void setUp() {
    InterpreterMain.disableSystemExitForTesting();
    Injector injector = Guice.createInjector(new InterpreterModule(new OptionsBuilder().build()));
    controller = injector.getInstance(Controller.class);
    repository = injector.getInstance(LibraryRepository.class);
  }

  @Test public void shouldRunTheHelloWorldProgram() throws Exception {
    UUID uuid = UUID.randomUUID();    
    Project project = new Project("Hello World", "com.example", "");
    project.addLibrary(new Library(uuid, "hello")).addFunction(new Function("go"));
    controller.addProject(new NewProjectOperation(project));
    repository.save(project);
    StandardLibraryBuilder stdLib = new StandardLibraryBuilder().build(controller);
    repository.save(stdLib.noop);

    InterpreterMain.main(new String[] {
        "-lib", "com.google.Noop/io",
        "-lib", "com.google.Noop/lang",
        "-lib", "com.example.Hello World/hello",
        "-v",
        uuid.toString(), "1"
        });
    assertEquals(0, InterpreterMain.exitCodeForTesting);
  }
}
