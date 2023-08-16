// Copyright 2009 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

grammar Doc;

options {
  backtrack = true;
  output = AST;
  ASTLabelType = CommonTree;
}

@header {
  package noop.grammar.antlr;
}

@lexer::header {
  package noop.grammar.antlr;
}

@lexer::rulecatch {
  catch (RecognitionException e) {
    reportError(e);
    throw new RuntimeException(e);
  }
}

@rulecatch {
  catch (RecognitionException e) {
    reportError(e);
    throw e;
  }
}

doc 
	: DocLine* docCommandDeclaration*
	;

docCommandDeclaration
	: docCommand DocLine*
	;
	
docCommand
	: '@param'
// TODO(gabe)	: '@param' VariableIdentifier
	| CustomCommand
	;
	
CustomCommand
	: '@' 'a' .. 'z' ('a' .. 'z' | 'A' .. 'Z' | '0' .. '9')*
	;

VariableIdentifier
	: 'a' .. 'z' ('a' .. 'z' | 'A' .. 'Z' | '0' .. '9')*
	;

DocLine
	:  ~'@' ~('\n'|'\r')* '\r'? '\n'
	;

WS      : (' '|'\t'|'\n')+
        ;

