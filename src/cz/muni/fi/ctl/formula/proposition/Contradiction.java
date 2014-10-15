package cz.muni.fi.ctl.formula.proposition;

public class Contradiction extends Proposition<Object> {

    @Override
    public boolean evaluate(Object value) {
        return false;
    }

    @Override
    public String toString() {
        return "False";
    }
}
