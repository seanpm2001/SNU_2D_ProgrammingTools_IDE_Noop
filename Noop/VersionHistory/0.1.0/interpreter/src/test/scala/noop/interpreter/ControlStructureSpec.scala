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
package noop.interpreter;

import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

import noop.model.{IdentifierExpression, AssignmentExpression, Block, BooleanLiteralExpression, Expression,
    IdentifierDeclarationExpression, ReturnExpression, StringLiteralExpression, Visitor, WhileLoop};
import noop.types.{NoopString};

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class ControlStructureSpec extends Spec with ShouldMatchers with GuiceInterpreterFixture {

  def interpreterFixture = {
    val injector = fixture;
    val block = new Block();
    (injector.getInstance(classOf[Context]), block, injector.getInstance(classOf[Visitor]));
  }

  class TrueThenFalseExpression(timesToReturnTrue: Int) extends Expression {
    var called = 0;

    def accept(visitor: Visitor) = {
      called += 1;
      new BooleanLiteralExpression(called <= timesToReturnTrue).accept(visitor);
    }
  }

  describe("the while loop") {

    it("should execute the body when the condition is true") {
      val (context, block, visitor) = interpreterFixture;
      val expression: MockExpression = new MockExpression();
      block.statements += expression;

      val whileLoop = new WhileLoop(new TrueThenFalseExpression(1), block);

      whileLoop.accept(visitor);
      expression.timesCalled should be(1);
    }

    it("should not execute the body when the condition is false") {
      val (context, block, visitor) = interpreterFixture;
      val expression: MockExpression = new MockExpression();
      block.statements += expression;

      val whileLoop = new WhileLoop(new BooleanLiteralExpression(false), block);

      whileLoop.accept(visitor);
      expression.timesCalled should be(0);
    }

    it("should execute the body repeatedly as long as the condition is true") {
      val (context, block, visitor) = interpreterFixture;
      val expression: MockExpression = new MockExpression();
      block.statements += expression;

      val whileLoop = new WhileLoop(new TrueThenFalseExpression(3), block);

      whileLoop.accept(visitor);
      expression.timesCalled should be(3);
    }

    it("should scope variables within the block") {
      val (context, block, visitor) = interpreterFixture;
      val expression = new IdentifierDeclarationExpression("String", "s");
      block.statements += expression;

      val whileLoop = new WhileLoop(new TrueThenFalseExpression(2), block);

      // should not throw exception
      whileLoop.accept(visitor);

      val enclosingBlock = new Block();
      enclosingBlock.statements += block;
      enclosingBlock.statements += new AssignmentExpression(
          new IdentifierExpression("s"), new StringLiteralExpression("s"));
      new WhileLoop(new TrueThenFalseExpression(1), enclosingBlock).accept(visitor);
    }
  }

  describe("the return statement") {

    it("should exit the current block and return the supplied value") {
      val (context, block, visitor) = interpreterFixture;
      val returnMe = new ReturnExpression(new StringLiteralExpression("to return"));

      block.statements += returnMe;
      val dontRunMe = new MockExpression((c: Context) => fail());

      block.statements += dontRunMe;
      block.accept(visitor);
      val result = context.stack.top.lastEvaluated(0);

      result should not be (null);
      result.getClass() should be(classOf[NoopString]);
      result.asInstanceOf[NoopString].value should be("to return");
    }
  }
}
