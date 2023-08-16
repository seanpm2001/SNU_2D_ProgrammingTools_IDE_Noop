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

import java.util.Stack;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public abstract class PrintingVisitor extends DefaultModelVisitor {
  protected Workspace workspace;
  protected Library library;
  protected int currentDepth;
  protected Stack<LanguageElement> parents = new Stack<LanguageElement>();

  protected LanguageElement getParent() {
    if (parents.size() < 2) {
      return null;
    }
    return parents.elementAt(parents.size() - 2);
  }

  @Override
  public void enter(LanguageElement element) {
    parents.push(element);
    currentDepth++;
  }

  @Override
  public void leave(LanguageElement element) {
    parents.pop();
    currentDepth--;
  }

  protected String escape(String value) {
    return value.replaceAll("\n", "\\\\n");
  }

  public abstract void print(LanguageElement element, String label, String... params);

  @Override
  public void visit(Library library) {
    this.library = library;
    print(library, "Library \"%s\"", library.name);
  }

  @Override
  public void visit(Clazz clazz) {
    print(clazz, "Class \"%s\"", clazz.name);
  }

  @Override
  public void visit(Method method) {
    print(method, "Method %s{}", method.name);
  }

  @Override
  public void visit(Function function) {
    print(function, "Function %s{}", function.name);
  }

  @Override
  public void visit(UnitTest unitTest) {
    print(unitTest, "Unit test %s", unitTest.name);
  }

  @Override
  public void visit(MethodInvocation methodInvocation) {
    print(methodInvocation, "invocation");
  }

  @Override
  public void visit(Parameter parameter) {
    print(parameter, "parameter %s", parameter.name);
  }

  @Override
  public void visit(AnonymousBlock block) {
    print(block, "{}");
  }

  @Override
  public void visit(Return aReturn) {
    print(aReturn, "[return]");
  }

  @Override
  public void visit(IntegerLiteral integerLiteral) {
    print(integerLiteral, "literal %s", String.valueOf(integerLiteral.value));
  }

  @Override
  public void visit(Loop loop) {
    print(loop, "Loop until");
  }

  @Override
  public void visit(Assignment assignment) {
    print(assignment, "Assign");
  }

  @Override
  public void visit(StringLiteral stringLiteral) {
    print(stringLiteral, "literal \"%s\"", stringLiteral.value);
  }
}
