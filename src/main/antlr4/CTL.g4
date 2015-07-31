
grammar CTL;

/* Main format structure */

root : fullStop? (statement fullStop)*;

statement : '#include' STRING                       # include
          | VAR_NAME '=' formula                    # assign
          | '#check' formula (',' formula)*         # check
          ;

fullStop : NEWLINE+ | EOF;

/* Formula and propositions */

formula : VAR_NAME                                          #id
        | (TRUE | FALSE)                                    #bool
        | VAR_NAME ':' (IN | OUT) (PLUS | MINUS)            #direction
        | VAR_NAME floatOp FLOAT_VAL                        #proposition
        | '(' formula ')'                                   #parenthesis
        | unaryOp formula                                   #unary
        | formula boolOp formula                            #binary
        | 'E' formula 'U' formula                           #existUntil
        | 'A' formula 'U' formula                           #allUntil
        ;

/** Helper/Grouping parser rules **/

unaryOp : NEG | EX | EF | EG | AX | AF | AG;
boolOp : CON | DIS | IMPL | EQIV;
floatOp : EQ | NEQ | | GT | LT | GTEQ | LTEQ;

/** Terminals **/

TRUE : ([tT]'rue' | 'tt' | 'TT');
FALSE : ([fF]'alse' | 'ff' | 'FF');

IN : 'in';
OUT : 'out';

PLUS : '+';
MINUS : '-';

/** Operators **/

EX : 'EX';
EF : 'EF';
EG : 'EG';
AX : 'AX';
AF : 'AF';
AG : 'AG';

EQ : '==';
NEQ : '!=';
GT : '>';
LT : '<';
GTEQ : '>=';
LTEQ : '<=';

NEG : '!';
CON : '&&';
DIS : '||';
IMPL : '=>';
EQIV : '<=>';

/* Literals */

STRING : ["].+?["]; //non-greedy match till next quotation mark
VAR_NAME : [a-zA-Z]+[a-zA-Z0-9]*;
FLOAT_VAL : [-]?[0-9]*[.]?[0-9]+;

NEWLINE : '\r'?'\n';

WS : [ \t\u]+ -> channel(HIDDEN) ;

Block_comment : '/*' (Block_comment|.)*? '*/' -> channel(HIDDEN) ; // nesting allow
Line_comment : '//' .*? '\n' -> channel(HIDDEN) ;