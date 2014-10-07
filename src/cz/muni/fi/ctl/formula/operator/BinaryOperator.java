package cz.muni.fi.ctl.formula.operator;

import java.util.regex.Pattern;

public enum BinaryOperator implements Operator {
    AND, EXISTS_UNTIL
    ;

    @Override
    public int getCardinality() {
        return 2;
    }

}
