
grammar CTLnew;

root :
    | NEWLINE
    | include root
    | property root;

PROP : '#property';
INCL : '#include';

include :
    INCL QUOTE PATH QUOTE NEWLINE;

property :
    PROP VAR_NAME IS formula NEWLINE;

formula:
    bool | formula binary formula | EXISTS formula UNTIL formula | B_OPEN formula B_CLOSE;

binary:
    AND | OR | IMPL | EQIV;

/* Terminals: */
    bool : TRUE | FALSE ;
    TRUE : 'True';
    FALSE : 'False';

    /* Boolean Operators */
        NEG : '!';
        AND : '&&';
        OR : '||';
        IMPL : '=>';
        EQIV : '<=>';
    /* Temporal Operators */
        ALL : 'A';
        EXISTS : 'E';
        UNTIL : 'U';
    /* Comparison operators */
        EQ : '==';
        N_EQ : '!=';
        GT : '>';
        LT : '<';
        GT_EQ : '>=';
        LT_EQ : '<=';
    /* Brackets */
        B_OPEN : '(';
        B_CLOSE : ')';
    /* Values */
        VAR_NAME : [a-zA-Z]+[a-zA-Z0-9]*;
        FLOAT_VAL : [-]?[0-9]*[.]?[0-9]+;
        PATH : [a-zA-Z/.]+[a-zA-Z0-9]*;

        IS : '=';
        QUOTE : '"';
        NEWLINE: [\n]*;
    WS : [ \t\n]+ -> skip ; // toss out whitespace