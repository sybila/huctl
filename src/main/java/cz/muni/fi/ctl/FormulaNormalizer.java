package cz.muni.fi.ctl;

import cz.muni.fi.ctl.formula.Formula;
import cz.muni.fi.ctl.formula.FormulaImpl;
import cz.muni.fi.ctl.formula.operator.BinaryOperator;
import cz.muni.fi.ctl.formula.operator.Operator;
import cz.muni.fi.ctl.formula.operator.UnaryOperator;
import cz.muni.fi.ctl.formula.proposition.Proposition;
import cz.muni.fi.ctl.formula.proposition.Tautology;

/**
 * <p>This class should transform formulas to normalized format processable by model checker.</p>
 * <p>Note: only one normal form is currently supported.</p>
 */
public class FormulaNormalizer {

    public Formula normalize(Formula formula) {
        //leave propositions unchanged
        if (formula instanceof Proposition) return formula;
        Operator operator = formula.getOperator();
        if (operator instanceof UnaryOperator) {
            switch ((UnaryOperator) operator) {
                case ALL_NEXT:
                    // AX p = !EX !p
                    return
                            new FormulaImpl(UnaryOperator.NEGATION,
                                    new FormulaImpl(UnaryOperator.EXISTS_NEXT,
                                            new FormulaImpl(UnaryOperator.NEGATION,
                                                    normalize(formula.getSubFormulaAt(0))
                                            )));
                case EXISTS_FUTURE:
                    // EF p = E [ true U p ]
                    return new FormulaImpl(BinaryOperator.EXISTS_UNTIL, Tautology.INSTANCE,
                            new FormulaImpl(UnaryOperator.NEGATION,
                                    normalize(formula.getSubFormulaAt(0))
                            ));
                case ALL_FUTURE:
                    // AF p = A [ true U p ]
                    return
                            new FormulaImpl(BinaryOperator.ALL_UNTIL, Tautology.INSTANCE,
                                    normalize(formula.getSubFormulaAt(0))
                            );
                case EXISTS_GLOBAL:
                    // EG p = ! A [ true U ! p ]
                    return new FormulaImpl(UnaryOperator.NEGATION,
                            new FormulaImpl(BinaryOperator.ALL_UNTIL, Tautology.INSTANCE,
                                    new FormulaImpl(UnaryOperator.NEGATION,
                                            normalize(formula.getSubFormulaAt(0))
                                    )));
                case ALL_GLOBAL:
                    // AG p = ! E [ true U ! p ]
                    return
                            new FormulaImpl(UnaryOperator.NEGATION,
                                    new FormulaImpl(BinaryOperator.EXISTS_UNTIL, Tautology.INSTANCE,
                                            new FormulaImpl(UnaryOperator.NEGATION,
                                                    normalize(formula.getSubFormulaAt(0))
                                            )));
                default:
                    return formula;
            }
        } else if (operator instanceof BinaryOperator) {
            switch ((BinaryOperator) operator) {
                case IMPLICATION:
                    // a => b = !a || b
                    return new FormulaImpl(BinaryOperator.OR,
                            new FormulaImpl(UnaryOperator.NEGATION, normalize(formula.getSubFormulaAt(0)),
                                    normalize(formula.getSubFormulaAt(1))
                            ));
                case EQUIVALENCE:
                    // a <=> b = (a && b) || (!a && !b)
                    return new FormulaImpl(BinaryOperator.OR,
                            new FormulaImpl(BinaryOperator.AND, formula.getSubFormulaAt(0), formula.getSubFormulaAt(1)),
                            new FormulaImpl(BinaryOperator.AND,
                                    new FormulaImpl(UnaryOperator.NEGATION, formula.getSubFormulaAt(0)),
                                    new FormulaImpl(UnaryOperator.NEGATION, formula.getSubFormulaAt(1))
                            ));
                default:
                    return formula;
            }
        } else {
            throw new IllegalArgumentException("Unsupported operator type: " + operator);
        }
    }

}
