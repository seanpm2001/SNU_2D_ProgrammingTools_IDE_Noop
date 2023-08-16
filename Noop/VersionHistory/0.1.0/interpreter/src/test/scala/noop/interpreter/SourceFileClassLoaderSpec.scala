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

import java.io.{PrintWriter, BufferedWriter, FileWriter, File};

import collection.mutable.ArrayBuffer;

import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

import noop.grammar.{ParseException, Parser};
import noop.model.ClassDefinition;

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class SourceFileClassLoaderSpec extends Spec with ShouldMatchers {

  val tmpDir = new File(System.getProperty("java.io.tmpdir"));

  describe("classloader") {

    it("should throw an exception if given a non-existent directory") {
      val srcPaths = List("doesNotExist");
      val classLoader = new SourceFileClassLoader(new Parser(), srcPaths);
      intercept[RuntimeException] {
        classLoader.findClass("Foo");
      }
    }

    it("should load a class from a source path") {
      val source = new File(tmpDir, "MyClass.noop");
      source.deleteOnExit();
      val printWriter = new PrintWriter(new FileWriter(source))
      printWriter.println("class MyClass() {}");
      printWriter.close();

      val srcPaths = List(tmpDir.getAbsolutePath());
      val classLoader = new SourceFileClassLoader(new Parser(), srcPaths);
      val classDef = classLoader.findClass("MyClass")

      classDef.name should equal("MyClass")
    }

    it("should load a class in a namespace") {
      new File(tmpDir, "noop").mkdir();
      val source = new File(new File(tmpDir, "noop"), "Foo.noop");
      source.deleteOnExit();
      val printWriter = new PrintWriter(new FileWriter(source))
      printWriter.println("class Foo() {}");
      printWriter.close();

      val srcPaths = List(tmpDir.getAbsolutePath());
      val classLoader = new SourceFileClassLoader(new Parser(), srcPaths);
      val classDef = classLoader.findClass("noop.Foo")

      classDef.name should equal("Foo")
      classDef.namespace should equal("noop")
    }

    it("should load a class in a nested namespace") {
      val dir = new File(new File(tmpDir, "noop"), "package");
      dir.mkdirs();
      val source = new File(dir, "Foo.noop");
      source.deleteOnExit();
      val printWriter = new PrintWriter(new FileWriter(source))
      printWriter.println("class Foo() {}");
      printWriter.close();

      val srcPaths = List(tmpDir.getAbsolutePath());
      val classLoader = new SourceFileClassLoader(new Parser(), srcPaths);
      val classDef = classLoader.findClass("noop.package.Foo")

      classDef.name should equal("Foo")
      classDef.namespace should equal("noop.package")
    }

    it("should throw ClassNotFound if the class doesn't exist") {
      val srcPaths = List(tmpDir.getAbsolutePath());
      val classLoader = new SourceFileClassLoader(new Parser(), srcPaths);
      intercept[ClassNotFoundException] {
        classLoader.findClass("Foo");
      }
    }

    // Actually, it might be annoying that changes to the source aren't seen?
    // it("should not parse the source file more than once") {
    // TODO(jeremie)
    // }

    it("should find all classes in the srcPaths when asked to search") {
      val srcPath = new File(tmpDir, "search");
      val dir = new File(srcPath, "package");
      dir.mkdirs();
      val source = new File(dir, "Foo.noop");
      source.deleteOnExit();
      val printWriter = new PrintWriter(new FileWriter(source))
      printWriter.println("class Foo() {}");
      printWriter.close();

      val classLoader = new SourceFileClassLoader(new Parser(), List(srcPath.getAbsolutePath));

      val classesFound = new ArrayBuffer[ClassDefinition];
      classLoader.eachClass((c:ClassDefinition) => classesFound += c);
      classesFound should have length(1);
      classesFound(0).name should be("Foo");
      classesFound(0).qualifiedName should be("package.Foo");
    }

    it("should locate standard libraries in the classpath") {
      val classLoader = new SourceFileClassLoader(new Parser(), List());
      val classDef = classLoader.findClass("noop.Object");

      classDef.name should equal("Object");
      classDef.namespace should equal("noop");
    }

    it("should not overwrite an explicit namespace in the file with the relative path") {
      new File(tmpDir, "noop").mkdir();
      val source = new File(new File(tmpDir, "noop"), "Foo.noop");
      source.deleteOnExit();
      val printWriter = new PrintWriter(new FileWriter(source))
      printWriter.println("namespace namespace1; class Foo() {}");
      printWriter.close();

      val srcPaths = List(tmpDir.getAbsolutePath());
      val classLoader = new SourceFileClassLoader(new Parser(), srcPaths);
      val classDef = classLoader.findClass("noop.Foo")

      classDef.name should equal("Foo")
      classDef.namespace should equal("namespace1")
    }
  }
}
