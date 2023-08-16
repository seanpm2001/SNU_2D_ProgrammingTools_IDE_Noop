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

import noop.inject.Injector
import noop.model.{Visitor, IdentifierDeclarationExpression, ClassDefinition, DereferenceExpression,
    EvaluatedExpression, IdentifierExpression}
import noop.types.{BooleanFactory, StringFactory, NoopObject, NoopString};


import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class InterpreterVisitorSpec extends Spec with ShouldMatchers with GuiceInterpreterFixture {
  def interpreterFixture = {
    val injector = fixture;
    (injector.getInstance(classOf[Context]), injector.getInstance(classOf[Visitor]), injector);
  }
  describe("the interpreter visitor") {
    it("should visit an identifier declaration and get the initial value from the eval stack") {
      val (context, visitor, injector) = interpreterFixture;
      val identifierDeclaration = new IdentifierDeclarationExpression("String", "s");

      context.stack.top.lastEvaluated += injector.getInstance(classOf[BooleanFactory]).create(true);
      context.stack.top.lastEvaluated += injector.getInstance(classOf[StringFactory]).create("hello");

      context.stack.top.blockScopes.inScope("interpreter test") {
        visitor.visit(identifierDeclaration);
        context.stack.top.blockScopes.getIdentifier("s")._2.getClass() should be(classOf[NoopString]);
      }
    }

    it("should dereference a property from a referenced object") {
      val (context, visitor, injector) = interpreterFixture;
      val stringFactory = injector.getInstance(classOf[StringFactory]);
      val anObject = new NoopObject(new ClassDefinition("Obj", "", ""));
      anObject.propertyMap += Pair("foo", stringFactory.create("bar"));
      context.stack.top.blockScopes.inScope("interpereter_tast") {
        val deref = new DereferenceExpression(new EvaluatedExpression(anObject), new IdentifierExpression("foo"))
        deref.accept(visitor);
      }

      context.stack.top.lastEvaluated should have length(1);
      context.stack.top.lastEvaluated.top.getClass() should be(classOf[NoopString])
      context.stack.top.lastEvaluated.top.asInstanceOf[NoopString].value should be ("bar");
    }


  }
}