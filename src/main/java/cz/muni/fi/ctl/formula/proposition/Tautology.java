package cz.muni.fi.ctl.formula.proposition;

import org.jetbrains.annotations.NotNull;

public class Tautology extends Proposition<Object> {

    public static final Tautology INSTANCE = new Tautology();

    private Tautology() {}

    @Override
    public boolean evaluate(Object value) {
        return true;
    }

    @NotNull
    @Override
    public String toString() {
        return "True";
    }
}
