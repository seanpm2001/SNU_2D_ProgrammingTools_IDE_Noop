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

import noop.model.ClassDefinition;
import noop.interpreter.ClassLoader;

import com.google.inject.assistedinject.FactoryProvider
import com.google.inject.name.Named
import com.google.inject.{Provides, AbstractModule, Singleton}

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class NoopTypesModule extends AbstractModule {
  override def configure() = {
    bind(classOf[BooleanFactory]).toProvider(FactoryProvider.newFactory(classOf[BooleanFactory], classOf[NoopBoolean]));
    bind(classOf[StringFactory]).toProvider(FactoryProvider.newFactory(classOf[StringFactory], classOf[NoopString]));
    bind(classOf[IntegerFactory]).toProvider(FactoryProvider.newFactory(classOf[IntegerFactory], classOf[NoopInteger]));
  }

  @Provides @Singleton @Named("Int")
  def intClassDef(classLoader: ClassLoader): ClassDefinition = classLoader.findClass("noop.Int");

  @Provides @Singleton @Named("String")
  def stringClassDef(classLoader: ClassLoader): ClassDefinition = classLoader.findClass("noop.String");

  @Provides @Singleton @Named("Boolean")
  def booleanClassDef(classLoader: ClassLoader): ClassDefinition = classLoader.findClass("noop.Boolean");

}