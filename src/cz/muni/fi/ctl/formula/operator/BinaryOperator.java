package cz.muni.fi.ctl.formula.operator;

public enum BinaryOperator implements Operator {
    AND, OR, EXISTS_UNTIL, ALL_UNTIL
    ;

    @Override
    public int getCardinality() {
        return 2;
    }

}
