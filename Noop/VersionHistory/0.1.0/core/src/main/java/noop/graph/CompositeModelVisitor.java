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

import com.google.inject.Inject;
import noop.model.*;

import java.util.Set;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class CompositeModelVisitor implements ModelVisitor {
  private final Set<ModelVisitor> delegates;

  @Inject
  public CompositeModelVisitor(Set<ModelVisitor> delegates) {
    this.delegates = delegates;
  }

  @Override
  public void enter(LanguageElement element) {
    for (ModelVisitor delegate : delegates) {
      delegate.enter(element);
    }
  }

  @Override
  public void leave(LanguageElement element) {
    for (ModelVisitor delegate : delegates) {
      delegate.leave(element);
    }
  }

  @Override
  public void visit(Edge edge) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(edge);
    }
  }

  @Override
  public void visit(Workspace workspace) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(workspace);
    }
  }

  @Override
  public void visit(Method method) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(method);
    }
  }

  @Override
  public void visit(Function function) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(function);
    }
  }

  @Override
  public void visit(UnitTest unitTest) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(unitTest);
    }
  }

  @Override
  public void visit(Project project) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(project);
    }
  }

  @Override
  public void visit(MethodInvocation methodInvocation) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(methodInvocation);
    }
  }

  @Override
  public void visit(Parameter parameter) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(parameter);
    }
  }

  @Override
  public void visit(Library library) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(library);
    }
  }

  @Override
  public void visit(Clazz clazz) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(clazz);
    }
  }

  @Override
  public void visit(StringLiteral stringLiteral) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(stringLiteral);
    }
  }

  @Override
  public void visit(Return aReturn) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(aReturn);
    }
  }

  @Override
  public void visit(IntegerLiteral integerLiteral) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(integerLiteral);
    }
  }

  @Override
  public void visit(Documentation documentation) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(documentation);
    }
  }

  @Override
  public void visit(Assignment assignment) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(assignment);
    }
  }

  @Override
  public void visit(IdentifierDeclaration identifierDeclaration) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(identifierDeclaration);
    }
  }

  @Override
  public void visit(Binding binding) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(binding);
    }
  }

  @Override
  public void visit(Comment comment) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(comment);
    }
  }

  @Override
  public void visit(Loop loop) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(loop);
    }
  }

  @Override
  public void visit(AnonymousBlock block) {
    for (ModelVisitor delegate : delegates) {
      delegate.visit(block);
    }
  }
}
