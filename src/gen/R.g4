/*
 [The "BSD licence"]
 Copyright (c) 2013 Terence Parr
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
    derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/**
derived from http://svn.r-project.org/R/trunk/src/main/gram.y
http://cran.r-project.org/doc/manuals/R-lang.html#Parser

I'm no R genius but this seems to work.

Requires RFilter.g4 to strip away NL that are really whitespace,
not end-of-command. See TestR.java

Usage:

$ antlr4 R.g4 RFilter.g4
$ javac *.java
$ java TestR sample.R
... prints parse tree ...
*/
grammar R;

prog:   (   expr (';'|NL)
        |   NL
        )*
        EOF
    ;

/*
expr_or_assign
    :   expr ('<-'|'='|'<<-') expr_or_assign
    |   expr
    ;
*/

expr returns [IExpression exp]:   expr '[[' sublist ']' ']'  // '[[' follows R's yacc grammar
    |   e1=expr '[' s=sublist ']' { $exp = new IndexExpr($e1.exp, $s.args); }
    |   expr ('::'|':::') expr
    |   expr ('$'|'@') expr
    |   <assoc=right> expr '^' expr
    |   ('-'|'+') expr
    |   e1=expr ':' e2=expr { $exp = new RangeExpr($e1.exp, $e2.exp); }
    |   expr USER_OP expr // anything wrappedin %: '%' .* '%'
    |   e1=expr op=('*'|'/') e2=expr { $exp = new MulDivExpr($e1.exp, $op.text, $e2.exp); }
    |   e1=expr op=('+'|'-') e2=expr { $exp = new AddSubExpr($e1.exp, $op.text, $e2.exp); }
    |   e1=expr op=('>'|'>='|'<'|'<='|'=='|'!=') e2=expr { $exp = new CompareExpr($e1.exp, $op.text, $e2.exp); }
    |   '!' expr
    |   expr ('&'|'&&') expr
    |   expr ('|'|'||') expr
    |   '~' expr
    |   expr '~' expr
    |   e1=expr ass=('<-'|'<<-'|'='|'->'|'->>'|':=') e2=expr { if ($ass.text.equals("<-") || $ass.text.equals("<<-") || $ass.text.equals("="))
                                                                   $exp = new AssignmentExpr($e1.exp, $e2.exp);
                                                               else
                                                                   $exp = new AssignmentExpr($e2.exp, $e1.exp);}
    |   'function' '(' f=formlist? ')' e=expr { $exp = new FunctionExpr($f.args, $e.exp); } // define function
    |   e1=expr '(' sublist ')' { $exp = new CallFunExpr($e1.exp, $sublist.args); } // call function
    |   '{' exprlist '}' { $exp = new CompoundExpr($exprlist.explist); } // compound statement
    |   'if' '(' e1=expr ')' e2=expr { $exp = new IfExpr($e1.exp, $e2.exp); }
    |   'if' '(' e1=expr ')' e2=expr 'else' e3=expr { $exp = new IfElseExpr($e1.exp, $e2.exp, $e3.exp); }
    |   'for' '(' ID 'in' e1=expr ')' e2=expr { $exp = new ForExpr($ID.text, $e1.exp, $e2.exp); }
    |   'while' '(' e1=expr ')' e2=expr { $exp = new WhileExpr($e1.exp, $e2.exp); }
    |   'repeat' expr { $exp = new RepeatExpr($expr.exp); }
    |   '?' expr // get help on expr, usually string or ID
    |   'next' { $exp = new NextExpr(); }
    |   'break' { $exp = new BreakExpr(); }
    |   '(' expr ')'
    |   ID { $exp = new IDExpr($ID.text); }
    |   STRING { $exp = new StringExpr($STRING.text); }
    |   HEX
    |   INT { $exp = new IntExpr($INT.int); }
    |   FLOAT { $exp = new FloatExpr(Double.parseDouble($FLOAT.text)); }
    |   COMPLEX
    |   'NULL' { $exp = null; }
    |   'NA'
    |   'Inf'
    |   'NaN'
    |   'TRUE' { $exp = new BoolExpr(true); }
    |   'FALSE' { $exp = new BoolExpr(false); }
    ;

exprlist returns [List<IExpression> explist = new ArrayList<>()]
    :   e1=expr { $explist.add($e1.exp); } ((';'|NL) (e2=expr { $explist.add($e2.exp); })?)*
    |
    ;

formlist returns [List<IArgument> args = new ArrayList<>()]: f1=form { $args.add($f1.arg); } (',' f2=form { $args.add($f2.arg); })* ;

form returns [IArgument arg]:   ID { $arg = new IDArgument($ID.text, null); }
    |   ID '=' expr { $arg = new IDArgument($ID.text, $expr.exp); }
    |   '...'
    ;

sublist returns [List<IArgument> args = new ArrayList<>()] : s1=sub { $args.add($s1.arg); } (',' s2=sub { $args.add($s2.arg); })* ;

sub returns [IArgument arg] :   expr { $arg = new ExprArgument($expr.exp); }
    |   ID '=' { $arg = new IDArgument($ID.text, null); }
    |   ID '=' expr { $arg = new IDArgument($ID.text, $expr.exp); }
    |   STRING '='
    |   STRING '=' expr
    |   'NULL' '='
    |   'NULL' '=' expr
    |   '...'
    |
    ;

HEX :   '0' ('x'|'X') HEXDIGIT+ [Ll]? ;

INT :   DIGIT+ [Ll]? ;

fragment
HEXDIGIT : ('0'..'9'|'a'..'f'|'A'..'F') ;

FLOAT:  DIGIT+ '.' DIGIT* EXP? [Ll]?
    |   DIGIT+ EXP? [Ll]?
    |   '.' DIGIT+ EXP? [Ll]?
    ;
fragment
DIGIT:  '0'..'9' ; 
fragment
EXP :   ('E' | 'e') ('+' | '-')? INT ;

COMPLEX
    :   INT 'i'
    |   FLOAT 'i'
    ;

STRING
    :   '"' ( ESC | ~[\\"] )*? '"'
    |   '\'' ( ESC | ~[\\'] )*? '\''
    |   '`' ( ESC | ~[\\'] )*? '`'
    ;

fragment
ESC :   '\\' [abtnfrv"'\\]
    |   UNICODE_ESCAPE
    |   HEX_ESCAPE
    |   OCTAL_ESCAPE
    ;

fragment
UNICODE_ESCAPE
    :   '\\' 'u' HEXDIGIT HEXDIGIT HEXDIGIT HEXDIGIT
    |   '\\' 'u' '{' HEXDIGIT HEXDIGIT HEXDIGIT HEXDIGIT '}'
    ;

fragment
OCTAL_ESCAPE
    :   '\\' [0-3] [0-7] [0-7]
    |   '\\' [0-7] [0-7]
    |   '\\' [0-7]
    ;

fragment
HEX_ESCAPE
    :   '\\' HEXDIGIT HEXDIGIT?
    ;

ID  :   '.' (LETTER|'_'|'.') (LETTER|DIGIT|'_'|'.')*
    |   LETTER (LETTER|DIGIT|'_'|'.')*
    ;
    
fragment LETTER  : [a-zA-Z] ;

USER_OP :   '%' .*? '%' ;

COMMENT :   '#' .*? '\r'? '\n' -> type(NL) ;

// Match both UNIX and Windows newlines
NL      :   '\r'? '\n' ;

WS      :   [ \t\u000C]+ -> skip ;
