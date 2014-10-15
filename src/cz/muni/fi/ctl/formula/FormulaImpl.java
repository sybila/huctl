package cz.muni.fi.ctl.formula;

import cz.muni.fi.ctl.formula.operator.NullaryOperator;
import cz.muni.fi.ctl.formula.operator.Operator;

import java.util.*;

public class FormulaImpl implements Formula {

    private Operator operator;
    private List<Formula> formulas = new ArrayList<>();

    /**
     * Utility constructor for nullary formulas.
     */
    public FormulaImpl() {
        this.operator = NullaryOperator.INSTANCE;
    }

    /**
     * Universal constructor for creating formulas with any cardinality.
     * @param operator N-ary operator.
     * @param formulas List of N formulas (empty list for nullary operator).
     */
    public FormulaImpl(Operator operator, List<Formula> formulas) {
        initOperator(operator);
        if (formulas == null) {
            throw new NullPointerException("No formulas provided.");
        }
        if (operator.getCardinality() != formulas.size()) {
            throw new IllegalArgumentException("Operator requires "+operator.getCardinality()+" subFormulas, "+formulas.size()+" formulas provided");
        }
        for (Formula formula : formulas) {
            this.formulas.add(formula);
        }
    }

    /**
     * Utility constructor for unary formulas.
     * @param operator Unary operator.
     * @param formula Inner formula.
     */
    public FormulaImpl(Operator operator, Formula formula) {
        initOperator(operator);
        if (formula == null) {
            throw new NullPointerException("No formula provided.");
        }
        if (operator.getCardinality() != 1) {
            throw new IllegalArgumentException("Operator cardinality not 1, but one formula provided. Cardinality: "+operator.getCardinality());
        }
        this.formulas.add(formula);
    }

    private void initOperator(Operator operator) {
        if (operator == null) {
            throw new NullPointerException("Cannot create formula without operator");
        }
        this.operator = operator;
    }

    @Override
    public int getSubFormulaCount() {
        return operator.getCardinality();   //faster than size() call;
    }

    @Override
    public Formula getSubFormulaAt(int index) {
        return formulas.get(index);
    }

    @Override
    public Collection<Formula> getSubFormulas() {
        return Collections.unmodifiableCollection(formulas);
    }

    @Override
    public Operator getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return operator+": "+ Arrays.toString(formulas.toArray());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FormulaImpl)) return false;

        FormulaImpl formula = (FormulaImpl) o;

        return formulas.equals(formula.formulas) && operator.equals(formula.operator);

    }

    @Override
    public int hashCode() {
        int result = operator.hashCode();
        result = 31 * result + formulas.hashCode();
        return result;
    }
}
