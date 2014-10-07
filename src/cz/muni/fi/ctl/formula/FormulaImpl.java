package cz.muni.fi.ctl.formula;

import cz.muni.fi.ctl.formula.operator.Operator;
import cz.muni.fi.ctl.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormulaImpl implements Formula {

    private Operator operator;
    private List<Formula> formulas;

    public FormulaImpl(Operator operator, List<Formula> formulas) {
        if (operator == null) {
            throw new NullPointerException("Cannot create formula without operator");
        }
        if (formulas == null && operator.getCardinality() != 0) {
            throw new IllegalArgumentException("Operator requires "+operator.getCardinality()+" subFormulas, formula list is null");
        }
        if (formulas != null) {
            if (operator.getCardinality() != formulas.size()) {
                throw new IllegalArgumentException("Operator requires "+operator.getCardinality()+" subFormulas, "+formulas.size()+" formulas provided");
            }
        }
        this.operator = operator;
        this.formulas = formulas;
    }

    public FormulaImpl(Operator operator, Formula formula) {
        if (operator == null) {
            throw new NullPointerException("Cannot create formula without operator");
        }
        List<Formula> list = new ArrayList<>();
        list.add(formula);
        this.operator = operator;
        this.formulas = list;
    }

    @Override
    public List<Formula> getSubFormulas() {
        return formulas;
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
    public int hashCode() {
        int k = operator.hashCode();
        for (Formula f : formulas) {
            k += f.hashCode();
        }
        return k;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FormulaImpl)) return false;

        FormulaImpl formula = (FormulaImpl) o;

        return formula.hashCode() == hashCode();
    }
}
