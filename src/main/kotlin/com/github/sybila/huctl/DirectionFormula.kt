package com.github.sybila.huctl


sealed class DirectionFormula {

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

    object True : DirectionFormula(), Atom {
        override fun toString(): String = "true"
    }

    object False : DirectionFormula(), Atom {
        override fun toString(): String = "false"
    }

    object Loop : DirectionFormula(), Atom {
        override fun toString(): String = "loop"
    }

    data class Proposition(val name: String, val facet: Facet) : DirectionFormula(), Atom {
        override fun toString(): String = "$name$facet"
    }

    internal data class Reference(val name: String) : DirectionFormula(), Atom {
        override fun toString(): String = name
    }

    data class Not(override val inner: DirectionFormula) : DirectionFormula(), Unary<Not> {
        override fun toString(): String = "!$inner"
    }

    data class And(
            override val left: DirectionFormula, override val right: DirectionFormula
    ) : DirectionFormula(), Binary<And> {
        override fun toString(): String = "($left && $right)"
    }

    data class Or(
            override val left: DirectionFormula, override val right: DirectionFormula
    ) : DirectionFormula(), Binary<And> {
        override fun toString(): String = "($left || $right)"
    }

    data class Implies(
            override val left: DirectionFormula, override val right: DirectionFormula
    ) : DirectionFormula(), Binary<And> {
        override fun toString(): String = "($left -> $right)"
    }

    data class Equals(
            override val left: DirectionFormula, override val right: DirectionFormula
    ) : DirectionFormula(), Binary<And> {
        override fun toString(): String = "($left <-> $right)"
    }
}