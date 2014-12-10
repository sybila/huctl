package cz.muni.fi.ctl.formula.proposition;

import org.jetbrains.annotations.NotNull;

public class FloatProposition extends Proposition<Float> {

    private final float value;
    private final String variable;
    private final Operator operator;

    public FloatProposition(float value, String variable, Operator operator) {
        this.value = value;
        this.operator = operator;
        this.variable = variable;
    }

    @Override
    public boolean evaluate(Float value) {
        switch (operator) {
            case LT:
                return value < this.value;
            case LT_EQ:
                return value <= this.value;
            case GT:
                return value > this.value;
            case GT_EQ:
                return value >= this.value;
            case NEQ:
                return value != this.value;
            case EQ:
            default:
                return value == this.value;
        }
    }

    public int getNativeOperator() {
        return operator.getOperatorIndex();
    }

    public float getThreshold() {
        return value;
    }

    public String getVariable() {
        return variable;
    }

    public static enum Operator {
        EQ("==", -1), NEQ("!=", -1), GT(">", 0), GT_EQ(">=", 2), LT("<", 1), LT_EQ("<=", 3);

        private final String val;
        //represents an integer passed to an enum inside a native library
        private final int nativeIndex;

        Operator(String val, int nativeIndex) {
            this.val = val;
            this.nativeIndex = nativeIndex;
        }

        @Override
        public String toString() {
            return val;
        }

        public int getOperatorIndex() {
            return nativeIndex;
        }
    }

    @NotNull
    @Override
    public String toString() {
        return variable+" "+operator+" "+value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FloatProposition)) return false;

        @NotNull FloatProposition that = (FloatProposition) o;

        return Float.compare(that.value, value) == 0 && operator == that.operator && variable.equals(that.variable);

    }

    @Override
    public int hashCode() {
        int result = (value != +0.0f ? Float.floatToIntBits(value) : 0);
        result = 31 * result + variable.hashCode();
        result = 31 * result + operator.hashCode();
        return result;
    }
}
