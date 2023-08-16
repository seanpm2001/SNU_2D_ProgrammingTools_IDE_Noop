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
package noop.grammar;

import java.io.InputStream;

import org.antlr.runtime.{ANTLRInputStream, ANTLRStringStream, CommonTokenStream};
import org.antlr.runtime.tree.{CommonTree, CommonTreeNodeStream};

import model.SourceFile;
import grammar.antlr.{DocLexer, DocParser, NoopAST, NoopParser, NoopLexer};

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class Parser() {

  def buildParser(input: ANTLRStringStream): (NoopLexer, NoopParser) = {
    val lexer = new NoopLexer(input);

    return (lexer, new NoopParser(new CommonTokenStream(lexer)));
  }

  def buildDocParser(input: ANTLRStringStream): (DocLexer, DocParser) = {
    val lexer = new DocLexer(input);

    return (lexer, new DocParser(new CommonTokenStream(lexer)));
  }

  def parseFile(source: InputStream): CommonTree = {
    val (lexer, parser) = buildParser(new ANTLRInputStream(source));

    return parseFile(lexer, parser);
  }

  def parseFile(source: String): CommonTree = {
    val (lexer, parser) = buildParser(new ANTLRStringStream(source));

    return parseFile(lexer, parser);
  }

  def parseFile(lexer: NoopLexer, parser: NoopParser): CommonTree = {
    val file = parser.file();

    if (parser.hadErrors || lexer.hadErrors) {
      throw new ParseException("Source failed to parse");
    }
    return file.getTree().asInstanceOf[CommonTree];
  }

  def parseInterpretable(source: String): CommonTree = {
    val (lexer, parser) = buildParser(new ANTLRStringStream(source));
    val interpretable = parser.interpretable();

    if (parser.hadErrors || lexer.hadErrors) {
      throw new ParseException("Source failed to parse");
    }
    return interpretable.getTree().asInstanceOf[CommonTree];
  }

  def parseBlock(source: String): CommonTree = {
    val (lexer, parser) = buildParser(new ANTLRStringStream(source));
    val block = parser.block();

    if (parser.hadErrors || lexer.hadErrors) {
      throw new ParseException("Source failed to parse");
    }
    return block.getTree().asInstanceOf[CommonTree];
  }

  def parseDoc(source: String): CommonTree = {
    val (lexer, parser) = buildDocParser(new ANTLRStringStream(source));
    val doc = parser.doc();

    return doc.getTree().asInstanceOf[CommonTree];
  }

  def buildTreeParser(ast: CommonTree): NoopAST = {
    return new NoopAST(new CommonTreeNodeStream(ast));
  }

  def file(ast: CommonTree): SourceFile = {
    val treeParser = buildTreeParser(ast);
    val file = treeParser.file();

    if (treeParser.hadErrors) {
      throw new ParseException("Syntax errors");
    }
    return file;
  }

  def file(source: String): SourceFile = file(parseFile(source));

  def file(source: InputStream): SourceFile = file(parseFile(source));
}
