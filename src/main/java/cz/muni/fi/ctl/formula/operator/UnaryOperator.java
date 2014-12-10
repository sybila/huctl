package cz.muni.fi.ctl.formula.operator;

public enum UnaryOperator implements Operator {
    NEGATION, EXISTS_NEXT, ALL_NEXT, EXISTS_FUTURE, ALL_FUTURE, EXISTS_GLOBAL, ALL_GLOBAL
    ;

    @Override
    public int getCardinality() {
        return 1;
    }

}
