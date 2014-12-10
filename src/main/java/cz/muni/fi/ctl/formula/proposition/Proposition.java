package cz.muni.fi.ctl.formula.proposition;

import cz.muni.fi.ctl.formula.Formula;
import cz.muni.fi.ctl.formula.operator.NullaryOperator;
import cz.muni.fi.ctl.formula.operator.Operator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public abstract class Proposition<T> implements Formula {

    public abstract boolean evaluate(T value);

    @Override
    public int getSubFormulaCount() {
        return 0;
    }

    @NotNull
    @Override
    public Formula getSubFormulaAt(int index) {
        throw new IndexOutOfBoundsException("Requested index "+index+", size 0");
    }

    @NotNull
    @Override
    public Collection<Formula> getSubFormulas() {
        return new ArrayList<>();
    }

    @NotNull
    @Override
    public Operator getOperator() {
        return NullaryOperator.INSTANCE;
    }
}
