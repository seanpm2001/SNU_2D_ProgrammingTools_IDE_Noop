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
class InterpretableSpec extends Spec with ShouldMatchers {

  val parser = new Parser();

  describe("the parser") {

    it("should parse an import as interpretable") {
      val source = "import noop.Foo;";
      parser.parseInterpretable(source).toStringTree() should equal (
        "(import noop Foo)");
    }

    it("should parse a class definition as interpretable") {
      val source = "class F() {}";
      parser.parseInterpretable(source).toStringTree() should equal (
        "(CLASS F)");
    }

    it("should parse a variable declaratian as interpretable") {
      val source = "Int a = 3;";
      parser.parseInterpretable(source).toStringTree() should equal (
        "(VAR Int (= a 3))");
    }

    it("should parse several statements at once") {
      val source = """Int a = 3; import foo.Foo; String b = "poop"; class F() {}""";
      parser.parseInterpretable(source).toStringTree() should equal (
        """(VAR Int (= a 3)) (import foo Foo) (VAR String (= b "poop")) (CLASS F)""");
    }
  }
}
