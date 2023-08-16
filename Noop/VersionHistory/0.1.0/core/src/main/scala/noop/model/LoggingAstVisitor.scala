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

import org.slf4j.LoggerFactory;

/**
 * A visitor which simply logs when it visits a model element.
 *
 * @author alexeagle@google.com (Alex Eagle)
 */
class LoggingAstVisitor extends Visitor {

  val logger = LoggerFactory.getLogger(this.getClass());

  def visit(assignmentExpression: AssignmentExpression) = {
    logger.info("assignment: {} {}", assignmentExpression.lhs, assignmentExpression.rhs);
  }

  def visit(block: Block) = {
    logger.info("block");
  }

  def visit(booleanLiteralExpression: BooleanLiteralExpression) = {
    logger.info("boolean");
  }

  def visit(dereferenceExpression: DereferenceExpression) = {
    logger.info("dereference");
  }

  def visit(evaluatedExpression: EvaluatedExpression) = {
    logger.info("pre-evaluated: " + evaluatedExpression.value);
  }

  def visit(identifierDeclarationExpression: IdentifierDeclarationExpression) = {
    logger.info("identifier declared");
  }

  def visit(identifierExpression: IdentifierExpression) = {
    logger.info("identifier");
  }

  def visit(intLiteralExpression: IntLiteralExpression) = {
    logger.info("Int literal");
  }

  def visit(method: Method) = {
    logger.info("Method: " + method.name);
  }

  def enter(methodInvocationExpression: MethodInvocationExpression) = {
    logger.info("Entering method call on " + methodInvocationExpression.name);
  }

  def afterArgumentVisit(methodInvocationExpression: MethodInvocationExpression) = {
    logger.info("evaluated an argument");
  }

  def visit(methodInvocationExpression: MethodInvocationExpression) = {
    logger.info("calling method " + methodInvocationExpression.name);
  }

  def visit(operatorExpression: OperatorExpression) = {
    logger.info("operator");
  }

  def visit(returnExpression: ReturnExpression) = {
    logger.info("returning");
  }

  def visit(shouldExpression: ShouldExpression) = {
    logger.info("should");
  }

  def visit(stringLiteralExpression: StringLiteralExpression) = {
    logger.info("String literal: " + stringLiteralExpression.value);
  }

  def visit(whileLoop: WhileLoop) = {
    logger.info("while loop");
  }

  def visit(bindingDeclaration: BindingDeclaration) = {
    logger.info("binding {} to {}", bindingDeclaration.noopType, bindingDeclaration.boundTo);
  }

  def visit(conditionalAndExpression: ConditionalAndExpression) = {
    logger.info("and conditional expression");
  }

  def visit(conditionalOrExpression: ConditionalOrExpression) = {
    logger.info("or conditional expression");
  }
}
