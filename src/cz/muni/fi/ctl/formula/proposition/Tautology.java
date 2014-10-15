package cz.muni.fi.ctl.formula.proposition;

public class Tautology extends Proposition<Object> {

    @Override
    public boolean evaluate(Object value) {
        return true;
    }

    @Override
    public String toString() {
        return "True";
    }
}
