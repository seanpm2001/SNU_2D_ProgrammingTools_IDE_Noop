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

import noop.model.*;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class PrintingVisitorTest {
  @Test public void testCorrectParent() {
    final Project p = new Project("p", "ns", "c");
    final Library l1 = new Library(UUID.randomUUID(), "l1");
    p.addLibrary(l1);
    final Library l2 = new Library(UUID.randomUUID(), "l2");
    p.addLibrary(l2);
    final Clazz c1 = new Clazz("c1");
    l1.addClazz(c1);
    c1.addBlock(new AnonymousBlock());
    final Block block2 = new AnonymousBlock();
    c1.addBlock(block2);
    final Clazz c2 = new Clazz("c2");
    l2.addClazz(c2);

    final PrintingVisitor visitor = new PrintingVisitor() {
      @Override
      public void print(LanguageElement element, String label, String... params) {}

      @Override
      public void visit(Clazz clazz) {
        if (clazz == c1) {
          assertEquals(l1, getParent());
        }
        if (clazz == c2) {
          assertEquals(l2, getParent());
        }
      }

      @Override
      public void visit(AnonymousBlock block) {
        assertEquals(c1, getParent());
      }
    };
    p.accept(visitor);
  }
}
