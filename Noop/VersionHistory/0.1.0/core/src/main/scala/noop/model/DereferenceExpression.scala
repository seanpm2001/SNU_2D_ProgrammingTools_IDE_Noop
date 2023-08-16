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
package noop.model

/**
 * An AST model element for a dereference, expressed syntactically as a dot.
 * {@code a.b} is a dereference of the b property from the object referenced by a.
 * A method call is not a dereference.
 *
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class DereferenceExpression(val left: Expression, val right: Expression) extends Expression {

  override def accept(visitor: Visitor) = {
    left.accept(visitor);
    right.accept(visitor);
    visitor.visit(this);
  }
}
