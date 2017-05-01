package com.github.sybila.huctl

/**
 * Formulas that are used to describe the direction of the temporal path.
 *
 * They support basic boolean logic, direction propositions ("var+", "var-", etc.) and
 * a special "loop" predicate, which indicates a self-loop transition.
 *
 * Note that each formula is either [Binary], [Unary] or an atomic proposition.
 */
sealed class DirFormula(
        private val string: String
) {

    /**
     * Logical tautology - any path will match this restriction.
     */
    object True : DirFormula("true")

    /**
     * Logical contradiction - no path will match this restriction.
     */
    object False : DirFormula("false")

    /**
     * Special loop proposition - only a self-loop path matches this restriction.
     */
    object Loop : DirFormula("loop")

    /**
     * General direction proposition. Contains a variable [name] and a requested [direction]
     * (increase/up or decrease/down).
     */
    data class Proposition(val name: String, val direction: Direction) : DirFormula("$name$direction")

    // Used for alias resolution
    internal data class Reference(val name: String) : DirFormula(name)

    /**
     * Logical negation. A path will match this restriction if it does not match the [inner] restriction.
     */
    data class Not(override val inner: DirFormula) : DirFormula("!$inner"), Unary<Not, DirFormula>

    /**
     * Logical conjunction. A path will match this restriction only if it matches both [left] and [right]
     * restrictions.
     */
    data class And(
            override val left: DirFormula, override val right: DirFormula
    ) : DirFormula("($left && $right)"), Binary<And, DirFormula>

    /**
     * Logical disjunction. A path will match this restriction only if it matches any of the [left] and [right]
     * restrictions.
     */
    data class Or(
            override val left: DirFormula, override val right: DirFormula
    ) : DirFormula("($left || $right)"), Binary<Or, DirFormula>

    /**
     * Logical implication. A path will match this restriction only if does not match the [left] restriction
     * or matches both [left] and [right] restriction.
     */
    data class Implies(
            override val left: DirFormula, override val right: DirFormula
    ) : DirFormula("($left -> $right)"), Binary<Implies, DirFormula>

    /**
     * Logical equivalence. A path will match this restriction either when it matches both [left] and [right]
     * restriction or none of them.
     */
    data class Equals(
            override val left: DirFormula, override val right: DirFormula
    ) : DirFormula("($left <-> $right)"), Binary<Equals, DirFormula>

    override fun toString(): String = string
}