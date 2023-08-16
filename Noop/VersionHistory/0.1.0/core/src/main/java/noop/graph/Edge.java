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

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Edge {
  public Edge(Vertex src, EdgeType type, Vertex dest) {
    this.type = type;
    this.src = src;
    this.dest = dest;
  }

  public void accept(ModelVisitor v) {
    v.visit(this);
  }

  public enum EdgeType {
    INVOKE,
    IMPLEMENT,
    TYPEOF,
    TARGET,
    ARG
  }

  public final EdgeType type;
  public final Vertex src;
  public final Vertex dest;

  @Override
  public String toString() {
    return src + "->" + dest;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(src)
        .append(dest)
        .append(type)
        .toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) { return false; }
    if (obj == this) { return true; }
    if (obj.getClass() != getClass()) {
      return false;
    }
    Edge rhs = (Edge) obj;
    return new EqualsBuilder()
        .append(src, rhs.src)
        .append(dest, rhs.dest)
        .append(type, rhs.type)
        .isEquals();
  }
}
