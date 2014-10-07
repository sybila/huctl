package cz.muni.fi.ctl.formula.proposition;

import cz.muni.fi.ctl.formula.Formula;
import cz.muni.fi.ctl.formula.operator.NullaryOperator;
import cz.muni.fi.ctl.formula.operator.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by daemontus on 23/09/14.
 */
public class Contradiction implements Proposition<Object> {

    @Override
    public boolean evaluate(Object value) {
        return false;
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
    public String toString() {
        return "False";
    }
}
