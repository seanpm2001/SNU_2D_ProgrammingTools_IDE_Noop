/**
 * Copyright 2009 Google Inc.
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

/**
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
trait Visitor {

  def visit(assignmentExpression: AssignmentExpression);

  def visit(block: Block);

  def visit(booleanLiteralExpression: BooleanLiteralExpression);

  def visit(dereferenceExpression: DereferenceExpression);

  def visit(evaluatedExpression: EvaluatedExpression);

  def visit(identifierDeclarationExpression: IdentifierDeclarationExpression);

  def visit(identifierExpression: IdentifierExpression);

  def visit(intLiteralExpression: IntLiteralExpression);

  def visit(method: Method);

  def enter(methodInvocationExpression: MethodInvocationExpression);

  def afterArgumentVisit(methodInvocationExpression: MethodInvocationExpression);

  def visit(methodInvocationExpression: MethodInvocationExpression);

  def visit(operatorExpression: OperatorExpression);

  def visit(returnExpression: ReturnExpression);

  def visit(shouldExpression: ShouldExpression);

  def visit(stringLiteralExpression: StringLiteralExpression);

  def visit(whileLoop: WhileLoop);

  def visit(bindingDeclaration: BindingDeclaration);

  def visit(conditionalAndExpression: ConditionalAndExpression);

  def visit(conditionalOrExpression: ConditionalOrExpression);
}
