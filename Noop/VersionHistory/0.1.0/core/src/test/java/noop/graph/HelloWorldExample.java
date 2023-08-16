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
import noop.operations.NewEdgeOperation;
import noop.operations.NewProjectOperation;
import noop.stdlib.StandardLibraryBuilder;

import java.util.UUID;

import static noop.graph.Edge.EdgeType.*;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class HelloWorldExample extends Example {
  public UUID uid;

  public HelloWorldExample(StandardLibraryBuilder stdLib) {
    super(stdLib);
  }

  @Override
  public void createProgram(Controller controller) {
    Project project = new Project("Hello World", "com.example", "Copyright 2010\nExample Co.");

    uid = UUID.randomUUID();
    Library library = new Library(uid, "hello");
    project.addLibrary(library);

    Function sayHello = new Function("Say hello");
    library.addFunction(sayHello);

    Parameter consoleDep = new Parameter("console");
    sayHello.addParameter(consoleDep);


    sayHello.setDocumentation(new Documentation("This is the entry point for the Hello World app",
        "alexeagle@google.com (Alex Eagle)"));

    StringLiteral helloWorld = new StringLiteral("Hello, World!");
    sayHello.addStatement(helloWorld);

    Expression printHello = new MethodInvocation();
    sayHello.addStatement(printHello);

    IntegerLiteral zero = new IntegerLiteral(0);
    sayHello.addStatement(zero);

    Return aReturn = new Return();
    sayHello.addStatement(aReturn);

    UnitTest unitTest = new UnitTest("Should say hello");
    sayHello.addUnitTest(unitTest);

    IdentifierDeclaration resultDecl = new IdentifierDeclaration("result");
    unitTest.addStatement(resultDecl);

    Expression callMain = new MethodInvocation();
    unitTest.addStatement(callMain);

    Expression assertion = new MethodInvocation();
    unitTest.addStatement(assertion);
    // TODO: fill in assertion

    controller.apply(new NewProjectOperation(project));
    controller.apply(
        new NewEdgeOperation(sayHello, TYPEOF, stdLib.intClazz),
        new NewEdgeOperation(consoleDep, TYPEOF, stdLib.consoleClazz));
    controller.apply(new NewEdgeOperation(helloWorld, TYPEOF, stdLib.stringClazz));
    controller.apply(
        new NewEdgeOperation(printHello, TARGET, consoleDep),
        new NewEdgeOperation(printHello, INVOKE, stdLib.printMethod),
        new NewEdgeOperation(printHello, ARG, helloWorld));
    controller.apply(
        new NewEdgeOperation(zero, TYPEOF, stdLib.intClazz),
        new NewEdgeOperation(aReturn, ARG, zero));
    controller.apply(new NewEdgeOperation(resultDecl, TYPEOF, stdLib.intClazz));
    controller.apply(new NewEdgeOperation(callMain, INVOKE, sayHello));
  }
}
