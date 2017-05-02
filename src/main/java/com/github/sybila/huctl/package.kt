package com.github.sybila.huctl

import com.github.sybila.Unary

/**
 * Specifies the type of the temporal path quantifier used by the HUCTLp [Formula].
 */
enum class PathQuantifier {
    /** All future paths */ A,
    /** Exists future path */ E,
    /** All past paths */ pA,
    /** Exists past path */ pE;
}

/**
 * Specifies the type of the comparison operator used by the [Formula.Numeric] propositions.
 */
enum class CompareOp(private val str: String) {
    /** Greater than (>) */ GT(">"),
    /** Greater than or equal (>=) */ GE(">="),
    /** Equal (==) */ EQ("=="),
    /** Not equal (!=) */ NEQ("!="),
    /** Less than or equal (<=) */ LE("<="),
    /** Less than (<) */ LT("<");

    /**
     * Return a string representation which can be parsed to create an equivalent object.
     */
    override fun toString(): String = str
}

/**
 * Specifies the type of transition between two states.
 */
enum class Flow(private val str: String) {
    /** Incoming transition */ IN("in"),
    /** Outgoing transition */ OUT("out");

    /**
     * Return a string representation which can be parsed to create an equivalent object.
     */
    override fun toString(): String = str
}

/**
 * Specifies the direction in which one moves in the transition system.
 */
enum class Direction(private val str: String) {
    /** Increasing (n -> n+1) */ POSITIVE("+"),
    /** Decreasing (n -> n-1) */ NEGATIVE("-");

    /**
     * Return a string representation which can be parsed to create an equivalent object.
     */
    override fun toString(): String = str
}

/**
 * Common interface for temporal [Formula]s.
 */
interface Temporal<out This : Formula> : Unary<This, Formula> {

    /**
     * Specifies the condition for selecting paths originating in the inspected state.
     */
    val quantifier: PathQuantifier

    /**
     * Specifies the direction requirement expected from the selected paths.
     */
    val direction: DirFormula

    /**
     * Create a copy of the original object, but optionally replace some of the elements.
     */
    fun copy(
            quantifier: PathQuantifier = this.quantifier,
            inner: Formula = this.inner,
            direction: DirFormula = this.direction
    ) : This
}