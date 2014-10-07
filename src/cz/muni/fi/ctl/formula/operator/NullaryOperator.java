package cz.muni.fi.ctl.formula.operator;

/**
 * Created by daemontus on 23/09/14.
 */
public class NullaryOperator implements Operator {
    public static final NullaryOperator INSTANCE = new NullaryOperator();

    @Override
    public int getCardinality() {
        return 0;
    }
}
