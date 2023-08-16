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

/**
 * An implementation of our Noop fixture which uses Guice to store the bindings and create objects.
 * @author alexeagle@google.com (Alex Eagle)
 */


import model.{ConcreteClassDefinition, ClassDefinition}
import noop.interpreter.ClassLoader;
import noop.types.{NoopConsole, NoopObject};

import com.google.inject.AbstractModule;
import scala.collection.mutable;

class GuiceBackedInjector(classLoader: ClassLoader, injector: com.google.inject.Injector) extends Injector {
  // A pointer to the youngest child injector
  var currentInjector: com.google.inject.Injector = injector;

  def getInstance(classDef: ClassDefinition): NoopObject = {
    val obj = classDef.qualifiedName match {
      case "noop.Console" => new NoopConsole(classLoader.findClass("noop.Console"));
      case _ => new NoopObject(classDef);
    }

    if (classDef.isInstanceOf[ConcreteClassDefinition]) {
      val concreteClass = classDef.asInstanceOf[ConcreteClassDefinition];
      for (param <- concreteClass.parameters) {
        val paramClassDef = classLoader.findClass(classDef.resolveType(param.noopType));
        obj.propertyMap += Pair(param.name, getInstance(paramClassDef));
      }
    }
    return obj;
  }

  def withBindings(bindings: Map[String, NoopObject])(f: => Any): Unit = {
    val module = new AbstractModule() {
      def configure() = {
        // TODO: add the bindings into this module
      }
    };
    currentInjector = injector.createChildInjector(module);
    f;
    currentInjector = currentInjector.getParent();
  }
}