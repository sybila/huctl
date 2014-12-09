package cz.muni.fi.ctl.formula.proposition;

import org.jetbrains.annotations.NotNull;

public class Contradiction extends Proposition<Object> {

    @Override
    public boolean evaluate(Object value) {
        return false;
    }

    @NotNull
    @Override
    public String toString() {
        return "False";
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Contradiction;
    }
}
