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
package noop.grammar;

import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class FileSpec extends Spec with ShouldMatchers {

  val parser = new Parser();

  describe("parser") {

    it("should allow the word 'test' as a package name") {
      // TODO(alexeagle)
    }

    it("should parse namespace declaration") {
      val source = "namespace noop.demo; class Foo() {}";
      parser.parseFile(source).toStringTree() should equal (
        "(namespace noop demo) (CLASS Foo)");
    }

    it("should allow import in default namespace") {
      val source = "import Foo; class B(){}";
      parser.parseFile(source).toStringTree() should equal (
        "(import Foo) (CLASS B)");
    }

    it("should parse import statements") {
      val source = "import noop.demo.Test; class Foo() {}";
      parser.parseFile(source).toStringTree() should equal (
        "(import noop demo Test) (CLASS Foo)");
    }

    it("should not allow import without a type on the end") {
      val source = "import noop.demo;";
      intercept[ParseException] {
        parser.parseFile(source);
      }
    }

    it("should parse a realistic looking file header") {
      val source = """
        namespace noop.grammar;
        // TODO: it should fail when a semicolon is missing, but the test still passes
        import org.antlr.runtime.RecognitionException;
        import org.scalatest.Spec;
        import org.scalatest.matchers.ShouldMatchers;

        class FileSpec() {
        }
      """;
      parser.parseFile(source).toStringTree() should equal (
        "(namespace noop grammar) (import org antlr runtime RecognitionException) " +
        "(import org scalatest Spec) (import org scalatest matchers ShouldMatchers) " +
        "(CLASS FileSpec)");
      val file = parser.file(source);
      file.namespace should be ("noop.grammar");
      file.imports(0) should be ("org.antlr.runtime.RecognitionException");
      file.imports(1) should be ("org.scalatest.Spec");
      file.imports(2) should be ("org.scalatest.matchers.ShouldMatchers");
    }

    it ("should not allow extra stuff after the class definition") {
      val source = "class Foo() {} extra stuff";
      //intercept[ParseException] {
      //  parser.parseFile(source);
      //}
    }
  }
}
