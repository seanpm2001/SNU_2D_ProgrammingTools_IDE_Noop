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
package noop.inject


import interpreter.InterpreterModule
import model.ClassDefinition
import com.google.inject.Guice
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import types.{NoopObject, NoopConsole, NoopTypesModule}

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class GuiceBackedInjectorSpec extends Spec with ShouldMatchers {
  def fixture = {
     val guiceInjector = Guice.createInjector(new NoopTypesModule(), new InterpreterModule(List()));
     guiceInjector.getInstance(classOf[Injector]);
  }
  describe("the injector") {
    it("should create a NoopConsole instance") {
      val noopInjector: Injector = fixture;
      val instance = noopInjector.getInstance(new ClassDefinition("Console", "noop", ""));
      instance.getClass() should be(classOf[NoopConsole]);

    }
    
    it("should create an instance of a user-defined type") {
      val noopInjector: Injector = fixture;
      val userClass = new ClassDefinition("A", "", "A class named A");
      val instance = noopInjector.getInstance(userClass);
      instance.getClass() should be(classOf[NoopObject]);
      instance.classDef should be(userClass);
    }
  }
}