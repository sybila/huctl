package cz.muni.fi.ctl.formula.proposition;

import cz.muni.fi.ctl.formula.Formula;
import cz.muni.fi.ctl.formula.operator.NullaryOperator;
import cz.muni.fi.ctl.formula.operator.Operator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Proposition<T> implements Formula {

    public abstract boolean evaluate(T value);

    @Override
    public int getSubFormulaCount() {
        return 0;
    }

    @Override
    public Formula getSubFormulaAt(int index) {
        throw new IndexOutOfBoundsException("Requested index "+index+", size 0");
    }

    @Override
    public Collection<Formula> getSubFormulas() {
        return new ArrayList<>();
    }

    @Override
    public Operator getOperator() {
        return NullaryOperator.INSTANCE;
    }
}
