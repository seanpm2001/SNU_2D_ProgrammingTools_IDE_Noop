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
package noop.interpreter.testing;

import collection.mutable.{ArrayBuffer, Buffer, Stack}
import com.google.inject.{Inject, Guice}
import inject.{Injector}

import model._
import types.{NoopBoolean, BooleanFactory}
/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class TestRunner @Inject() (classSearch: ClassSearch, classLoader: ClassLoader, injector: Injector,
                            interpreterVisitor: Visitor, context: Context) {

  def gatherTests(): Buffer[TestHolder] = {
    var tests = new ArrayBuffer[TestHolder];
    classSearch.eachClass((c:ClassDefinition) => {
      for (testMethod <- c.unittests) {
        tests += new TestHolder(c, testMethod);
      }
    });
    return tests;
  }

  def runTests() = {
    val startTime = System.currentTimeMillis;
    val tests = gatherTests;
    for (test <- tests) {
      runTest(test);
    }
    val elapsed = (System.currentTimeMillis - startTime) / 1000;
    println("Ran " + tests.size + " test(s) in " + elapsed + " seconds.");
  }

  /**
   * Run a single test
   */
  def runTest(test: TestHolder) = {
    val instance = injector.getInstance(test.classDef);

    context.stack.push(new Frame(instance, test.testMethod));
    try {
      context.stack.top.blockScopes.inScope("test " + test.testMethod.name) {
        interpreterVisitor.visit(test.testMethod);
      }
    } finally {
      context.stack.pop();
    }
  }
}
