package cz.muni.fi.ctl.formula.proposition;

import org.jetbrains.annotations.NotNull;

public class Contradiction extends Proposition {

    public static final Contradiction INSTANCE = new Contradiction();

    private Contradiction() {}

    @NotNull
    @Override
    public String toString() {
        return "False";
    }

}
