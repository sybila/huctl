
grammar CTL;

/* Main format structure */

root : fullStop? (statement fullStop)*;

statement : ':include' STRING                                                           #includeStatement
          //aliases are ambiguous - we can't decide whether they are formulas or expressions until they are resolved
          | FLAG? VAR_NAME '=' (formula | dirFormula | expression | VAR_NAME)           #assignStatement
          ;

fullStop : NEWLINE+ | EOF | ';';

/* Formula and propositions */

formula : VAR_NAME                                                                      #id
        | (TRUE | FALSE)                                                                #bool
        | VAR_NAME ':' (IN | OUT) (PLUS | MINUS)                                        #transition
        | expression compare expression                                                 #proposition
        | '(' formula ')'                                                               #parenthesis
        | NEG formula                                                                   #negation
        | dirModifier? TEMPORAL_UNARY formula                                           #unaryTemporal
        //we list operators explicitly, becuase writing them as a subrule broke operator priority
        | formula CON formula                                                           #and
        | formula DIS formula                                                           #or
        |<assoc=right> formula IMPL formula                                             #implies
        | formula EQIV formula                                                          #equal
        |<assoc=right> formula dirModifierL? TEMPORAL_BINARY dirModifierR? formula      #binaryTemporal
        | (FORALL | EXISTS) VAR_NAME setBound? ':' formula                              #firstOrder
        | (AT | BIND) VAR_NAME ':' formula                                              #hybrid
        ;

setBound : 'in' formula;

dirModifier : '{' dirFormula '}';
dirModifierL : dirModifier;
dirModifierR : dirModifier;

/* Direction formula - used as an optional parameter for temporal operators */

dirFormula : VAR_NAME                                       #dirId
           | VAR_NAME (PLUS | MINUS)                        #dirProposition
           | '(' dirFormula ')'                             #dirParenthesis
           | NEG dirFormula                                 #dirNegation
           | dirFormula CON dirFormula                      #dirAnd
           | dirFormula DIS dirFormula                      #dirOr
           | <assoc=right> dirFormula IMPL dirFormula       #dirImplies
           | dirFormula EQIV dirFormula                     #dirEqual
           ;

/* Numeric proposition */

compare : EQ | NEQ | | GT | LT | GTEQ | LTEQ;

expression : VAR_NAME                                       #expId
        | FLOAT_VAL                                         #expValue
        | '(' expression ')'                                #expParenthesis
        //we list operators explicitly, becuase writing them as a subrule broke operator priority
        | expression MUL expression                         #expMultiply
        | expression DIV expression                         #expDivide
        | expression PLUS expression                        #expAdd
        | expression MINUS expression                       #expSubtract
        ;

/** Terminals **/

TRUE : ([tT]'rue' | 'tt');
FALSE : ([fF]'alse' | 'ff');

IN : 'in';
OUT : 'out';

PLUS : '+';
MINUS : '-';

FLAG : ':?';

/** Path quantifiers **/

A : 'A';
E : 'E';
PA : 'pA';
PE : 'pE';

PATH : (A|E|PA|PE);

/** Temporal operators **/

X : 'X';
F : 'F';
G : 'G';
U : 'U';
WF : 'wF';
WX : 'wX';

TEMPORAL_UNARY : PATH (X|G|F);
TEMPORAL_BINARY : PATH U;

/** Logical operators **/

NEG : '!';
CON : '&&';
DIS : '||';
IMPL : '->';
EQIV : '<->';

/** first order operators **/

FORALL : 'forall';
EXISTS : 'exists';

/** Hybrid operators **/

BIND : 'bind';
AT : 'at';

/** Compare operators **/

EQ : '==';
NEQ : '!=';
GT : '>';
LT : '<';
GTEQ : '>=';
LTEQ : '<=';

/** Arithmetics **/

MUL : '*';
DIV : '/';

/* Literals */

STRING : ["].+?["]; //non-greedy match till next quotation mark
VAR_NAME : [_a-zA-Z]+[_a-zA-Z0-9]*;
FLOAT_VAL : [-]?[0-9]*[.]?[0-9]+;

NEWLINE : '\r'?'\n';

WS : [ \t\u]+ -> channel(HIDDEN) ;

Block_comment : '/*' (Block_comment|.)*? '*/' -> channel(HIDDEN) ; // nesting allow
C_Line_comment : '//' .*? ('\n' | EOF) -> channel(HIDDEN) ;
Python_Line_comment : '#' .*? ('\n' | EOF) -> channel(HIDDEN) ;