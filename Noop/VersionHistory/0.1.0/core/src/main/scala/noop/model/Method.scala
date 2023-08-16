/**
 *  Copyright 2009 Google Inc.
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
package noop.model;

import collection.mutable.{ArrayBuffer, Buffer};

/**
 * Represents the declaration of a method in source code.
 *
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class Method(val name: String, val block: Block, val documentation: String)  {

  val returnTypes: Buffer[String] = new ArrayBuffer[String]();
  val parameters: Buffer[Parameter] = new ArrayBuffer[Parameter]();
  val modifiers: Buffer[Modifier.Value] = new ArrayBuffer[Modifier.Value]();

  override def toString() = String.format("Method %s (%s) returns %s", name, parameters, returnTypes);
}
