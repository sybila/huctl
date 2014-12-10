package cz.muni.fi.ctl.formula.operator;

public enum BinaryOperator implements Operator {
    AND, OR, IMPLICATION, EQUIVALENCE, EXISTS_UNTIL, ALL_UNTIL
    ;

    @Override
    public int getCardinality() {
        return 2;
    }

}
