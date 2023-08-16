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
 */
class DocumentationSpec extends Spec with ShouldMatchers {

  val parser = new Parser();

  describe("documentation") {

    it("should appear before a class definition, in a string literal") {
      val source = "\"This class is awesome\" class Awesome() {}";
      parser.parseFile(source).toStringTree() should equal (
          "(CLASS Awesome (DOC \"This class is awesome\"))");
      val file = parser.file(source);
      file.classDef.documentation should equal("This class is awesome");
    }

    it("should appear before a method declaration") {
      val source = "class Foo() { \"Here is my great method\" Int doIt() {}}";
      parser.parseFile(source).toStringTree() should equal (
          "(CLASS Foo (METHOD (DOC \"Here is my great method\") (RETURN_TYPE Int) doIt))");
      val file = parser.file(source);
      file.classDef.methods(0).documentation should equal ("Here is my great method");
    }

    it("can use the triple-quoted string literal and appear on an interface") {
      val source = "\"\"\"\nMulti-line\ndocumentation\n\"\"\" interface Bar {}";
      parser.parseFile(source).toStringTree() should equal (
          "(INTERFACE Bar (DOC \"\"\"\nMulti-line\ndocumentation\n\"\"\"))");
      val file = parser.file(source);
      file.classDef.documentation should equal ("\nMulti-line\ndocumentation\n");
    }

    it("should appear before a bindings definition") {
      val source = "\"Stuff you need bound\" binding Stuff {}";
      parser.parseFile(source).toStringTree() should equal (
          "(BINDING Stuff (DOC \"Stuff you need bound\"))");
      val file = parser.file(source);
      file.classDef.documentation should equal("Stuff you need bound");
    }
  }
}
