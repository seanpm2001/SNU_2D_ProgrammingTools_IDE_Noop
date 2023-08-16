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

import collection.mutable.{Stack, ArrayBuffer}
import com.google.inject.{Inject, Guice}
import noop.inject.{Injector, GuiceBackedInjector}
import noop.model._

/**
 * This class bootstraps the interpretation process, by setting up the ClassLoader with
 * native Scala-implemented Noop types, and starting off the first method invocation.
 *
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class Interpreter @Inject() (classLoader: ClassLoader, injector: Injector, context: Context, visitor: Visitor) {

  def runApplication(bindingDefinition: BindingDefinition): Int = {
    // TODO(alex): Register bindings with the injector, then request an instance of noop.Application
    bindingDefinition.bindings.find((b: BindingDeclaration) => b.noopType == "Application") match {
      case Some(binding: BindingDeclaration) => {
        val applicationType = binding.boundTo.asInstanceOf[IdentifierExpression].identifier;
        val applicationClass = classLoader.findClass(bindingDefinition.resolveType(applicationType));
        applicationClass match {
          case concrete: ConcreteClassDefinition => runFromConcrete(concrete);
          case _ => throw new IllegalStateException("Application must be bound to concrete class");
        }
      }
      case None => throw new IllegalStateException("No binding for Application was found");
    }
  }

  def runFromConcrete(mainClass: ConcreteClassDefinition): Int = {
    val mainInstance = injector.getInstance(mainClass);

    context.addRootFrame(mainInstance);
    new MethodInvocationExpression(new EvaluatedExpression(mainInstance), "main", List()).accept(visitor);
    // TODO(alex): return the value from the main
    return 0;
  }
}
