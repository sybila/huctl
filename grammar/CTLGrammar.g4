/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

grammar CTLGrammar;

formula : op_unary #Unary
        | op_binary_temp #BinTemp
        | op_binary_bool #BinBool
        | prop #Proposition;                        

op_unary : NEG formula #UnBool
        | quantifier path formula #UnTemp;
op_binary_temp : quantifier SB_OPEN formula UNTIL formula SB_CLOSE;
op_binary_bool : B_OPEN formula op_bool formula B_CLOSE;

/* Propositions */
prop : CB_OPEN expression CB_CLOSE;
expression : comparison | bool;
comparison : VAR_NAME op_float FLOAT_VAL;

/* Terminals: */
    bool : TRUE | FALSE ;
    TRUE : 'True';
    FALSE : 'False';
    /* Boolean Operators */
    op_bool : AND | OR | IMPL | EQIV;
        NEG : '!';
        AND : '&&';
        OR : '||';
        IMPL : '=>';
        EQIV : '<=>';
    /* Temporal Operators */
    quantifier : ALL | EXISTS;
    path : NEXT | FUTURE | GLOBAL;
        ALL : 'A';
        EXISTS : 'E';
        NEXT : 'X';
        FUTURE : 'F';
        GLOBAL : 'G';
        UNTIL : 'U';
    /* Comparison operators */
    op_float : EQ | N_EQ | GT | LT | GT_EQ | LT_EQ; 
        EQ : '==';
        N_EQ : '!=';
        GT : '>';
        LT : '<';
        GT_EQ : '>=';
        LT_EQ : '<=';
    /* Brackets */
        B_OPEN : '(';
        B_CLOSE : ')';
        CB_OPEN : '{';
        CB_CLOSE : '}';
        SB_OPEN : '[';
        SB_CLOSE : ']';
    /* Values */
        VAR_NAME : [a-z][a-zA-Z]+;
        FLOAT_VAL : [0-9]+.[0-9]+;
   WS : [ \t]+ -> skip ; // toss out whitespace