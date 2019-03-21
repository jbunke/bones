lexer grammar BonesLexer;

// Ignore whitespace and line comment
WS: [ \t\n]+ -> skip;
LINE_COMMENT: '//' ~[\r\n]* -> skip;
MULTILINE_COMMENT: '/*' .*? '*/' -> skip;

// Separators
LPAREN: '(';
RPAREN: ')';
LBRACKET: '[';
RBRACKET: ']';
LCURLY: '{';
RCURLY: '}';
SEMICOLON: ';';
COLON: ':';
COMMA: ',';
PERIOD: '.';

// Assignment
ASSIGN: '=';
NEGATE: '!!';
INCREMENT: '++';
DECREMENT: '--';
ADD_ASSIGN: '+=';
SUB_ASSIGN: '-=';
MUL_ASSIGN: '*=';
DIV_ASSIGN: '/=';
MOD_ASSIGN: '%=';
AND_ASSIGN: '&=';
OR_ASSIGN: '|=';

// Binary Operators
EQUAL: '==';
NOT_EQUAL: '!=';
GT: '>';
LT: '<';
GEQ: '>=';
LEQ: '<=';
RAISE: '^';
PLUS: '+';
MINUS: '-';
TIMES: '*';
DIVIDE: '/';
MOD: '%';
AND: '&&';
OR: '||';
AT_INDEX: '@';

// Unary Operators
NOT: '!';
SIZE: '#';
// NEGATIVE: '-'; (handled as MINUS rule)

// Keywords
CLASS: 'class';
PATH: 'path';
IMPORT: 'import';
BOOL: 'bool';
INT: 'int';
FLOAT: 'float';
CHAR: 'char';
STRING: 'string';
VOID: 'void';
FOREACH: 'foreach';
FOR: 'for';
IF: 'if';
ELSE: 'else';
WHILE: 'while';
TRUE: 'true';
FALSE: 'false';
RETURN: 'return';

// numbers
fragment DIGIT: '0'..'9';
FLOAT_LIT: DIGIT+ '.' DIGIT*;
INT_LIT: DIGIT+;

CHAR_QUOTE: '\'';
STR_QUOTE: '"';

fragment RESTRICTED_ASCII: ~('\\' | '\'' | '"');

STRING_LITERAL: '"' (RESTRICTED_ASCII | ESC_CHAR | CHAR_QUOTE)* '"';
fragment CHARACTER: RESTRICTED_ASCII | ESC_CHAR;
CHAR_LIT: '\'' CHARACTER '\'';

ESC_CHAR: '\\' ('0' | 'b' | 't' | 'n' | 'f' | 'r' | '"' | '\'' | '\\');

// Identifier
IDENTIFIER: [_A-Za-z] [_A-Za-z0-9]*;
