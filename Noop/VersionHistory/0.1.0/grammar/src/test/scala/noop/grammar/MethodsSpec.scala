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

import model.{IdentifierDeclarationExpression, Modifier};

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class MethodsSpec extends Spec with ShouldMatchers {

  val parser = new Parser();

  describe("the parser") {

    it("should parse a method with no parameters") {
      val source = "class Bar() { String helloWorld() { Int i = 1; } }";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Bar (METHOD (RETURN_TYPE String) helloWorld " +
          "(VAR Int (= i 1))))");
    }

    it("should parse a method with parameters") {
      val source = "class Bar() { String helloWorld(String s, Int n) { Int i = 1; } }";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Bar (METHOD (RETURN_TYPE String) helloWorld " +
          "(PARAMS (PARAM String s) (PARAM Int n)) (VAR Int (= i 1))))");

      val file = parser.file(source);
      file.classDef.methods.size should be (1);
      val firstMethod = file.classDef.methods(0)
      firstMethod.name should be ("helloWorld");
      firstMethod.returnTypes.size should be (1);
      firstMethod.returnTypes(0) should be ("String");
      firstMethod.parameters.size should be (2);
      firstMethod.parameters(0).name should be ("s");
      firstMethod.parameters(0).noopType should be ("String");
      firstMethod.parameters(1).name should be ("n");
      firstMethod.parameters(1).noopType should be ("Int");
      firstMethod.block.statements.size should be (1);
      firstMethod.block.statements(0).getClass() should be (classOf[IdentifierDeclarationExpression]);
      val firstStatement = firstMethod.block.statements(0).asInstanceOf[IdentifierDeclarationExpression];
      firstStatement.noopType should be ("Int");
      firstStatement.name should be ("i");
      // firstStatement.initialValue should be (Some(new IntLiteralExpression(1)));
    }

    it("should allow variable declaration without an initial value") {
      val source = "class Bar() { String helloWorld() { Int i; } }";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Bar (METHOD (RETURN_TYPE String) helloWorld " +
          "(VAR Int i)))");

      val file = parser.file(source);
      file.classDef.methods.size should be (1);
      val firstMethod = file.classDef.methods(0)
      val firstStatement = firstMethod.block.statements(0).asInstanceOf[IdentifierDeclarationExpression];

      firstStatement.noopType should be ("Int");
      firstStatement.name should be ("i");
      firstStatement.initialValue should be (None);
    }

    it("should parse a method invocation on a parameter reference") {
      val source = """class HelloWorld() { Int hello() { console.println("Hello, World!"); }}""";
      parser.parseFile(source).toStringTree() should equal(
          """(CLASS HelloWorld (METHOD (RETURN_TYPE Int) hello (. console println (ARGS "Hello, World!"))))""");
    }

    it("should parse a method with a return statement") {
      val source = "class Foo() { Void do() { return 4; } }";
      parser.parseFile(source).toStringTree() should equal (
          "(CLASS Foo (METHOD (RETURN_TYPE Void) do (return 4)))");
    }

    it("should allow the native modifier on a method") {
      val source = "class Foo() { native Void do() { Int i; }}";
      parser.parseFile(source).toStringTree() should equal (
          "(CLASS Foo (METHOD (MOD native) (RETURN_TYPE Void) do (VAR Int i)))");
      val file = parser.file(source);
      file.classDef.methods(0).modifiers should contain(Modifier.native);
    }

    it("should parse a method with one return type (no parentheses)") {
      val source = "class Math() { Int calculate() {} }";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal (
          "(CLASS Math (METHOD (RETURN_TYPE Int) calculate))");

      val file = parser.file(source);
      file.classDef.methods.size should be (1);
      val firstMethod = file.classDef.methods(0);
      firstMethod.name should be ("calculate");
      firstMethod.returnTypes.size should be (1);
      firstMethod.returnTypes(0) should be ("Int");
      firstMethod.parameters.size should be (0);
      firstMethod.block.statements.size should be (0);
    }

    it("should parse a method with one return type (parentheses)") {
      val source = "class Math() { (Int) calculate() {} }";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal (
          "(CLASS Math (METHOD (RETURN_TYPE Int) calculate))");

      val file = parser.file(source);
      file.classDef.methods.size should be (1);
      val firstMethod = file.classDef.methods(0);
      firstMethod.name should be ("calculate");
      firstMethod.returnTypes.size should be (1);
      firstMethod.returnTypes(0) should be ("Int");
      firstMethod.parameters.size should be (0);
      firstMethod.block.statements.size should be (0);
    }

    it("should parse a method with multiple return types") {
      val source = "class Math() { (Int, Int) calculate() {} }";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal (
          "(CLASS Math (METHOD (RETURN_TYPE Int Int) calculate))");

      val file = parser.file(source);
      file.classDef.methods.size should be (1);
      val firstMethod = file.classDef.methods(0);
      firstMethod.name should be ("calculate");
      firstMethod.returnTypes.size should be (2);
      firstMethod.returnTypes(0) should be ("Int");
      firstMethod.returnTypes(1) should be ("Int");
      firstMethod.parameters.size should be (0);
      firstMethod.block.statements.size should be (0);
    }

    it ("should NOT parse a method with one named return parameter (no parentheses)") {
      val source = "class Math() { Int a calculate() {} }";
      intercept[ParseException] {
        parser.parseFile(source);
      }
    }

    it ("should NOT parse a method with one named return parameter (parentheses)") {
      val source = "class Math() { (Int a) calculate() {} }";
      intercept[ParseException] {
        parser.parseFile(source);
      }
    }

    it ("should NOT parse a method with a mixture of named and unnamed return parameters") {
      val source = "class Math() { (Int, Int b, Int) calculate() {} }";
      intercept[ParseException] {
        parser.parseFile(source);
      }
    }

    it ("should NOT parse a method with multiple named return parameters") {
      val source = "class Math() { (Int a, Int b) calcualte() {} }";
      intercept[ParseException] {
        parser.parseFile(source);
      }
    }
  }
}
