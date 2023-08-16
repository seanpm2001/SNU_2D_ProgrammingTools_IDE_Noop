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

package noop.model;

import noop.graph.ModelVisitor;
import org.joda.time.Instant;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Comment extends LanguageElement<Comment> {
  public final String text;
  public final String user;
  public final Instant timestamp;

  public Comment(String text, String user, Instant timestamp) {
    this.text = text;
    this.user = user;
    this.timestamp = timestamp;
  }

  @Override
  public void accept(ModelVisitor v) {
    v.visit(this);
  }
}
