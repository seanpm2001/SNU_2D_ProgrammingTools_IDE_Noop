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
package noop.interpreter

import com.google.inject.util.Modules
import java.io.File;
import com.google.inject.{Injector, Guice};
import types.{StringFactory, NoopTypesModule};

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
trait GuiceInterpreterFixture {
  def fixture: Injector = {
    val injector = Guice.createInjector(
      new NoopTypesModule(),
      Modules.`override`(new InterpreterModule(List())).
              `with`(new InterpreterTestingModule()));
    val context = injector.getInstance(classOf[Context]);
    val stringFactory = injector.getInstance(classOf[StringFactory]);
    context.addRootFrame(stringFactory.create("aClazz"));
    return injector;
  }
}