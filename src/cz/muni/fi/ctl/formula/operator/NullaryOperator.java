package cz.muni.fi.ctl.formula.operator;

public class NullaryOperator implements Operator {
    public static final NullaryOperator INSTANCE = new NullaryOperator();

    @Override
    public int getCardinality() {
        return 0;
    }
}
