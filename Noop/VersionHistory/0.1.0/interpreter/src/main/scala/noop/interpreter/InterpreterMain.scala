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
import model.BindingDefinition;
import noop.types.NoopTypesModule;

/**
 * The static entry point into the Noop interpreter, for use from the command-line.
 *
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
object InterpreterMain {

  // The problem with statics: not testable!!
  var testing: Boolean = false;
  var exitCodeForTesting: Int = 0;
  def disableSystemExitForTesting = {
    testing = true;
  }

  def main(args: Array[String]) = {
    //TODO: a proper command line parser, like scalax.io.CommandLineParser
    if (args.size < 1) {
      println("Usage: InterpreterMain main-class [source roots ...]");
      exit(1);
    } else {
      val sourcePaths = args.toList.tail + System.getProperty("user.dir");
      val injector = Guice.createInjector(new InterpreterModule(sourcePaths), new NoopTypesModule());
      val mainClass = injector.getInstance(classOf[ClassLoader]).findClass(args(0));
      mainClass match {
        case binding: BindingDefinition => {
          val returnVal = injector.getInstance(classOf[Interpreter]).runApplication(binding);
          exit(returnVal);
        }
        case _ => throw new IllegalArgumentException("Can only run a binding");
      }
    }
  }

  def exit(exitCode: Int) {
    if (testing) {
      exitCodeForTesting = exitCode;
    } else {
      System.exit(exitCode);
    }
  }
}
