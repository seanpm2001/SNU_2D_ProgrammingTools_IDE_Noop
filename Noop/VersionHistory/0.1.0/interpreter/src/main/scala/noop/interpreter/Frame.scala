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
package noop.interpreter;

import collection.mutable.{Map, Stack};
import org.slf4j.LoggerFactory;

import model.Method;
import types.{NoopType, NoopObject};

 /**
  * @author alexeagle@google.com (Alex Eagle)
  * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
  */
class Frame(val thisRef: NoopObject, val method: Method) {
  val blockScopes = new BlockScopes(new Stack[Map[String, Tuple2[NoopType, NoopObject]]]());
  val lastEvaluated = new Stack[NoopObject]();
  val logger = LoggerFactory.getLogger(this.getClass());

  def addIdentifier(name: String, arg: Tuple2[NoopType, NoopObject]) = {
    logger.trace("Adding identifier {} to the current frame with value {}", name, arg._2);
    blockScopes.registerIdentifier(name, arg);
  }

}
