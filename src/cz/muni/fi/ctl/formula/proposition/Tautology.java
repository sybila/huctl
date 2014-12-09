package cz.muni.fi.ctl.formula.proposition;

import org.jetbrains.annotations.NotNull;

public class Tautology extends Proposition<Object> {

    @Override
    public boolean evaluate(Object value) {
        return true;
    }

    @NotNull
    @Override
    public String toString() {
        return "True";
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Tautology;
    }
}
