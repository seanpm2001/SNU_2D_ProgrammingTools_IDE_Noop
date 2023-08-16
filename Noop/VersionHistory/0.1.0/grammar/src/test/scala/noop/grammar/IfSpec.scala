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
class IfSpec extends Spec with ShouldMatchers {

  val parser = new Parser();

  describe("an if expression") {

    it("should parse correctly equality expression on two literals") {
      val source = "{ if (1 == 1) { } }";
      val ifBlock = parser.parseBlock(source);

      ifBlock.toStringTree() should be("(IF (== 1 1))");
    }

    it("should parse correctly an inequality expression on two literals") {
      val source = "{ if (1 != 2) { } }";
      val ifBlock = parser.parseBlock(source);

      ifBlock.toStringTree() should be("(IF (!= 1 2))");
    }

    it("should parse correctly an inequality expression followed by an or'ed equality expression on literals") {
      val source = "{ if (1 != 2 || 1 == 1) { } }";
      val ifBlock = parser.parseBlock(source);

      ifBlock.toStringTree() should be("(IF (|| (!= 1 2) (== 1 1)))");
    }

    it("should parse correctly an inequality expression followed by an and'ed equality expression on literals") {
      val source = "{ if (1 != 2 && 1 == 1) { } }";
      val ifBlock = parser.parseBlock(source);

      ifBlock.toStringTree() should be("(IF (&& (!= 1 2) (== 1 1)))");
    }

    it("should parse correctly complex conditional with or and and primaries") {
      val source = "{ if (1 != a.getValue() && \"hello\" != \"wolrd\" || 50 == myVar) { } }";
      val ifBlock = parser.parseBlock(source);

      ifBlock.toStringTree() should be("(IF (|| (&& (!= 1 (. a getValue ARGS)) (!= \"hello\" \"wolrd\")) (== 50 myVar)))");
    }

    it("should parse correctly greater than conditional") {
      val source = "{ if (1 > 3) {} }";
      val ifBlock = parser.parseBlock(source);

      ifBlock.toStringTree() should be("(IF (> 1 3))");
    }

    it("should parse correctly lesser than conditional") {
      val source = "{ if (1 < 3) {} }";
      val ifBlock = parser.parseBlock(source);

      ifBlock.toStringTree() should be("(IF (< 1 3))");
    }
    
    it("should parse correctly greater than or equal conditional") {
      val source = "{ if (1 >= 3) {} }";
      val ifBlock = parser.parseBlock(source);

      ifBlock.toStringTree() should be("(IF (>= 1 3))");
    }

    it("should parse correctly lesser than or equal conditional") {
      val source = "{ if (1 <= 3) {} }";
      val ifBlock = parser.parseBlock(source);

      ifBlock.toStringTree() should be("(IF (<= 1 3))");
    }

    it("should parse regular expression") {
      val source = "{ boolean b = false; if (b = true) {} }";
      val ifBlock = parser.parseBlock(source);

      ifBlock.toStringTree() should be("(= boolean b false) (IF (= b true))");
    }
  }
}
