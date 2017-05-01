package com.github.sybila.huctl


sealed class DirectionFormula(
        private val string: String
) {

    interface Atom

    interface Unary<T> where T : DirectionFormula, T : Unary<T> {
        val inner: DirectionFormula
        fun copy(inner: DirectionFormula = this.inner): T
    }

    interface Binary<T> where T : DirectionFormula, T : Binary<T> {
        val left: DirectionFormula
        val right: DirectionFormula
        fun copy(left: DirectionFormula = this.left, right: DirectionFormula = this.right): T
    }

    object True : DirectionFormula("true"), Atom

    object False : DirectionFormula("false"), Atom

    object Loop : DirectionFormula("loop"), Atom

    data class Proposition(val name: String, val facet: Facet) : DirectionFormula("$name$facet"), Atom

    internal data class Reference(val name: String) : DirectionFormula(name), Atom

    data class Not(override val inner: DirectionFormula) : DirectionFormula("!$inner"), Unary<Not>

    data class And(
            override val left: DirectionFormula, override val right: DirectionFormula
    ) : DirectionFormula("($left && $right)"), Binary<And>

    data class Or(
            override val left: DirectionFormula, override val right: DirectionFormula
    ) : DirectionFormula("($left || $right)"), Binary<Or>

    data class Implies(
            override val left: DirectionFormula, override val right: DirectionFormula
    ) : DirectionFormula("($left -> $right)"), Binary<Implies>

    data class Equals(
            override val left: DirectionFormula, override val right: DirectionFormula
    ) : DirectionFormula("($left <-> $right)"), Binary<Equals>

    override fun toString(): String = string
}