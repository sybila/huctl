
grammar CTL;

/* Main format structure */

root : fullStop? (statement fullStop)*;

statement : '#include' STRING                       # include
          | VAR_NAME '=' formula                    # assign_formula
          | VAR_NAME '=' expression                 # assign_expression
          ;

fullStop : NEWLINE+ | EOF;

/* Formula and propositions */

formula : VAR_NAME                                          #id
        | (TRUE | FALSE)                                    #bool
        | VAR_NAME ':' (IN | OUT) (PLUS | MINUS)            #direction
        | expression compare expression                     #proposition
        | '(' formula ')'                                   #parenthesis
        | unaryOp formula                                   #unary
        //we list operators explicitly, becuase writing them as a subrule broke operator priority
        | formula CON formula                               #and
        | formula DIS formula                               #or
        |<assoc=right> formula IMPL formula                 #implies
        | formula EQIV formula                              #equal
        |<assoc=right> formula EU formula                   #EU
        |<assoc=right> formula AU formula                   #AU
        ;

expression : VAR_NAME                                       #id_expression
        | FLOAT_VAL                                         #value
        | '(' expression ')'                                #parenthesis_exp
        //we list operators explicitly, becuase writing them as a subrule broke operator priority
        | expression MUL expression                         #multipliction
        | expression DIV expression                         #division
        | expression PLUS expression                        #addition
        | expression MINUS expression                       #subtraction
        ;

/** Helper/Grouping parser rules **/

unaryOp : NEG | EX | EF | EG | AX | AF | AG;
compare : EQ | NEQ | | GT | LT | GTEQ | LTEQ;

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

MUL : '*';
DIV : '/';

/* Literals */

STRING : ["].+?["]; //non-greedy match till next quotation mark
VAR_NAME : [a-zA-Z]+[a-zA-Z0-9]*;
FLOAT_VAL : [-]?[0-9]*[.]?[0-9]+;

NEWLINE : '\r'?'\n';

WS : [ \t\u]+ -> channel(HIDDEN) ;

Block_comment : '/*' (Block_comment|.)*? '*/' -> channel(HIDDEN) ; // nesting allow
Line_comment : '//' .*? '\n' -> channel(HIDDEN) ;