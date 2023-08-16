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

import com.google.inject.Guice;

import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

import noop.model.{Block, IntLiteralExpression, OperatorExpression, Visitor};
import noop.types.{IntegerFactory, NoopBoolean, NoopTypesModule};

/**
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class ConditionalSpec extends Spec with ShouldMatchers {

  def createFixture = {
    val injector = Guice.createInjector(new InterpreterModule(List()), new NoopTypesModule());
    val context = injector.getInstance(classOf[Context]);
    val visitor = injector.getInstance(classOf[Visitor]);
    val integerFactory = injector.getInstance(classOf[IntegerFactory]);

    context.addRootFrame(integerFactory.create(1));
    (injector, context, visitor);
  }

  describe("conditional") {

    it("should evaluate to true when equals is called and the values are equal") {
      val (injector, context, visitor) = createFixture;
      val conditional = new OperatorExpression(new IntLiteralExpression(1), "==",
          new IntLiteralExpression(1));

      conditional.accept(visitor);
      context.stack.top.lastEvaluated should have size(1);
      val result = context.stack.top.lastEvaluated.top;

      result.isInstanceOf[NoopBoolean] should be (true);
      result.asInstanceOf[NoopBoolean].value should be (true);
    }

    it("should evaluate to false when equals is called and the values are not equal") {
      val (injector, context, visitor) = createFixture;
      val conditional = new OperatorExpression(new IntLiteralExpression(1), "==",
          new IntLiteralExpression(2));

      conditional.accept(visitor);
      context.stack.top.lastEvaluated should have size(1);
      val result = context.stack.top.lastEvaluated.top;

      result.isInstanceOf[NoopBoolean] should be (true);
      result.asInstanceOf[NoopBoolean].value should be (false);      
    }

    it("should evaluate to true when doesNotEqual is called and the values are not equal") {
      val (injector, context, visitor) = createFixture;
      val conditional = new OperatorExpression(new IntLiteralExpression(1), "!=",
          new IntLiteralExpression(2));

      conditional.accept(visitor);
      context.stack.top.lastEvaluated should have size(1);
      val result = context.stack.top.lastEvaluated.top;

      result.isInstanceOf[NoopBoolean] should be (true);
      result.asInstanceOf[NoopBoolean].value should be (true);            
    }

    it("should evaluate to false when doesNotEqual is called and the values are equal") {
      val (injector, context, visitor) = createFixture;
      val conditional = new OperatorExpression(new IntLiteralExpression(1), "!=",
          new IntLiteralExpression(1));

      conditional.accept(visitor);
      context.stack.top.lastEvaluated should have size(1);
      val result = context.stack.top.lastEvaluated.top;

      result.isInstanceOf[NoopBoolean] should be (true);
      result.asInstanceOf[NoopBoolean].value should be (false);
    }

    it("should evaluate to true when greaterThan is called and the value is greater than the other one") {
      val (injector, context, visitor) = createFixture;
      val conditional = new OperatorExpression(new IntLiteralExpression(2), ">",
          new IntLiteralExpression(1));

      conditional.accept(visitor);
      context.stack.top.lastEvaluated should have size(1);
      val result = context.stack.top.lastEvaluated.top;

      result.isInstanceOf[NoopBoolean] should be (true);
      result.asInstanceOf[NoopBoolean].value should be (true);  
    }
 
    it("should evaluate to false when greaterThan is called and the value is less than the other one") {
      val (injector, context, visitor) = createFixture;
      val conditional = new OperatorExpression(new IntLiteralExpression(1), ">",
          new IntLiteralExpression(2));

      conditional.accept(visitor);
      context.stack.top.lastEvaluated should have size(1);
      val result = context.stack.top.lastEvaluated.top;

      result.isInstanceOf[NoopBoolean] should be (true);
      result.asInstanceOf[NoopBoolean].value should be (false);  
    }

    it("should evaluate to true when lesserThan is called and the value is less than the other one") {
      val (injector, context, visitor) = createFixture;
      val conditional = new OperatorExpression(new IntLiteralExpression(1), "<",
          new IntLiteralExpression(2));

      conditional.accept(visitor);
      context.stack.top.lastEvaluated should have size(1);
      val result = context.stack.top.lastEvaluated.top;

      result.isInstanceOf[NoopBoolean] should be (true);
      result.asInstanceOf[NoopBoolean].value should be (true);
    }

    it("should evaluate to false when lesserThan is called and the value is greater than the other one") {
      val (injector, context, visitor) = createFixture;
      val conditional = new OperatorExpression(new IntLiteralExpression(3), "<",
          new IntLiteralExpression(2));

      conditional.accept(visitor);
      context.stack.top.lastEvaluated should have size(1);
      val result = context.stack.top.lastEvaluated.top;

      result.isInstanceOf[NoopBoolean] should be (true);
      result.asInstanceOf[NoopBoolean].value should be (false);
    }

    it("should evaluate to true when greaterOrEqualThan is called and the value is greater or equal than the other one") {
      val (injector, context, visitor) = createFixture;
      val conditional = new OperatorExpression(new IntLiteralExpression(3), ">=",
          new IntLiteralExpression(2));

      conditional.accept(visitor);
      context.stack.top.lastEvaluated should have size(1);
      val result = context.stack.top.lastEvaluated.top;

      result.isInstanceOf[NoopBoolean] should be (true);
      result.asInstanceOf[NoopBoolean].value should be (true);
    }

    it("should evaluate to false when greaterOrEqualThan is called and the value is lesser than the other one") {
      val (injector, context, visitor) = createFixture;
      val conditional = new OperatorExpression(new IntLiteralExpression(1), ">=",
          new IntLiteralExpression(2));

      conditional.accept(visitor);
      context.stack.top.lastEvaluated should have size(1);
      val result = context.stack.top.lastEvaluated.top;

      result.isInstanceOf[NoopBoolean] should be (true);
      result.asInstanceOf[NoopBoolean].value should be (false);
    }

    it("should evaluate to true when lesserOrEqualThan is called and the value is lesser or equal than the other one") {
      val (injector, context, visitor) = createFixture;
      val conditional = new OperatorExpression(new IntLiteralExpression(1), "<=",
          new IntLiteralExpression(2));

      conditional.accept(visitor);
      context.stack.top.lastEvaluated should have size(1);
      val result = context.stack.top.lastEvaluated.top;

      result.isInstanceOf[NoopBoolean] should be (true);
      result.asInstanceOf[NoopBoolean].value should be (true);
    }

    it("should evaluate to false when lesserOrEqualThan is called and the value is greater than the other one") {
      val (injector, context, visitor) = createFixture;
      val conditional = new OperatorExpression(new IntLiteralExpression(2), "<=",
          new IntLiteralExpression(1));

      conditional.accept(visitor);
      context.stack.top.lastEvaluated should have size(1);
      val result = context.stack.top.lastEvaluated.top;

      result.isInstanceOf[NoopBoolean] should be (true);
      result.asInstanceOf[NoopBoolean].value should be (false);
    }
  }
}
