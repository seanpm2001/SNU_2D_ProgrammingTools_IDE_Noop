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
class PropertiesSpec extends Spec with ShouldMatchers {

  val parser = new Parser();

  describe("parser") {

    it("should parse a single property declaration") {
      val source = """
        class Foo() {
          Int a = 4;
          Int i;
        }
      """;

      parser.parseFile(source).toStringTree() should equal ("(CLASS Foo (VAR Int (= a 4)) (VAR Int i))");
    }
  }
}
