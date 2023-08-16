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
package noop.model;

import noop.types.NoopObject
import org.slf4j.LoggerFactory;

import scala.collection.mutable.{ArrayBuffer, Buffer};

/**
 * namespace is mutable because we may infer the namespace from the relative path of the file
 *
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class ClassDefinition (val name: String, var namespace: String, val documentation: String) {
  val methods: Buffer[Method] = new ArrayBuffer[Method];
  val unittests: Buffer[Method] = new ArrayBuffer[Method];
  val modifiers: Buffer[Modifier.Value] = new ArrayBuffer[Modifier.Value];
  val imports: Buffer[String] = new ArrayBuffer[String];

  def resolveType(noopType: String): String = {
    imports.find((imp: String) => imp.split("\\.").last == noopType) match {
      case Some(qualifiedType) => return qualifiedType;
      case None => return noopType;
    }
  }

  def findMethod(methodName: String): Method = {
    methods.find(method => method.name == methodName) match {
      case Some(method) => return method;
      case None => throw new NoSuchMethodException(
          "Method " + methodName + " is not defined on class " + name);
    }
  }

  def qualifiedName = if (namespace == "") name else namespace + "." + name;
}
