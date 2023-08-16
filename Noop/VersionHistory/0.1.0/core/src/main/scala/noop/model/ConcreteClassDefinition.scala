/*
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

import scala.collection.mutable.{ArrayBuffer, Buffer};

/**
 * An AST element representing a concrete class. To the user, this is a "class", but to us,
 * a class may be an interface or a binding as well. This is similar to Java's confusion
 * but it's familiar.
 *
 * @author Alex Eagle (alexeagle@google.com)
 */

class ConcreteClassDefinition(name: String, namespace: String, documentation: String)
    extends ClassDefinition(name, namespace, documentation) {
  val interfaces: Buffer[String] = new ArrayBuffer[String];
  val parameters: Buffer[Parameter] = new ArrayBuffer[Parameter];
}
