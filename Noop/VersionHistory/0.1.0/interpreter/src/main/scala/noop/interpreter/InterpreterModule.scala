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

import noop.model.{Visitor, CompositeVisitor, LoggingAstVisitor};
import noop.inject.{Injector, GuiceBackedInjector};
import noop.grammar.Parser;

import com.google.inject.{Guice, Provides, AbstractModule, Singleton};
import java.io.File;
import scala.collection.mutable;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class InterpreterModule(srcRoots: List[String]) extends AbstractModule {
  override def configure() = {
    bind(classOf[ClassSearch]).to(classOf[SourceFileClassLoader]);
    bind(classOf[ClassLoader]).to(classOf[SourceFileClassLoader]);
  }

  @Provides @Singleton def getClassLoader(parser: Parser) = {
    new SourceFileClassLoader(parser, srcRoots);
  }

  @Provides def getInjector(classLoader: ClassLoader): Injector =
      new GuiceBackedInjector(classLoader, Guice.createInjector());

  @Provides @Singleton def getContext(classLoader: ClassLoader) =
      new Context(new mutable.Stack[Frame], classLoader);

  /**
   * Chain a logging visitor with the one that interprets the code.
   */
  @Provides def getVisitor(context: Context, injector: Injector, interpreterVisitor: InterpreterVisitor): Visitor =
      new CompositeVisitor(List(new LoggingAstVisitor(), interpreterVisitor));
}