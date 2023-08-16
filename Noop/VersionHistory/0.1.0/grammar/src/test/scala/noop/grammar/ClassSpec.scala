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
import noop.model.{ConcreteClassDefinition, Modifier};
import org.scalatest.Spec;



/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class ClassSpec extends Spec with ShouldMatchers {

  val parser = new Parser();

  describe("parser") {

    it("should fail to parse a class with no parenthesis") {
      val source = "class Bar {}";
      intercept[ParseException] {
        parser.parseFile(source);
      }
    }

    it("should parse a class with no parameters") {
      val source = "class Bar() {}";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Bar)");
    }

    it("should parse a class with one parameter") {
      val source = "class Bar(String a) {}";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Bar (PARAMS (PARAM String a)))");

      val file = parser.file(source);
      file.classDef.name should be ("Bar");
      val concreteClass = file.classDef.asInstanceOf[ConcreteClassDefinition];
      concreteClass.parameters(0).name should be ("a");
      concreteClass.parameters(0).noopType should be ("String");
    }

    it("should parse a class with a fully-qualified type in a parameter") {
      val source = "class Bar(noop.String a) {}";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Bar (PARAMS (PARAM noop String a)))");

      val file = parser.file(source);
      val concreteClass = file.classDef.asInstanceOf[ConcreteClassDefinition];
      concreteClass.parameters(0).noopType should be ("noop.String");
    }

    it("should parse a class with multiple parameters") {
      val source = "class Bar(A a, B b, C c) {}";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal (
          "(CLASS Bar (PARAMS (PARAM A a) (PARAM B b) (PARAM C c)))");
    }

    it("should allow modifiers on the parameters") {
      val source = "class Bar(mutable A a, delegate B b, mutable delegate C c) {}";
      parser.parseFile(source).toStringTree() should equal (
          "(CLASS Bar (PARAMS (PARAM (MOD mutable) A a) (PARAM (MOD delegate) B b) " +
          "(PARAM (MOD mutable delegate) C c)))");
      val file = parser.file(source);
      file.classDef.name should be("Bar");
      val parameters = file.classDef.asInstanceOf[ConcreteClassDefinition].parameters;
      parameters should have length(3);
      parameters(0).modifiers should have length(1);
      parameters(0).modifiers should contain(Modifier.mutable);
      parameters(1).modifiers should have length(1);
      parameters(1).modifiers should contain(Modifier.delegate);
      parameters(2).modifiers should have length(2);
      parameters(2).modifiers should contain(Modifier.mutable);
      parameters(2).modifiers should contain(Modifier.delegate);
    }

    it("should allow an implements clause with one interface") {
      val source = "class Foo() implements Bar {}";

      parser.parseFile(source).toStringTree() should equal (
          "(CLASS Foo (IMPL Bar))");
    }

    it("should allow an implements clause with several interfaces") {
      val source = "class Foo() implements A, a.b.C, d.E {}";
      parser.parseFile(source).toStringTree() should equal (
          "(CLASS Foo (IMPL A) (IMPL a b C) (IMPL d E))");
      val file = parser.file(source);
      file.classDef.name should be ("Foo");
      val concreteDef = file.classDef.asInstanceOf[ConcreteClassDefinition];
      concreteDef.interfaces(0) should be("A");
      concreteDef.interfaces(1) should be ("a.b.C");
      concreteDef.interfaces(2) should be ("d.E");
    }

    it("should allow the native modifier on a class") {
      val source = "native class Foo() {}";
      parser.parseFile(source).toStringTree() should equal (
          "(CLASS (MOD native) Foo)");
      val file = parser.file(source);
      file.classDef.modifiers should contain(Modifier.native);
    }
  }
}
