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
public class ArithmeticExample extends Example {
  public UUID uid;

  public ArithmeticExample(StandardLibraryBuilder stdLib) {
    super(stdLib);
  }

  @Override
  public void createProgram(Controller controller) {
    Project project = new Project("Arithmetic", "com.example", "Copyright 2010\nExample Co.");

    uid = UUID.randomUUID();
    Library library = new Library(uid, "adding stuff");
    project.addLibrary(library);

    Function entryPoint = new Function("start here");
    library.addFunction(entryPoint);
    Parameter consoleDep = new Parameter("console");
    entryPoint.addParameter(consoleDep);

    IdentifierDeclaration i = new IdentifierDeclaration("i");
    entryPoint.addStatement(i);

    IntegerLiteral one = new IntegerLiteral(1);
    i.setInitialValue(one);

    IdentifierDeclaration j = new IdentifierDeclaration("j");
    entryPoint.addStatement(j);

    IntegerLiteral two = new IntegerLiteral(2);
    entryPoint.addStatement(two);
    j.setInitialValue(two);

    IdentifierDeclaration k = new IdentifierDeclaration("k");
    entryPoint.addStatement(k);

    Expression sum = new MethodInvocation();
    k.setInitialValue(sum);

    Expression printResult = new MethodInvocation();
    entryPoint.addStatement(printResult);

    IntegerLiteral zero = new IntegerLiteral(0);
    entryPoint.addStatement(zero);

    Expression returnVal = new Return();
    entryPoint.addStatement(returnVal);
    
    controller.apply(new NewProjectOperation(project));
    controller.apply(
        new NewEdgeOperation(entryPoint, TYPEOF, stdLib.intClazz),
        new NewEdgeOperation(consoleDep, TYPEOF, stdLib.consoleClazz));
    controller.apply(
        new NewEdgeOperation(sum, INVOKE, stdLib.integerPlus),
        new NewEdgeOperation(sum, TARGET, i),
        new NewEdgeOperation(sum, ARG, j));
    controller.apply(
        new NewEdgeOperation(printResult, INVOKE, stdLib.printMethod),
        new NewEdgeOperation(printResult, TARGET, consoleDep),
        new NewEdgeOperation(printResult, ARG, k));
    controller.apply(new NewEdgeOperation(returnVal, ARG, zero));
  }
}
