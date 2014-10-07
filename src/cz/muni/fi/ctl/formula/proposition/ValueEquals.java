package cz.muni.fi.ctl.formula.proposition;

import cz.muni.fi.ctl.formula.Formula;
import cz.muni.fi.ctl.formula.operator.NullaryOperator;
import cz.muni.fi.ctl.formula.operator.Operator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daemontus on 23/09/14.
 */
public class ValueEquals implements Proposition<Float> {

    private float value;
    private int param;

    public ValueEquals(int param, float value) {
        this.value = value;
        this.param = param;
    }

    @Override
    public boolean evaluate(Float value) {
        return value.equals(this.value);
    }

    @Override
    public List<Formula> getSubFormulas() {
        return new ArrayList<>();
    }

    @Override
    public Operator getOperator() {
        return NullaryOperator.INSTANCE;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValueEquals)) return false;

        ValueEquals that = (ValueEquals) o;

        if (param != that.param) return false;
        if (Float.compare(that.value, value) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (value != +0.0f ? Float.floatToIntBits(value) : 0);
        result = 31 * result + param;
        return result;
    }

    @Override
    public String toString() {
        return "equals "+param+" = "+value;
    }
}
