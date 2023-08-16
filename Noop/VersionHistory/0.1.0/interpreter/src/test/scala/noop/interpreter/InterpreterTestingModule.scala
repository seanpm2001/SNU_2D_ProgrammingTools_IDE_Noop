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

import model.{Method, Modifier, ClassDefinition};
import com.google.inject.{Provides, AbstractModule, Singleton};

/**
 * This module sets up a classloader which doesn't rely on reading real .noop files to get the
 * standard library classes.
 *
 * @author alexeagle@google.com (Alex Eagle)
 */
class InterpreterTestingModule extends AbstractModule {
  override def configure() = {
  }

  @Provides @Singleton def mockClassLoader(): ClassLoader = {
    val classLoader = new MockClassLoader();
    val classDefinition = new ClassDefinition("String", "noop", "");
    val length = new Method("length", null, "");
    length.returnTypes += "Int";
    length.modifiers += Modifier.native;
    classDefinition.methods += length;

    classLoader.classes += Pair("noop.String", classDefinition);
    classLoader.classes += Pair("noop.Int", new ClassDefinition("Int", "noop", ""));
    classLoader.classes += Pair("noop.Boolean", new ClassDefinition("Boolean", "noop", ""));
    return classLoader;
  }
}
