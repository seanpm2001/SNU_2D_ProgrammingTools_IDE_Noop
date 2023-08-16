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
import noop.model.{OperatorExpression, IntLiteralExpression, IdentifierDeclarationExpression};
import org.scalatest.Spec;
import noop.model._

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class ExpressionsSpec extends Spec with ShouldMatchers {

  val parser = new Parser();

  describe("the parser") {

    it("should parse a variable assignment") {
      val source = "{ Int a = 3; }";
      parser.parseBlock(source).toStringTree() should equal ("(VAR Int (= a 3))");
    }

    it("should parse arithmetic") {
      val source = "{ Float a = 3 + 4 / (5 * (6 + 7)) % 8; }";
      parser.parseBlock(source).toStringTree() should equal (
          "(VAR Float (= a (+ 3 (% (/ 4 (* 5 (+ 6 7))) 8))))");
      val block = parser.buildTreeParser(parser.parseBlock(source)).block();
      block.statements should have length(1);
      block.statements(0).getClass should be(classOf[IdentifierDeclarationExpression]);
      val declaration = block.statements(0).asInstanceOf[IdentifierDeclarationExpression];
      declaration.initialValue should be ('defined);
      declaration.initialValue match {
        case Some(expression1: OperatorExpression) => {
          expression1.operator should be ("+");
          expression1.left.asInstanceOf[IntLiteralExpression].value should be (3);
          val expression2 = expression1.asInstanceOf[OperatorExpression].right;
          expression2.asInstanceOf[OperatorExpression].operator should be ("%");
        }
        case _ => fail();
      }
    }
  }
}
