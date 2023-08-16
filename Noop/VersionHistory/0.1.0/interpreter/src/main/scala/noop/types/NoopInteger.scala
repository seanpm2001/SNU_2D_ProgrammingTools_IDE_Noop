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

import noop.model.ClassDefinition;

import scala.collection.immutable;
import com.google.inject.name.Named;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.Inject;

import scala.collection.immutable;

import model.ClassDefinition;

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class NoopInteger @Inject() (@Named("Int") classDef: ClassDefinition,
    integerFactory: IntegerFactory, stringFactory: StringFactory, booleanFactory: BooleanFactory,
    @Assisted val value: Int) extends NoopObject(classDef) {

  def other(args: Seq[NoopObject]): Int = {
    args(0).asInstanceOf[NoopInteger].value;
  }

  def nativeMethodMap = immutable.Map[String, Seq[NoopObject] => NoopObject](
    "plus" -> ((args: Seq[NoopObject]) => integerFactory.create(value + other(args))),
    "minus" -> ((args: Seq[NoopObject]) => integerFactory.create(value - other(args))),
    "multiply" -> ((args: Seq[NoopObject]) => integerFactory.create(value * other(args))),
    "divide" -> ((args: Seq[NoopObject]) => integerFactory.create(value / other(args))),
    "modulo" -> ((args: Seq[NoopObject]) => integerFactory.create(value % other(args))),
    "equals" -> ((args: Seq[NoopObject]) => booleanFactory.create(value == other(args))),
    "doesNotEqual" -> ((args: Seq[NoopObject]) => booleanFactory.create(value != other(args))),
    "greaterThan" -> ((args: Seq[NoopObject]) => booleanFactory.create(value > other(args))),
    "lesserThan" -> ((args: Seq[NoopObject]) => booleanFactory.create(value < other(args))),
    "greaterOrEqualThan" -> ((args: Seq[NoopObject]) => booleanFactory.create(value >= other(args))),
    "lesserOrEqualThan" -> ((args: Seq[NoopObject]) => booleanFactory.create(value <= other(args))),
    "toString" -> ((args: Seq[NoopObject]) => stringFactory.create(value.toString))
  );

  override def nativeMethod(name: String): (Seq[NoopObject] => NoopObject) = {
    return nativeMethodMap(name);
  }

  def isComparable(obj: Any) = obj.isInstanceOf[NoopInteger];
  override def equals(obj: Any) = obj match {
    case that: NoopInteger => (that isComparable this) &&
            that.value == this.value;
    case _ => false;
  }
  override def hashCode = super.hashCode * 41 + value.hashCode;
  override def toString() = value.toString;
}
