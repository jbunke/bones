parser grammar BonesParser;

options {
  tokenVocab=BonesLexer;
}

ident: IDENTIFIER ;

path: PATH ident (PERIOD ident)* SEMICOLON ;

import_stat: IMPORT ident
(PERIOD ident)* SEMICOLON ;

type: BOOL                                    #BOOL_TYPE
| INT                                         #INT_TYPE
| FLOAT                                       #FLOAT_TYPE
| CHAR                                        #CHAR_TYPE
| STRING                                      #STRING_TYPE
| VOID                                        #VOID_TYPE
| type LPAREN RPAREN                          #LIST_TYPE
| type LBRACKET RBRACKET                      #ARRAY_TYPE
;

decl: type ident SEMICOLON ;

init: type ident ASSIGN expr SEMICOLON ;

field: decl                                   #DECLARED_FIELD
| init                                        #INITIALISED_FIELD
;

param: type ident ;

param_list: param (COMMA param)* ;

main: VOID MAIN LPAREN STRING LBRACKET
RBRACKET ident RPAREN LCURLY stat* RCURLY ;

funct: type ident LPAREN param_list?
RPAREN LCURLY stat* RCURLY ;

bool_literal: TRUE                            #TRUE_LITERAL
| FALSE                                       #FALSE_LITERAL
;
char_literal: CHAR_LIT ;
string_literal: STRING_LITERAL ;
int_literal: INT_LIT ;
float_literal: FLOAT_LIT ;

expr: int_literal                             #INT_EXPR
| float_literal                               #FLOAT_EXPR
| bool_literal                                #BOOL_EXPR
| char_literal                                #CHAR_EXPR
| string_literal                              #STRING_EXPR
| assignable                                  #ASSIGNABLE_EXPR
| CALL (ident PERIOD)* ident
  LPAREN (expr (COMMA expr)* )? RPAREN        #FUNCTION_CALL_EXPR
| op=(NOT | SIZE | MINUS) expr                #UNARY_OP_EXPR
| expr RAISE expr                             #EXPONENTIATION_EXPR
| expr op=(TIMES | DIVIDE | MOD) expr         #MUL_DIV_MOD_EXPR
| expr op=(PLUS | MINUS) expr                 #ARITH_EXPR
| expr AT_INDEX expr                          #AT_INDEX_EXPR
| expr op=(GEQ | LEQ | GT | LT) expr          #COMPARISON_EXPR
| expr op=(EQUAL | NOT_EQUAL) expr            #EQUALITY_EXPR
| expr AND expr                               #AND_EXPR
| expr OR expr                                #OR_EXPR
| LPAREN expr RPAREN                          #PARENTHETICAL
;

list_elem: ident
(LPAREN int_literal RPAREN)+ ;

array_elem: ident
(LBRACKET int_literal RBRACKET)+ ;

assignable: ident                             #IDENT_ASSIGNABLE
| list_elem                                   #LIST_ELEM_ASSIGNABLE
| array_elem                                  #ARRAY_ELEM_ASSIGNABLE
;

assignment:
assignable ASSIGN expr SEMICOLON              #STANDARD_ASSIGNMENT
| assignable NEGATE SEMICOLON                 #NEGATE_ASSIGNMENT
| assignable INCREMENT SEMICOLON              #INCREMENT_ASSIGNMENT
| assignable DECREMENT SEMICOLON              #DECREMENT_ASSIGNMENT
| assignable ADD_ASSIGN expr SEMICOLON        #ADD_ASSIGNMENT
| assignable SUB_ASSIGN expr SEMICOLON        #SUB_ASSIGNMENT
| assignable MUL_ASSIGN expr SEMICOLON        #MUL_ASSIGNMENT
| assignable DIV_ASSIGN expr SEMICOLON        #DIV_ASSIGNMENT
| assignable MOD_ASSIGN expr SEMICOLON        #MOD_ASSIGNMENT
| assignable AND_ASSIGN expr SEMICOLON        #AND_ASSIGNMENT
| assignable OR_ASSIGN expr SEMICOLON         #OR_ASSIGNMENT
;

body: LCURLY stat* RCURLY ;

stat: FOR LPAREN init SEMICOLON expr
  SEMICOLON assignment RPAREN body            #FOR_STAT
| FOREACH LPAREN ident COLON expr
  RPAREN body                                 #FOREACH_STAT
| IF LPAREN expr RPAREN body
  (ELSE IF LPAREN expr RPAREN body)*
  (ELSE body)?                                #IF_STAT
| WHILE LPAREN expr RPAREN body               #WHILE_STAT
| PRINTLN LPAREN expr RPAREN SEMICOLON        #PRINTLN_STAT
| PRINT LPAREN expr RPAREN SEMICOLON          #PRINT_STAT
| RETURN SEMICOLON                            #VOID_RETURN_STAT
| RETURN expr SEMICOLON                       #RETURN_STAT
| decl                                        #DECLARATION_STAT
| init                                        #INITIALISATION_STAT
| assignment                                  #ASSIGNMENT_STAT
| expr SEMICOLON                              #EXPRESSION_STAT
;

// root-level program rule
class_rule: path? import_stat* CLASS
ident LCURLY field* main? funct* RCURLY EOF ;
