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
package noop.interpreter

import collection.mutable.{Stack, Map}
import types.{NoopType, NoopObject}

import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class BlockScopesSpec extends Spec with ShouldMatchers {
  val foo = new Tuple2[NoopType, NoopObject](null, null);
  def fixture = {
    val storage = new Stack[Map[String, Tuple2[NoopType, NoopObject]]]();
    val scopes = new BlockScopes(storage)
    (storage, scopes);
  }

  describe("the stack of block scopes") {

    it("should register a new identifier at the current scope") {
      val (storage, scopes) = fixture;
      scopes.inScope("if") {
        scopes.registerIdentifier("s", foo);
        storage.top.contains("s") should be(true);
      }
    }

    it("should throw a RuntimeException if an identifier with that name has been declared") {
      val (storage, scopes) = fixture;
      scopes.inScope("while") {
        scopes.registerIdentifier("s", foo);
        intercept[RuntimeException] {
          scopes.registerIdentifier("s", foo);
        };
      }
    }

    it("should find an identifier on the top scope") {
      val (storage, scopes) = fixture;
      scopes.inScope("if") {
        scopes.registerIdentifier("s", foo);
        scopes.getIdentifier("s") should be theSameInstanceAs(foo);
      }
    }

    it("should find an identifier earlier in the stack") {
      val (storage, scopes) = fixture;
      scopes.inScope("if") {
        scopes.registerIdentifier("s", foo);
        scopes.inScope("while") {
          scopes.getIdentifier("s") should be theSameInstanceAs(foo);
        }
      }
    }

    it("should return false if it doesn't find the identifier in the stack") {
      val (storage, scopes) = fixture;
      scopes.inScope("for") {
        scopes.registerIdentifier("b", foo);
      }
      scopes.hasIdentifier("b") should be (false);
    }

    it("should allow re-assignment") {
      val (storage, scopes) = fixture;
      scopes.inScope("if") {
        scopes.registerIdentifier("s", null);
        scopes.inScope("while") {
          scopes.setValue("s", foo);
          scopes.getIdentifier("s") should be theSameInstanceAs(foo);
        }
      }
    }

    it("should throw a RuntimeException if an unregistered identifier is assigned to") {
      val (storage, scopes) = fixture;
      scopes.inScope("if") {
        intercept[RuntimeException] {
          scopes.setValue("s", foo);
        };
      }
    }
  }
}