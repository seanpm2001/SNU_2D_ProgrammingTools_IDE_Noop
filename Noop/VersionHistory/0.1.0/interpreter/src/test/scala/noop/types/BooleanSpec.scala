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
package noop.types;

import com.google.inject.Guice
import inject.{GuiceBackedInjector, Injector}
import interpreter._
import java.io.File;

import collection.mutable.Stack;

import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

import grammar.Parser;

import model.Modifier;

/**
 * @author Erik Soe Sorensen (eriksoe@gmail.com)
 */
class BooleanSpec extends Spec with ShouldMatchers {
  def fixture = Guice.createInjector(new InterpreterModule(List()), new NoopTypesModule());

  describe("a Noop Boolean") {

    it("should have a valid class definition parsed from Noop source") {
      val classLoader = fixture.getInstance(classOf[ClassLoader]);
      val classDef = classLoader.findClass("noop.Boolean");
      classDef.name should be("Boolean");
    }

    it("should have a native implementation of the xor method") {
      val injector = fixture;
      val classLoader = injector.getInstance(classOf[ClassLoader]);
      val boolClass = classLoader.findClass("noop.Boolean");
      val booleanFactory = injector.getInstance(classOf[BooleanFactory]);
      val aTrue = booleanFactory.create(true);
      val aFalse = booleanFactory.create(false);
      val method = boolClass.findMethod("xor");
      val context = injector.getInstance(classOf[Context]);

      context.addRootFrame(null);
      method.modifiers should contain(Modifier.native);
      val frame = new Frame(aTrue, null);
      context.stack.push(frame);
      frame.blockScopes.inScope("boolean test") {
        frame.addIdentifier("other", (null, aFalse));

        injector.getInstance(classOf[InterpreterVisitor]).visit(method);
      }
      val theBool = context.stack.top.lastEvaluated(0);
      theBool should not be (null);
      theBool.asInstanceOf[NoopBoolean].value should be(true);
    }
  }
}
