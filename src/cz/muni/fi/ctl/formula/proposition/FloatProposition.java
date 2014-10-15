package cz.muni.fi.ctl.formula.proposition;

public class FloatProposition extends Proposition<Float> {

    private float value;
    private String variable;
    private Operator operator;

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

    public String getVariable() {
        return variable;
    }

    public static enum Operator {
        EQ("=="), NEQ("!="), GT(">"), GT_EQ(">="), LT("<"), LT_EQ("<=");

        private String val;

        Operator(String val) {
            this.val = val;
        }

        @Override
        public String toString() {
            return val;
        }
    }

    @Override
    public String toString() {
        return variable+" "+operator+" "+value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FloatProposition)) return false;

        FloatProposition that = (FloatProposition) o;

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
