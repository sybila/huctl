package com.github.sybila.huctl

import com.github.sybila.Binary
import com.github.sybila.TreeNode
import com.github.sybila.Unary

/**
 * Formulas that are used to describe the direction of the temporal path.
 *
 * They support basic boolean logic, direction propositions ("var+", "var-", etc.) and
 * a special "loop" predicate, which indicates a self-loop transition.
 *
 * Note that each formula is either [Binary], [Unary] or an atomic proposition.
 */
sealed class DirFormula(
        internal val string: String
) : TreeNode<DirFormula> {

    /**
     * Logical tautology - any path will match this restriction.
     */
    object True : DirFormula("true") {
        override fun toString(): String = string
    }

    /**
     * Logical contradiction - no path will match this restriction.
     */
    object False : DirFormula("false") {
        override fun toString(): String = string
    }

    /**
     * Special loop proposition - only a self-loop path matches this restriction.
     */
    object Loop : DirFormula("loop"){
        override fun toString(): String = string
    }

    /**
     * General direction proposition. Contains a variable [name] and a requested [direction]
     * (increase/up or decrease/down).
     */
    data class Proposition(
            /** The name of the variable in which the direction should hold. */
            val name: String,
            /** The direction in which the path should be moving (up/down). */
            val direction: Direction
    ) : DirFormula("$name$direction") {
        override fun toString(): String = string
    }

    /**
     * A general purpose proposition that can contain any string value.
     */
    data class Text(
            /** The text value of this proposition. */
            val value: String
    ) : DirFormula("\"$value\"") {
        override fun toString(): String = string
    }

    // Used for alias resolution
    internal data class Reference(val name: String) : DirFormula(name) {
        override fun toString(): String = string
    }

    /**
     * Logical negation. A path will match this restriction if it does not match the [inner] restriction.
     */
    data class Not(override val inner: DirFormula) : DirFormula("!$inner"), Unary<Not, DirFormula> {
        override fun toString(): String = string
    }

    /**
     * Logical conjunction. A path will match this restriction only if it matches both [left] and [right]
     * restrictions.
     */
    data class And(
            override val left: DirFormula, override val right: DirFormula
    ) : DirFormula("($left && $right)"), Binary<And, DirFormula> {
        override fun toString(): String = string
    }

    /**
     * Logical disjunction. A path will match this restriction only if it matches any of the [left] and [right]
     * restrictions.
     */
    data class Or(
            override val left: DirFormula, override val right: DirFormula
    ) : DirFormula("($left || $right)"), Binary<Or, DirFormula> {
        override fun toString(): String = string
    }

    /**
     * Logical implication. A path will match this restriction only if does not match the [left] restriction
     * or matches both [left] and [right] restriction.
     */
    data class Implies(
            override val left: DirFormula, override val right: DirFormula
    ) : DirFormula("($left -> $right)"), Binary<Implies, DirFormula> {
        override fun toString(): String = string
    }

    /**
     * Logical equivalence. A path will match this restriction either when it matches both [left] and [right]
     * restriction or none of them.
     */
    data class Equals(
            override val left: DirFormula, override val right: DirFormula
    ) : DirFormula("($left <-> $right)"), Binary<Equals, DirFormula> {
        override fun toString(): String = string
    }

    /**
     * Return string which uniquely represents this formula and can be parsed to create an equivalent object.
     */
    override fun toString(): String = string

    /**
     * DirFormula is also the node in the tree, so `node == this`
     */
    override val node: DirFormula
        get() = this

}