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
class NoopConsole(classDef: ClassDefinition)
    extends NoopObject(classDef) {

  def println(args: Seq[NoopObject]): NoopObject = {
    val toPrint = args(0) match {
      case s: NoopString => s.value
      case i: NoopInteger => i.value.toString
      case _ => "Can't print " + args(0).getClass().getName()
    }
    Console.println(toPrint);
    return null;
  }

  def nativeMethodMap = Map[String, Seq[NoopObject] => NoopObject] (
    "println" -> println
  );

  override def nativeMethod(name: String): (Seq[NoopObject] => NoopObject) = {
    return nativeMethodMap(name);
  }
}
