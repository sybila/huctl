/* Grammar for CTL parser */

grammar CTL;

@header { 
import cz.muni.fi.ctl.formula.Formula;
import cz.muni.fi.ctl.formula.FormulaImpl;
import cz.muni.fi.ctl.formula.operator.Operator;
import cz.muni.fi.ctl.formula.operator.BinaryOperator;
import cz.muni.fi.ctl.formula.operator.UnaryOperator;
import cz.muni.fi.ctl.formula.proposition.Contradiction;
import cz.muni.fi.ctl.formula.proposition.FloatProposition;
import cz.muni.fi.ctl.formula.proposition.Proposition;
import cz.muni.fi.ctl.formula.proposition.Tautology;

import java.util.HashMap; 
import java.util.List;
import java.util.Map;
} 

root locals [
    Map<String, Proposition> propositions = new HashMap<>(),
    Formula result
    ] : ctl; 

ctl : 
      define ctl
    | property
;

define : 
           DEF 
           VAR_NAME { String name = $VAR_NAME.text; } 
           VAR_NAME { String prop = $VAR_NAME.text; } 
           op_float 
           FLOAT_VAL 
    { $root::propositions.put( name,
        new FloatProposition(
            Float.parseFloat($FLOAT_VAL.text),
            prop,
            $op_float.op
            )
      ); 
    }
;

property : PROP formula { $root::result = $property::formula().processed; };

formula returns [ Formula processed ] : 
          TRUE 
          { $processed = Tautology.INSTANCE; }
        | FALSE 
          { $processed = Contradiction.INSTANCE; }
        | VAR_NAME
          { $processed = $root::propositions.get($VAR_NAME.text);
            if ($processed == null) throw new IllegalArgumentException("Unknown proposition: "+$VAR_NAME.text);
          }
        | NEG formula
          { $processed = new FormulaImpl(UnaryOperator.NEGATION, $formula.ctx.formula.processed ); }
        | B_OPEN formula op_bool formula B_CLOSE
          { $processed = new FormulaImpl($op_bool.op, $formula.ctx.formula(0).processed, $formula.ctx.formula(1).processed ); }
        | EXISTS B_OPEN formula UNTIL formula B_CLOSE
          { $processed = new FormulaImpl(BinaryOperator.EXISTS_UNTIL, $formula.ctx.formula(0).processed, $formula.ctx.formula(1).processed ); }
        | ALL B_OPEN formula UNTIL formula B_CLOSE
          { $processed = new FormulaImpl(BinaryOperator.ALL_UNTIL, $formula.ctx.formula(0).processed, $formula.ctx.formula(1).processed ); }
        | temporal formula
          { $processed = new FormulaImpl($temporal.op, $formula.ctx.formula(0).processed ); }
        ;

temporal returns [ Operator op ] :                                     
                                    'EX' { $op = UnaryOperator.EXISTS_NEXT; }
                                    | 'AX' { $op = UnaryOperator.ALL_NEXT; }
                                    | 'EF' { $op = UnaryOperator.EXISTS_FUTURE; }
                                    | 'AF' { $op = UnaryOperator.ALL_FUTURE; }
                                    | 'EG' { $op = UnaryOperator.EXISTS_GLOBAL; }
                                    | 'AG' { $op = UnaryOperator.ALL_GLOBAL; }
                                 ;

op_bool returns [ Operator op ] : 
          AND { $op = BinaryOperator.AND; } 
        | OR { $op = BinaryOperator.OR; } 
        | IMPL { $op = BinaryOperator.IMPLICATION; } 
        | EQIV { $op = BinaryOperator.EQUIVALENCE; }
;

op_float returns [ FloatProposition.Operator op ] : 
          EQ { $op = FloatProposition.Operator.EQ; } 
        | N_EQ { $op = FloatProposition.Operator.NEQ; } 
        | GT { $op = FloatProposition.Operator.GT; } 
        | LT { $op = FloatProposition.Operator.LT; }
        | GT_EQ { $op = FloatProposition.Operator.GT_EQ; } 
        | LT_EQ { $op = FloatProposition.Operator.LT_EQ; }
; 

DEF : '#define';
PROP : '#property';
NEWLINE   : '\r' '\n' | '\n' | '\r';

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
        VAR_NAME : [a-zA-Z]+;
        FLOAT_VAL : [-]?[0-9]+.[0-9]+;
    WS : [ \t\n]+ -> skip ; // toss out whitespace