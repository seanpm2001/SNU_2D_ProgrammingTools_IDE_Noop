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

import com.google.inject.Guice
import java.net.{URL, URLClassLoader}

import java.io.File;
import noop.model.BindingDefinition;
import noop.types.NoopTypesModule;

import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

import noop.grammar.Parser;

/**
 * This test runs all the example noop programs found under /examples.
 *
 * @author alexeagle@google.com (Alex Eagle)
 */
class ExampleIntegrationTest extends Spec with ShouldMatchers with ConsoleTestFixture {

  def createFixture = {
    val injector = Guice.createInjector(new InterpreterModule(List()), new NoopTypesModule());
    val classLoader = new SourceFileClassLoader(new Parser(), List());
    (classLoader, injector.getInstance(classOf[Interpreter]));
  }

  it("should run the hello world program") {
    withRedirectedStandardOut { (output) => {
      val (classLoader, interpreter) = createFixture;
      val mainClass = classLoader.findClass("helloworld.HelloWorldBinding").asInstanceOf[BindingDefinition];
      mainClass should not be(null);

      interpreter.runApplication(mainClass);
      output.toString() should include("Hello World!");
    }}
  }

  it("should run while loop") {
    withRedirectedStandardOut { (output) => {
      val (classLoader, interpreter) = createFixture;
      val mainClass = classLoader.findClass("controlFlow.WhileLoopBinding").asInstanceOf[BindingDefinition];
      interpreter.runApplication(mainClass);
      output.toString() should equal("Hello World!\n");
    }}
  }

  it("should run the arithmetic program") {
    withRedirectedStandardOut { (output) => {
      val (classLoader, interpreter) = createFixture;
      val mainClass = classLoader.findClass("arithmetic.ArithmeticBinding").asInstanceOf[BindingDefinition];
      interpreter.runApplication(mainClass);
      output.toString() should include("3");
    }}
  }

  it("should pass command line arguments to the commandLine program") {
    withRedirectedStandardOut { (output) =>
      val (classLoader, interpreter) = createFixture;
      val mainClass = classLoader.findClass("commandLine.CommandLineExample").asInstanceOf[BindingDefinition];
      interpreter.runApplication(mainClass);
      // output.toString() should include("argument1");
    }
  }
}
