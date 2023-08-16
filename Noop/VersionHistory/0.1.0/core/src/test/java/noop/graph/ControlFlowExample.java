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
public class ControlFlowExample extends Example {
  public UUID uid;

  public ControlFlowExample(StandardLibraryBuilder stdLib) {
    super(stdLib);
  }

  @Override
  public void createProgram(Controller controller) {
    Project project = new Project("Control Flow", "com.example", "Copyright 2010\nExample Co.");
    uid = UUID.randomUUID();
    Library library = new Library(uid, "Testing loops");
    project.addLibrary(library);

    Clazz clazz = new Clazz("Iterating Printer");
    library.addClazz(clazz);

    Method method = new Method("Print 1 through 10");
    clazz.addBlock(method);

    Parameter consoleDep = new Parameter("console");
    method.addParameter(consoleDep);

    IdentifierDeclaration i = new IdentifierDeclaration("count");
    method.addStatement(i);

    i.setInitialValue(new IntegerLiteral(0));

    Loop loop = new Loop();
    method.addStatement(loop);

    IntegerLiteral ten = new IntegerLiteral(10);
    method.addStatement(ten);

    Expression terminateWhen = new MethodInvocation();
    loop.setTerminationCondition(terminateWhen);

    Block body = new AnonymousBlock();
    loop.setBody(body);

    Expression printValue = new MethodInvocation();
    body.addStatement(printValue);

    controller.apply(new NewProjectOperation(project));
    controller.apply(new NewEdgeOperation(i, TYPEOF, stdLib.intClazz));
    controller.apply(
        new NewEdgeOperation(terminateWhen, TARGET, i),
        new NewEdgeOperation(terminateWhen, INVOKE, stdLib.integerEquals),
        new NewEdgeOperation(terminateWhen, ARG, ten));
    controller.apply(
        new NewEdgeOperation(printValue, TARGET, consoleDep),
        new NewEdgeOperation(printValue, INVOKE, stdLib.printMethod),
        new NewEdgeOperation(printValue, ARG, i));
  }
}
