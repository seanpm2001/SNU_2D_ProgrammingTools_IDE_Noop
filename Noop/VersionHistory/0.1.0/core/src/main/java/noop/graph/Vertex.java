/*
 * Copyright 2010 Google Inc.
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

package noop.graph;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.UUID;

/**
 * A vertex in the semantic graph, which allows serializable references to language elements.
 * Each vertex is addressable by the library that contains it, and the index of the vertex within the library.
 * Edges have one Vertex as their source and one as the destination.
 * 
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Vertex {
  public final UUID libraryUid;
  public final int index;
  public static final Vertex NONE = new Vertex(null, -1);

  public Vertex(UUID libraryUid, int index) {
    this.libraryUid = libraryUid;
    this.index = index;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(libraryUid).append(index).toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }
    Vertex rhs = (Vertex) obj;
    return new EqualsBuilder()
        .append(libraryUid, rhs.libraryUid)
        .append(index, rhs.index)
        .isEquals();
  }

  @Override
  public String toString() {
    if (this == NONE) {
      return "NO_VERTEX";
    }
    return index + "(" + libraryUid.toString().substring(0, libraryUid.toString().indexOf("-")) + ")";
  }
}
