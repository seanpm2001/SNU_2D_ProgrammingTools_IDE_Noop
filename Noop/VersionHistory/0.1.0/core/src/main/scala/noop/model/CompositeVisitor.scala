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
 * @author alexeagle@google.com (Alex Eagle)
 * TODO(alex): looks like we could have a single visit(Expression) method
 */
class CompositeVisitor(visitors: Seq[Visitor]) extends Visitor {

  def visit(assignmentExpression: AssignmentExpression) {
    for (v <- visitors) {
      v.visit(assignmentExpression);
    }
  }

  def visit(block: Block) = {
    for (v <- visitors) {
      v.visit(block);
    }
  }

  def visit(booleanLiteralExpression: BooleanLiteralExpression) = {
    for (v <- visitors) {
      v.visit(booleanLiteralExpression);
    }
  }

  def visit(dereferenceExpression: DereferenceExpression) = {
    for (v <- visitors) {
      v.visit(dereferenceExpression);
    }
  }

  def visit(evaluatedExpression: EvaluatedExpression) = {
    for (v <- visitors) {
      v.visit(evaluatedExpression);
    }
  }

  def visit(identifierDeclarationExpression: IdentifierDeclarationExpression) = {
    for (v <- visitors) {
      v.visit(identifierDeclarationExpression);
    }
  }

  def visit(identifierExpression: IdentifierExpression) = {
    for (v <- visitors) {
      v.visit(identifierExpression);
    }
  }

  def visit(intLiteralExpression: IntLiteralExpression) = {
    for (v <- visitors) {
      v.visit(intLiteralExpression);
    }
  }

  def visit(method: Method) = {
    for (v <- visitors) {
      v.visit(method);
    }
  }

  def enter(methodInvocationExpression: MethodInvocationExpression) = {
    for (v <- visitors) {
      v.enter(methodInvocationExpression);
    }
  }

  def afterArgumentVisit(methodInvocationExpression: MethodInvocationExpression) = {
    for (v <- visitors) {
      v.afterArgumentVisit(methodInvocationExpression);
    }
  }

  def visit(methodInvocationExpression: MethodInvocationExpression) = {
    for (v <- visitors) {
      v.visit(methodInvocationExpression);
    }
  }

  def visit(operatorExpression: OperatorExpression) = {
    for (v <- visitors) {
      v.visit(operatorExpression);
    }
  }

  def visit(returnExpression: ReturnExpression) = {
    for (v <- visitors) {
      v.visit(returnExpression);
    }
  }

  def visit(shouldExpression: ShouldExpression) = {
    for (v <- visitors) {
      v.visit(shouldExpression);
    }
  }

  def visit(stringLiteralExpression: StringLiteralExpression) = {
    for (v <- visitors) {
      v.visit(stringLiteralExpression);
    }
  }

  def visit(whileLoop: WhileLoop) = {
    for (v <- visitors) {
      v.visit(whileLoop);
    }
  }

  def visit(bindingDeclaration: BindingDeclaration) = {
    for (v <- visitors) {
      v.visit(bindingDeclaration);
    }
  }

  def visit(conditionalAndExpression: ConditionalAndExpression) = {
    for (v <- visitors) {
      v.visit(conditionalAndExpression);
    }
  }

  def visit(conditionalOrExpression: ConditionalOrExpression) = {
    for (v <- visitors) {
      v.visit(conditionalOrExpression);
    }
  }
}
