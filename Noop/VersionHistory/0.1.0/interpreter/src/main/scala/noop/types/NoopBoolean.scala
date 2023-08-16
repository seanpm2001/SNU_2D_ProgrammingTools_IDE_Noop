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

import noop.model.ClassDefinition

import scala.collection.immutable
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted
import com.google.inject.name.Named

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class NoopBoolean @Inject() (@Named("Boolean") classDef: ClassDefinition,
                             booleanFactory: BooleanFactory, stringFactory: StringFactory,
                             @Assisted val value: Boolean)
        extends NoopObject(classDef) {

  def other(args: Seq[NoopObject]): Boolean = args(0).asInstanceOf[NoopBoolean].value;
  def nativeMethodMap = immutable.Map[String, Seq[NoopObject] => NoopObject](
    "and" -> ((args: Seq[NoopObject]) => booleanFactory.create(value && other(args))),
    "or" -> ((args: Seq[NoopObject]) => booleanFactory.create(value || other(args))),
    "xor" -> ((args: Seq[NoopObject]) => booleanFactory.create(value ^ other(args))),
    "not" -> ((args: Seq[NoopObject]) => booleanFactory.create(!value)),
    "toString" -> ((args: Seq[NoopObject]) => stringFactory.create(value.toString))
  );

  override def nativeMethod(name: String): (Seq[NoopObject] => NoopObject) = {
    return nativeMethodMap(name);
  }
}
