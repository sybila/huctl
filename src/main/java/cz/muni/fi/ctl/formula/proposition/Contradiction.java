package cz.muni.fi.ctl.formula.proposition;

import org.jetbrains.annotations.NotNull;

public class Contradiction extends Proposition<Object> {

    public static final Contradiction INSTANCE = new Contradiction();

    private Contradiction() {}

    @Override
    public boolean evaluate(Object value) {
        return false;
    }

    @NotNull
    @Override
    public String toString() {
        return "False";
    }

}
