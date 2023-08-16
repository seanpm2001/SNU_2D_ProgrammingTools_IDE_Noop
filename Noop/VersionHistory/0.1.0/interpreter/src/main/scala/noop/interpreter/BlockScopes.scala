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
import types.{NoopType, NoopObject};

/**
 * A handy little data structure which wraps the stack of scopes that exist on a single stack frame.
 * @author alexeagle@google.com (Alex Eagle)
 */

class BlockScopes(val scopes: Stack[Map[String, Tuple2[NoopType, NoopObject]]]) {

  def inScope(name: String)(f: => Any) {
    scopes += Map.empty[String, Tuple2[NoopType, NoopObject]];
    try {
      f;
    } finally {
      scopes.pop;
    }
  }

  def registerIdentifier(name: String, value: Tuple2[NoopType, NoopObject]) = {
    if (scopes.isEmpty) {
      throw new RuntimeException("Cannot declare an identifier unless we are in a scope");
    }
    if (hasIdentifier(name)) {
      throw new RuntimeException("Identifier " + name + " is already declared");
    }
    scopes.top += Pair(name, value);
  }

  def setValue(name: String, value: Tuple2[NoopType, NoopObject]): Unit = {
    for (identifiers <- scopes.elements) {
      if (identifiers.contains(name)) {
        identifiers(name) = value;
        return;
      }
    }
    throw new RuntimeException("No such identifier to assign: " + name);
  }

  def hasIdentifier(name: String): Boolean = getIdentifier(name) != null;

  def getIdentifier(name: String): Tuple2[NoopType, NoopObject] = {
    for (identifiers <- scopes.elements) {
      if (identifiers.contains(name)) {
        return identifiers(name);
      }
    }
    null;
  }
}