/*
 * Copyright 2010 Google Inc.
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

package noop.stdlib;

import org.joda.time.Instant;

import java.util.UUID;

import noop.graph.Controller;
import static noop.graph.Edge.EdgeType.TYPEOF;
import noop.model.Clazz;
import noop.model.Comment;
import noop.model.Library;
import noop.model.Method;
import noop.model.Parameter;
import noop.model.Project;
import noop.operations.NewEdgeOperation;
import noop.operations.NewProjectOperation;

/**
 * TODO: when we have a way to share serialized noop code, remove this class
 * @author alexeagle@google.com (Alex Eagle)
 */
public class StandardLibraryBuilder {
  public Clazz intClazz;
  public Clazz consoleClazz;
  public Clazz stringClazz;
  public Clazz voidClazz;
  public Clazz booleanClazz;
  public Method printMethod;
  public Method integerPlus;
  public Method integerEquals;
  public Project noop;
  public Library lang;
  public Library io;

  public StandardLibraryBuilder build(Controller controller) {

    noop = new Project("Noop", "com.google", "Apache 2");

    lang = new Library(UUID.randomUUID(), "lang");
    noop.addLibrary(lang);

    stringClazz = new Clazz("String");
    lang.addClazz(stringClazz);

    voidClazz = new Clazz("Void");
    lang.addClazz(voidClazz);

    io = new Library(UUID.randomUUID(), "io");
    noop.addLibrary(io);

    consoleClazz = new Clazz("Console");
    io.addClazz(consoleClazz);

    printMethod = new Method("print");
    consoleClazz.addBlock(printMethod);

    Parameter printArg = new Parameter("s");
    printMethod.addParameter(printArg);

    booleanClazz = new Clazz("Boolean");
    lang.addClazz(booleanClazz);

    intClazz = new Clazz("Integer");
    lang.addClazz(intClazz);

    integerPlus = new Method("+");
    intClazz.addBlock(integerPlus);
    intClazz.addComment(new Comment("Elements may have symbols in their names." +
        " Tools may choose to render this as infix",
        System.getProperty("user.name"), new Instant()));

    integerEquals = new Method("==");
    intClazz.addBlock(integerEquals);

    Parameter integerPlusArg = new Parameter("i");
    integerPlus.addParameter(integerPlusArg);

    controller.apply(new NewProjectOperation(noop));
    controller.apply(new NewEdgeOperation(printMethod, TYPEOF, voidClazz));
    controller.apply(new NewEdgeOperation(printArg, TYPEOF, stringClazz));
    controller.apply(new NewEdgeOperation(integerPlus, TYPEOF, intClazz));
    controller.apply(new NewEdgeOperation(integerEquals, TYPEOF, booleanClazz));
    controller.apply(new NewEdgeOperation(integerPlusArg, TYPEOF, intClazz));

    return this;
  }
}
