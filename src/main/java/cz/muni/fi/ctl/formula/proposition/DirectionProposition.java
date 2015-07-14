package cz.muni.fi.ctl.formula.proposition;

import org.jetbrains.annotations.NotNull;

public class DirectionProposition extends Proposition {

    public enum Direction { IN, OUT }
    public enum Facet { POSITIVE, NEGATIVE }

    @NotNull
    private final String variable;
    @NotNull
    private final Direction direction;
    @NotNull
    private final Facet facet;

    public DirectionProposition(@NotNull String variable, @NotNull Direction direction, @NotNull Facet facet) {
        this.variable = variable;
        this.direction = direction;
        this.facet = facet;
    }

    @NotNull
    public String getVariable() {
        return variable;
    }

    @NotNull
    public Direction getDirection() {
        return direction;
    }

    @NotNull
    public Facet getFacet() {
        return facet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DirectionProposition)) return false;

        DirectionProposition that = (DirectionProposition) o;

        if (!variable.equals(that.variable)) return false;
        if (direction != that.direction) return false;
        return facet == that.facet;

    }

    @Override
    public int hashCode() {
        int result = variable.hashCode();
        result = 31 * result + direction.hashCode();
        result = 31 * result + facet.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return
                variable + ":" +
                (direction == Direction.IN ? "in" : "out") +
                (facet == Facet.POSITIVE ? "+" : "-");
    }
}
