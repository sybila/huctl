
grammar CTL;

/* Main format structure */

root : fullStop? (statement fullStop)*;

statement : '#include' STRING                       # include
          | VAR_NAME '=' formula                    # assign
          ;

fullStop : NEWLINE+ | EOF;

/* Formula and propositions */

formula : VAR_NAME                                          #id
        | (TRUE | FALSE)                                    #bool
        | VAR_NAME ':' (IN | OUT) (PLUS | MINUS)            #direction
        | VAR_NAME floatOp FLOAT_VAL                        #proposition
        | '(' formula ')'                                   #parenthesis
        | unaryOp formula                                   #unary
        | formula binaryOp formula                          #binary
        ;

/** Helper/Grouping parser rules **/

unaryOp : NEG | EX | EF | EG | AX | AF | AG;
binaryOp : EU | AU | CON | DIS | IMPL | EQIV;
floatOp : EQ | NEQ | | GT | LT | GTEQ | LTEQ;

/** Terminals **/

TRUE : ([tT]'rue' | 'tt');
FALSE : ([fF]'alse' | 'ff');

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
EU : 'EU';
AU : 'AU';

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