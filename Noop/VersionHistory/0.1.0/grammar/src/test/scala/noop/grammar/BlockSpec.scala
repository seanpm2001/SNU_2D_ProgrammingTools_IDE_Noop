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

import collection.mutable.ArrayBuffer
import org.scalatest.matchers.ShouldMatchers
import noop.model._

import org.scalatest.Spec;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class BlockSpec extends Spec with ShouldMatchers {

  val parser = new Parser();

  describe("a block") {

    it("should allow a return statement") {
      val blockAst = parser.parseBlock("{ return 0; }");

      blockAst.toStringTree() should be("(return 0)");
      val block = parser.buildTreeParser(blockAst).block();

      block.statements.size should be(1);
      block.statements(0).getClass() should be(classOf[ReturnExpression]);
      val returnExpression = block.statements(0).asInstanceOf[ReturnExpression];

      returnExpression.expr.getClass() should be(classOf[IntLiteralExpression]);
      returnExpression.expr.asInstanceOf[IntLiteralExpression].value should be(0);
    }

    it("should allow chained property access on properties") {
      val source = "{ b.c.d; }";
      val blockAst = parser.parseBlock(source);

      blockAst.toStringTree() should be("(. (. b c) d)");
      val block = parser.buildTreeParser(blockAst).block();

      block.statements.size should be(1);
      block.statements(0).getClass() should be(classOf[DereferenceExpression]);
      val deref1 = block.statements(0).asInstanceOf[DereferenceExpression];
      deref1.left.getClass() should be(classOf[DereferenceExpression]);
      deref1.right.getClass() should be(classOf[IdentifierExpression]);
      deref1.right.asInstanceOf[IdentifierExpression].identifier should be("d");

      val deref2 = deref1.left.asInstanceOf[DereferenceExpression];
      deref2.left.getClass() should be(classOf[IdentifierExpression]);
      deref2.right.getClass() should be(classOf[IdentifierExpression]);
      deref2.left.asInstanceOf[IdentifierExpression].identifier should be("b");
      deref2.right.asInstanceOf[IdentifierExpression].identifier should be("c");
    }

    it("should allow a method call on implicit 'this'") {
      val source = "{ a(); }";
      val blockAst = parser.parseBlock(source);

      blockAst.toStringTree() should be ("a ARGS");
      val block = parser.buildTreeParser(blockAst).block();
      block.statements.size should be(1);
      block.statements(0).getClass() should be(classOf[MethodInvocationExpression]);
      val methodInvocation = block.statements(0).asInstanceOf[MethodInvocationExpression];
      methodInvocation.left.getClass() should be(classOf[IdentifierExpression]);
      methodInvocation.left.asInstanceOf[IdentifierExpression].identifier should be("this");
      methodInvocation.name should be("a");
      methodInvocation.arguments should be ('empty);
    }

    it("should allow calling a method on an identifier") {
      val source = "{ a.b(); }";
      val blockAst = parser.parseBlock(source);

      blockAst.toStringTree() should be("(. a b ARGS)");
      val block = parser.buildTreeParser(blockAst).block();
      block.statements.size should be(1);

      val methodInvocation = block.statements(0).asInstanceOf[MethodInvocationExpression];
      methodInvocation.left.getClass() should be(classOf[IdentifierExpression]);
      methodInvocation.left.asInstanceOf[IdentifierExpression].identifier should be("a");
      methodInvocation.name should be("b");
      methodInvocation.arguments.isEmpty should be (true);
    }

    it("should allow method chaining") {
      val source = "{ a.b().c(); }";
      val blockAst = parser.parseBlock(source);

      blockAst.toStringTree() should be("(. (. a b ARGS) c ARGS)");
      val block = parser.buildTreeParser(blockAst).block();
      block.statements should have length (1);

      val method1 = block.statements(0).asInstanceOf[MethodInvocationExpression];
      method1.left.getClass() should be(classOf[MethodInvocationExpression]);
      val method2 = method1.left.asInstanceOf[MethodInvocationExpression];

      method2.left.getClass() should be(classOf[IdentifierExpression]);
      method2.left.asInstanceOf[IdentifierExpression].identifier should be("a");
      method2.name should be("b");
      method2.arguments should be ('empty);

      method1.name should be("c");
      method1.arguments should be('empty);
    }

    it("should allow a method call on a property") {
      val source = "{ a.b.c(); }";
      val blockAst = parser.parseBlock(source);

      blockAst.toStringTree() should be("(. (. a b) c ARGS)");
      val block = parser.buildTreeParser(blockAst).block();
      block.statements should have length (1);

      val method = block.statements(0).asInstanceOf[MethodInvocationExpression];
      method.left.getClass() should be(classOf[DereferenceExpression]);
      val deref = method.left.asInstanceOf[DereferenceExpression];

      deref.left.getClass() should be(classOf[IdentifierExpression]);
      deref.left.asInstanceOf[IdentifierExpression].identifier should be("a");
      deref.right.getClass() should be(classOf[IdentifierExpression]);
      deref.right.asInstanceOf[IdentifierExpression].identifier should be("b");

      method.name should be("c");
      method.arguments should be('empty);
    }

    it("should allow a method call with arguments") {
      val source = "{ a.b(c, \"d\"); }";
      val blockAst = parser.parseBlock(source);

      blockAst.toStringTree() should be ("(. a b (ARGS c \"d\"))");

      val block = parser.buildTreeParser(blockAst).block();
      block.statements.size should be(1);

      val methodInvocation = block.statements(0).asInstanceOf[MethodInvocationExpression];
      methodInvocation.left.getClass() should be(classOf[IdentifierExpression]);
      methodInvocation.left.asInstanceOf[IdentifierExpression].identifier should be("a");
      methodInvocation.name should be("b");
      methodInvocation.arguments should have length(2);
      methodInvocation.arguments(0).getClass() should be(classOf[IdentifierExpression]);
      methodInvocation.arguments(0).asInstanceOf[IdentifierExpression].identifier should be("c");
      methodInvocation.arguments(1).getClass() should be(classOf[StringLiteralExpression]);
      methodInvocation.arguments(1).asInstanceOf[StringLiteralExpression].value should be ("d");
    }
  }
}
