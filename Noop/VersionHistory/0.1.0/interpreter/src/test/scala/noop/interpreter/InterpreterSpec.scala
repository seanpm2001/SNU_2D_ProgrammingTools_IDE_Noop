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


import noop.types.{NoopTypesModule, NoopInteger};
import noop.model.{Visitor, IntLiteralExpression, OperatorExpression}
import noop.grammar.Parser;

import com.google.inject.Guice;
import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class InterpreterSpec extends Spec with ShouldMatchers {
  describe("integration tests for the interpreter") {

    def createFixture = {
      val injector = Guice.createInjector(new InterpreterModule(List()), new NoopTypesModule());
      val context: Context = injector.getInstance(classOf[Context]);
      context.addRootFrame(null);

      (context, injector.getInstance(classOf[Visitor]));

    }

    it("should evaluate simple arithmetic") {
      val (context, visitor) = createFixture;
      val expr = new OperatorExpression(new IntLiteralExpression(2), "+", new IntLiteralExpression(3));

      expr.accept(visitor);
      val result = context.stack.top.lastEvaluated(0);

      result should not be (null);
      result.asInstanceOf[NoopInteger].value should be (5);
    }

    it("should evaluate more complex arithmetic") {
      val source = "{ (1 + 2) * 3 - 10 / 2 % 4; }";
      val (context, visitor) = createFixture;
      val parser = new Parser();
      val block = parser.buildTreeParser(parser.parseBlock(source)).block();

      block.statements(0).accept(visitor);
      val result = context.stack.top.lastEvaluated(0);

      result should not be (null);
      result.asInstanceOf[NoopInteger].value should be (8);
    }

    it("should assign variables") {
      val source = "{ Int a; a = 1; }";
      val (context, visitor) = createFixture;
      val parser = new Parser();
      val block = parser.buildTreeParser(parser.parseBlock(source)).block();

      context.stack.top.blockScopes.inScope("interpreter test") {
        block.accept(visitor);
        context.stack.top.blockScopes.scopes.top should contain key("a");
        context.stack.top.blockScopes.getIdentifier("a")._2 should not be (null);
        context.stack.top.blockScopes.getIdentifier("a")._2.asInstanceOf[NoopInteger].value should equal (1);
      }
    }
  }
}
