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
| ident                                       #CLASS_TYPE
| type LPAREN RPAREN                          #LIST_TYPE
| type LBRACKET RBRACKET                      #ARRAY_TYPE
;

decl: type ident SEMICOLON ;

init: type ident ASSIGN rhs SEMICOLON ;

field: decl                                   #DECLARED_FIELD
| init                                        #INITIALISED_FIELD
;

param: type ident ;

param_list: param (COMMA param)* ;

constructor: CONSTRUCTOR LPAREN param_list?
RPAREN body;

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
| READ LPAREN RPAREN                          #READ_EXPR
| RANDOM LPAREN RPAREN                        #RANDOM_EXPR
| LPAREN type RPAREN expr                     #CAST_EXPR
| NEW ident LPAREN (expr (COMMA expr)* )?
  RPAREN                                      #CONSTRUCTOR_CALL_EXPR
| CALL ident (PERIOD ident)?
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
| expr op=(EQUAL | NOT_EQUAL | GEQ |
  LEQ | GT | LT) DISJUNCTION LCURLY expr
  (VERTBAR expr)+ RCURLY                      #DISJUNCT_EXPR
| LPAREN expr RPAREN                          #PARENTHETICAL
;

list_elem: ident
(LPAREN expr RPAREN)+ ;

array_elem: ident
(LBRACKET expr RBRACKET)+ ;

list_literal: LISTINIT
LCURLY (rhs (COMMA rhs)*)? RCURLY ;

array_literal: ARRAYINIT
LCURLY (rhs (COMMA rhs)*)? RCURLY ;

list_init:
type LPAREN int_literal RPAREN ;

array_init:
type LBRACKET int_literal RBRACKET ;

rhs: expr                                     #EXPR_RHS
| list_literal                                #LIST_LIT_RHS
| array_literal                               #ARRAY_LIT_RHS
| list_init                                   #LIST_INIT_RHS
| array_init                                  #ARRAY_INIT_RHS
;

assignable: ident                             #IDENT_ASSIGNABLE
| ident (PERIOD ident)+                       #EXTERNAL_ASSIGNABLE
| list_elem                                   #LIST_ELEM_ASSIGNABLE
| array_elem                                  #ARRAY_ELEM_ASSIGNABLE
;

assignment:
assignable ASSIGN rhs                         #STANDARD_ASSIGNMENT
| assignable NEGATE                           #NEGATE_ASSIGNMENT
| assignable INCREMENT                        #INCREMENT_ASSIGNMENT
| assignable DECREMENT                        #DECREMENT_ASSIGNMENT
| assignable ADD_ELEM_AT rhs COMMA expr       #ADD_ELEM_AT_ASSIGNMENT
| assignable REM_ELEM_AT expr                 #REM_ELEM_AT_ASSIGNMENT
| assignable ADD_ASSIGN expr                  #ADD_ASSIGNMENT
| assignable SUB_ASSIGN expr                  #SUB_ASSIGNMENT
| assignable MUL_ASSIGN expr                  #MUL_ASSIGNMENT
| assignable DIV_ASSIGN expr                  #DIV_ASSIGNMENT
| assignable MOD_ASSIGN expr                  #MOD_ASSIGNMENT
| assignable AND_ASSIGN expr                  #AND_ASSIGNMENT
| assignable OR_ASSIGN expr                   #OR_ASSIGNMENT
;

body: LCURLY stat* RCURLY ;

stat: FOR LPAREN init expr
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
| assignment SEMICOLON                        #ASSIGNMENT_STAT
| expr SEMICOLON                              #EXPRESSION_STAT
;

command: expr                                 #EXPR_COMMAND
| stat                                        #STAT_COMMAND
| funct                                       #FUNCT_COMMAND
| import_stat                                 #IMPORT_COMMAND
;

// root-level command rule
shell_rule: command EOF ;

// root-level program rule
class_rule: path? import_stat* CLASS
ident LCURLY field* constructor* main?
funct* RCURLY EOF ;
