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
package noop.grammar

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

class TryCatchSpec extends Spec with ShouldMatchers {
  val parser = new Parser();
  
  describe("the parser") {
    it("should parse a try-catch block") {
      val source = "{ try { parseInt(\"1\"); } catch { println(\"oops\"); } }";
      parser.parseBlock(source).toStringTree() should equal (
          "(TRY parseInt (ARGS \"1\") CATCH println (ARGS \"oops\"))");
    }

    it("should parse a try-finally block") {
      val source = "{ try { parseInt(\"1\"); } finally { cleanup(); } }";
      parser.parseBlock(source).toStringTree() should equal (
          "(TRY parseInt (ARGS \"1\") FINALLY cleanup ARGS)");
    }

    it("should parse a try-catch-finally block") {
      val source = "{ try { parseInt(\"1\"); } catch { println(\"oops\"); } finally { cleanup(); } }";
      parser.parseBlock(source).toStringTree() should equal (
          "(TRY parseInt (ARGS \"1\") CATCH println (ARGS \"oops\") FINALLY cleanup ARGS)");
    }
  }

}