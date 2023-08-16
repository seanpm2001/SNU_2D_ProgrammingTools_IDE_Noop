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
class InterfaceSpec extends Spec with ShouldMatchers {

  val parser = new Parser();

  describe("interface") {

    it("should parse an interface with no methods") {
      val source = "interface MyInterface {}";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(INTERFACE MyInterface)");
    }

    it("should parse an interface with one method") {
      val source = "interface MyInterface { Int helloWorld();  }";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(INTERFACE MyInterface (METHOD (RETURN_TYPE Int) helloWorld))");
    }

    it("should not parse an interface with a method having a body") {
      val source = "interface MyInterface { Int helloWorld() { Int i = 0; }  }";

      intercept[ParseException] {
    	  parser.parseFile(source);
      }
    }
  }
}
