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

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public abstract class DefaultModelVisitor implements ModelVisitor {
  @Override
  public void enter(LanguageElement element) {}
  
  @Override
  public void leave(LanguageElement element) {}

  @Override
  public void visit(Edge edge) {}

  @Override
  public void visit(Workspace workspace) {}

  @Override
  public void visit(Method method) {}

  @Override
  public void visit(Function function) {}

  @Override
  public void visit(UnitTest unitTest) {}

  @Override
  public void visit(Project project) {}

  @Override
  public void visit(MethodInvocation methodInvocation) {}

  @Override
  public void visit(Parameter parameter) {}

  @Override
  public void visit(Library library) {}

  @Override
  public void visit(Clazz clazz) {}

  @Override
  public void visit(StringLiteral stringLiteral) {}

  @Override
  public void visit(Return aReturn) {}

  @Override
  public void visit(IntegerLiteral integerLiteral) {}

  @Override
  public void visit(Documentation documentation) {}

  @Override
  public void visit(Assignment assignment) {}

  @Override
  public void visit(IdentifierDeclaration identifierDeclaration) {}

  @Override
  public void visit(Binding binding) {}

  @Override
  public void visit(Comment comment) {}

  @Override
  public void visit(Loop loop) {}

  @Override
  public void visit(AnonymousBlock block) {}
}
