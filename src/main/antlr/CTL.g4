
grammar CTL;

/* Main format structure */

root : fullStop? (statement fullStop)*;

statement : ':include' STRING                               #includeStatement
          //aliases are ambiguous - we can't decide whether they are formulas or expressions until they are resolved
          | VAR_NAME '=' VAR_NAME                           #assignAlias
          | VAR_NAME '=' expression                         #assignExpression
          | VAR_NAME '=' dirFormula                         #assignDirFormula
          | FLAG? VAR_NAME '=' formula                      #assignFormula
          ;

fullStop : NEWLINE+ | EOF | ';';

/* Formula and propositions */

formula : VAR_NAME                                                                      #id
        | (TRUE | FALSE)                                                                #bool
        | facetProposition                                                              #direction
        | numericProposition                                                            #proposition
        | '(' formula ')'                                                               #parenthesis
        | NEG formula                                                                   #negation
        | dirModifier? TEMPORAL_UNARY formula                                           #tempUnary
        //we list operators explicitly, becuase writing them as a subrule broke operator priority
        | formula CON formula                                                           #and
        | formula DIS formula                                                           #or
        |<assoc=right> formula IMPL formula                                             #implies
        | formula EQIV formula                                                          #equal
        |<assoc=right> formula dirModifier? TEMPORAL_BINARY dirModifier? formula        #tempBinary
        | (FORALL | EXISTS) VAR_NAME setBound? ':' formula                              #firstOrder
        | (AT | BIND) VAR_NAME ':' formula                                              #hybrid
        ;

setBound : 'in' formula;

dirModifier : '{' dirFormula '}';

/* Direction formula - used as an optional parameter for temporal operators */

dirFormula : VAR_NAME (PLUS | MINUS)                        #dirProposition
           | NEG dirFormula                                 #dirNegation
           | dirFormula CON dirFormula                      #dirAnd
           | dirFormula DIS dirFormula                      #dirOr
           | <assoc=right> dirFormula IMPL dirFormula       #dirImplies
           | dirFormula EQIV dirFormula                     #dirEqual
           ;

/* Numeric proposition */

numericProposition : expression compare expression;

compare : EQ | NEQ | | GT | LT | GTEQ | LTEQ;

expression : VAR_NAME                                       #idExpression
        | FLOAT_VAL                                         #value
        | '(' expression ')'                                #parenthesisExpression
        //we list operators explicitly, becuase writing them as a subrule broke operator priority
        | expression MUL expression                         #multiplication
        | expression DIV expression                         #division
        | expression PLUS expression                        #addition
        | expression MINUS expression                       #subtraction
        ;

/* Facet proposition */

facetProposition : VAR_NAME ':' (IN | OUT) (PLUS | MINUS);

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