package cz.muni.fi.ctl.formula.proposition;

import org.jetbrains.annotations.NotNull;

public class Tautology extends Proposition {

    public static final Tautology INSTANCE = new Tautology();

    private Tautology() {}

    @NotNull
    @Override
    public String toString() {
        return "True";
    }
}
