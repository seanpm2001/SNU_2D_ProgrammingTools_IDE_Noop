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

import com.google.common.collect.Sets;
import noop.graph.ModelVisitor;

import java.util.Set;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Clazz extends LanguageElement<Clazz> {
  public final String name;
  private final Set<Block> blocks = Sets.newHashSet();

  public Clazz(String name) {
    this.name = name;
  }

  @Override
  public void accept(ModelVisitor v) {
    v.visit(this);
    for (Block block : blocks) {
      v.enter(block);
      block.accept(v);
      v.leave(block);
    }
    super.accept(v);
  }

  public void addBlock(Block block) {
    blocks.add(block);
  }
}
