package cz.muni.fi.ctl.formula.operator;

public enum UnaryOperator implements Operator {
    NEGATION, EXISTS_NEXT
    ;

    @Override
    public int getCardinality() {
        return 1;
    }

}
