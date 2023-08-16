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

import noop.inject.{GuiceBackedInjector, Injector}
import noop.interpreter._
import noop.grammar.Parser;
import noop.model.Modifier;

import collection.mutable.Stack

import com.google.inject.Guice
import java.io.File;

import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class StringSpec extends Spec with ShouldMatchers {
  def fixture = Guice.createInjector(new InterpreterModule(List()), new NoopTypesModule());

  describe("a Noop String") {
    it("should have a valid class definition parsed from Noop source") {
      val classLoader = fixture.getInstance(classOf[ClassLoader]);
      val classDef = classLoader.findClass("noop.String");
      classDef.name should be("String");
    }

    it("should have a native implementation of the length method") {
      val injector = fixture;
      val classLoader = injector.getInstance(classOf[ClassLoader]);
      val stringClass = classLoader.findClass("noop.String");

      val aString = injector.getInstance(classOf[StringFactory]).create("hello");
      val method = stringClass.findMethod("length");
      val context = injector.getInstance(classOf[Context]);

      context.addRootFrame(null);
      method.modifiers should contain(Modifier.native);

      context.stack.push(new Frame(aString, null));
      injector.getInstance(classOf[InterpreterVisitor]).visit(method);

      val theString = context.stack.top.lastEvaluated(0);

      theString should not be (null);
      theString.asInstanceOf[NoopInteger].value should be(5);
    }
  }
}
