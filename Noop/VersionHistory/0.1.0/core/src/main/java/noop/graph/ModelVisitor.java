package noop.graph;

import noop.model.*;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public interface ModelVisitor {
  void enter(LanguageElement element);

  void leave(LanguageElement element);

  void visit(Edge edge);

  void visit(Workspace workspace);

  void visit(Method method);

  void visit(Function function);

  void visit(UnitTest unitTest);

  void visit(Project project);

  void visit(MethodInvocation methodInvocation);

  void visit(Parameter parameter);

  void visit(Library library);

  void visit(Clazz clazz);

  void visit(StringLiteral stringLiteral);

  void visit(Return aReturn);

  void visit(IntegerLiteral integerLiteral);

  void visit(Documentation documentation);

  void visit(Assignment assignment);

  void visit(IdentifierDeclaration identifierDeclaration);

  void visit(Binding binding);

  void visit(Comment comment);

  void visit(Loop loop);

  void visit(AnonymousBlock block);
}
