package cz.muni.fi.ctl.formula;

import cz.muni.fi.ctl.formula.operator.NullaryOperator;
import cz.muni.fi.ctl.formula.operator.Operator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class FormulaImpl implements Formula {

    @NotNull
    private Operator operator;
    @NotNull
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
    public FormulaImpl(@NotNull Operator operator, Formula... formulas) {
        this.operator = operator;
        if (operator.getCardinality() != formulas.length) {
            throw new IllegalArgumentException("Operator requires "+operator.getCardinality()+" subFormulas, "+formulas.length+" formulas provided");
        }
        this.formulas.addAll(Arrays.stream(formulas).collect(Collectors.toList()));
    }

    @Override
    public int getSubFormulaCount() {
        return operator.getCardinality();
    }

    @Override
    public Formula getSubFormulaAt(int index) {
        return formulas.get(index);
    }

    @NotNull
    @Override
    public Collection<Formula> getSubFormulas() {
        return Collections.unmodifiableCollection(formulas);
    }

    @NotNull
    @Override
    public Operator getOperator() {
        return operator;
    }

    @NotNull
    @Override
    public String toString() {
        return operator + ": " + Arrays.toString(formulas.toArray());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FormulaImpl)) return false;

        @NotNull FormulaImpl formula = (FormulaImpl) o;

        return formulas.equals(formula.formulas) && operator.equals(formula.operator);

    }

    @Override
    public int hashCode() {
        int result = operator.hashCode();
        result = 31 * result + formulas.hashCode();
        return result;
    }
}
