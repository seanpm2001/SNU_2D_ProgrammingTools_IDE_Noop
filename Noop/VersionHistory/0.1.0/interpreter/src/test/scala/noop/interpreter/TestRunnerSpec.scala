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

import inject.Injector
import model._
import org.scalatest.matchers.ShouldMatchers
import types.StringFactory;
import org.scalatest.Spec;

import interpreter.testing.{TestFailedException, TestHolder, TestRunner};

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class TestRunnerSpec extends Spec with ShouldMatchers with GuiceInterpreterFixture {

  val fooClass = new ClassDefinition("Foo", "", "a class");

  describe("the test runner") {

    it("should find unittests within production classes") {
      val unittest = new Method("name", new Block(), "doc");
      unittest.returnTypes += "Void";
      fooClass.unittests += unittest;
      val classLoader = new ClassSearch() {
        def eachClass(f: ClassDefinition => Unit) = {
          f.apply(fooClass);
        }
      }

      val testRunner = new TestRunner(classLoader, null, null, null, null);
      val testsToRun = testRunner.gatherTests();
      testsToRun should have length (1);
      testsToRun.first.testMethod should be theSameInstanceAs(unittest);
      testsToRun.first.classDef should be theSameInstanceAs(fooClass);
    }

    it("should execute a test") {
      val injector = fixture;
      val block = new Block();
      val expression = new MockExpression();

      block.statements += expression;
      val testMethod = new Method("should execute me", block, "doc");
      testMethod.returnTypes += "Void";
      val test = new TestHolder(fooClass, testMethod);

      injector.getInstance(classOf[TestRunner]).runTest(test);
      expression.timesCalled should be(1);
    }
  }
}
