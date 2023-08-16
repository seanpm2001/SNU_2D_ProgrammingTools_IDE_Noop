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
package noop.interpreter

import noop.interpreter.testing.{TestFailedException};
import noop.model.{ShouldExpression, Visitor, MethodInvocationExpression, StringLiteralExpression};

import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class ShouldExpressionSpec extends Spec with ShouldMatchers with GuiceInterpreterFixture {
   describe("the 'should' operator") {
    it("should be silent if the lefthand side matches an equals matcher") {
      val matcher = new MethodInvocationExpression(null, "equal", List(new StringLiteralExpression("hello")));
      val shouldExpr = new ShouldExpression(new StringLiteralExpression("hello"), matcher);
      val injector = fixture;
      val visitor = injector.getInstance(classOf[Visitor]);
      val context = injector.getInstance(classOf[Context]);

      shouldExpr.accept(visitor);

      // TODO(jeremiele): no idea if it is the correct behavior
      context.stack.top.lastEvaluated.size should be (0);
    }

    it("should throw a test failed exception if an equals matcher is not satisfied") {
      val matcher = new MethodInvocationExpression(null, "equal", List(new StringLiteralExpression("goodbye")));
      val shouldExpr = new ShouldExpression(new StringLiteralExpression("hello"), matcher);
      val injector = fixture;
      val visitor = injector.getInstance(classOf[Visitor]);

      intercept[TestFailedException] {
        shouldExpr.accept(visitor);
      }
    }
  }
}
