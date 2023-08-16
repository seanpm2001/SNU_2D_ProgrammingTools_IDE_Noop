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

import org.scalatest.matchers.ShouldMatchers
import noop.model.{BooleanLiteralExpression, IdentifierDeclarationExpression};
import org.scalatest.Spec;

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class LiteralsSpec extends Spec with ShouldMatchers {

  val parser = new Parser();

  describe("the parser") {

    it("should parse integer literals") {
      val source = "{ Int a = 123; Int b = -123; } ";
      parser.parseBlock(source).toStringTree() should equal (
          "(VAR Int (= a 123)) (VAR Int (= b -123))");
    }

    it("should parse a string literal") {
      val source = " { a = \"hello, world!\"; } ";
      parser.parseBlock(source).toStringTree() should equal (
          "(= a \"hello, world!\")");
    }

    it("should parse a multi-line string literal") {
      val source = "{ String a = \"\"\"Line1\n\"Line2\"\n\"\"\"; }";
      parser.parseBlock(source).toStringTree() should equal(
          "(VAR String (= a \"\"\"Line1\n\"Line2\"\n\"\"\"))");
    }

    it("should not allow a single-double-quoted string to span lines") {
      val source = """{ a = "Line 1
      "; }""";
      intercept[ParseException] (parser.parseBlock(source));
    }

    it("should parse boolean literals") {
      val source = "{ Boolean a = true; }";
      parser.parseBlock(source).toStringTree() should equal ("(VAR Boolean (= a true))");
      val statement: IdentifierDeclarationExpression =
          parser.buildTreeParser(parser.parseBlock(source)).block().statements(0).asInstanceOf[IdentifierDeclarationExpression];
      statement.initialValue should be('defined);
      statement.initialValue match {
        case Some(bool) => {
          bool match {
            case e: BooleanLiteralExpression => e.value should be (true);
            case _ => fail("Expression has wrong type");
          }
        }
        case None => fail("Should have initial value");
      }
    }
  }
}
