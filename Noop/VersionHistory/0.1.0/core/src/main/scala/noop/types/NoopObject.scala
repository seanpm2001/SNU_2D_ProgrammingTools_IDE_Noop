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
package noop.types;

import collection.mutable.Map;

import noop.model.ClassDefinition;

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class NoopObject(val classDef: ClassDefinition) {
  val propertyMap: Map[String, NoopObject] = Map.empty[String, NoopObject];

  def nativeMethod(name: String): (Seq[NoopObject] => NoopObject) = {
    throw new NoSuchMethodException("Native method implemention for '"
        + name + "' missing in " + classDef.qualifiedName);
  }

  def executeNativeMethod(args: Seq[NoopObject], name: String): NoopObject = {
    return nativeMethod(name).apply(args);
  }

  override def toString = classDef.name;
}
