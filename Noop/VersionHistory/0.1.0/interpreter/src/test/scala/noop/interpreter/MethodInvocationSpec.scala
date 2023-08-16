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

import noop.model._
import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

import noop.types.{NoopString, NoopObject};

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class MethodInvocationSpec extends Spec with ShouldMatchers with GuiceInterpreterFixture {

  def interpreterFixture = {
    val injector = fixture;
    (injector.getInstance(classOf[Context]), injector.getInstance(classOf[Visitor]));
  }

  describe("a method invocation") {

    it("should throw an exception if the method doesn't exist on the type") {
      val (context, visitor) = interpreterFixture;
      val target = new StringLiteralExpression("aString");
      val expr = new MethodInvocationExpression(target, "noSuchMethodSorry", List());
      val exception = intercept[NoSuchMethodException] (
        expr.accept(visitor)
      );
      exception.getMessage() should include("noSuchMethodSorry");
      exception.getMessage() should include("String");
    }

    it("should evaluate the method body in a new stack frame") {
      val (context, visitor) = interpreterFixture;
      val assertInNewFrame = (c: Context) =>
              c.stack.top should not be theSameInstanceAs(context.stack.top);

      val block = new Block();
      block.statements += new MockExpression(assertInNewFrame);
      val myMethod = new Method("length", block, null);
      myMethod.returnTypes += "Int";
      context.classLoader.findClass("noop.String").methods += myMethod;
      val target = new StringLiteralExpression("aString");
      val expr = new MethodInvocationExpression(target, "length", List());
      expr.accept(visitor);
    }

    it("should evaluate arguments and assign them to local variables indicated by the parameters") {
      val (context, visitor) = interpreterFixture;
      val paramName = "other";
      val arg = "argumentStr";
      val assertParamIsSetFromArgument = (c: Context) => {
        val other = c.stack.top.blockScopes.getIdentifier("other")._2;
        other.getClass() should be(classOf[NoopString]);
        other.asInstanceOf[NoopString].value should be(arg);
      }
      val block = new Block();

      block.statements += new MockExpression(assertParamIsSetFromArgument);
      val myMethod = new Method("plus", block, null);
      myMethod.returnTypes += "Void";

      myMethod.parameters += new Parameter(paramName, "String");
      context.classLoader.findClass("noop.String").methods += myMethod;
      val target = new StringLiteralExpression("aString");
      val expr = new MethodInvocationExpression(target, "plus",
          List(new StringLiteralExpression(arg)));
      expr.accept(visitor);
    }

    it("should throw an exception if the number of arguments don't match the parameter count") {
      val (context, visitor) = interpreterFixture;
      val myMethod = new Method("plus", null, null);
      myMethod.returnTypes += "Void";

      myMethod.parameters += new Parameter("other", "String");
      context.classLoader.findClass("noop.String").methods += myMethod;
      val target = new StringLiteralExpression("aString");
      val expr = new MethodInvocationExpression(target, "plus", List());

      intercept[RuntimeException] {
        expr.accept(visitor);
      }
    }

    it("should throw an exception if the evaluated argument does not match the type of the parameter") {
      val (context, visitor) = interpreterFixture;
      val myMethod = new Method("plus", null, null);
      myMethod.returnTypes += "Void";

      myMethod.parameters += new Parameter("other", "String");
      context.classLoader.findClass("noop.String").methods += myMethod;
      val target = new StringLiteralExpression("aString");
      val expr = new MethodInvocationExpression(target, "plus", List(new IntLiteralExpression(1)));

      intercept[RuntimeException] {
        expr.accept(visitor);
      }
    }

    it("should restore the original stack frame when finished even if exception") {
      val (context, visitor) = interpreterFixture;
      val myMethod = new Method("plus", null, null);
      myMethod.returnTypes += "Void";

      context.classLoader.findClass("noop.String").methods += myMethod;
      val target = new StringLiteralExpression("aString");
      val expr = new MethodInvocationExpression(target, "plus", List());
      val currentFrame = new Frame(null, null);

      context.stack.push(currentFrame);
      intercept[RuntimeException] {
        expr.accept(visitor);
      }
      currentFrame should be theSameInstanceAs(context.stack.top);
    }

    it("should throw an exception if an argument expression returns no value") {
      val (context, visitor) = interpreterFixture;
      val paramName = "other";
      val block = new Block();
      val myMethod = new Method("plus", block, null);
      myMethod.returnTypes += "Void";

      myMethod.parameters += new Parameter(paramName, "String");
      context.classLoader.findClass("noop.String").methods += myMethod;

      val target = new StringLiteralExpression("aString");
      val expr = new MethodInvocationExpression(target, "plus", List(new MockExpression()));

      intercept[RuntimeException] {
        expr.accept(visitor);
      }
    }

    it("should be aware of the 'this' identifier and dispatch the method on thisRef") {
      val (context, visitor) = interpreterFixture;
      new StringLiteralExpression("aString").accept(visitor);
      val thisRef: NoopObject = context.stack.top.lastEvaluated(0);

      thisRef should not be (null);
      context.stack.push(new Frame(thisRef, null));
      val target = new IdentifierExpression("this");
      val expr = new MethodInvocationExpression(target, "length", List());

      expr.accept(visitor);
    }
  }
}
