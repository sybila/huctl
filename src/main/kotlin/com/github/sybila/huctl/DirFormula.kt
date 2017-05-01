package com.github.sybila.huctl


sealed class DirFormula(
        private val string: String
) {

    interface Atom

    interface Unary<T> where T : DirFormula, T : Unary<T> {
        val inner: DirFormula
        fun copy(inner: DirFormula = this.inner): T
    }

    interface Binary<T> where T : DirFormula, T : Binary<T> {
        val left: DirFormula
        val right: DirFormula
        fun copy(left: DirFormula = this.left, right: DirFormula = this.right): T
    }

    object True : DirFormula("true"), Atom

    object False : DirFormula("false"), Atom

    object Loop : DirFormula("loop"), Atom

    data class Proposition(val name: String, val facet: Facet) : DirFormula("$name$facet"), Atom

    internal data class Reference(val name: String) : DirFormula(name), Atom

    data class Not(override val inner: DirFormula) : DirFormula("!$inner"), Unary<Not>

    data class And(
            override val left: DirFormula, override val right: DirFormula
    ) : DirFormula("($left && $right)"), Binary<And>

    data class Or(
            override val left: DirFormula, override val right: DirFormula
    ) : DirFormula("($left || $right)"), Binary<Or>

    data class Implies(
            override val left: DirFormula, override val right: DirFormula
    ) : DirFormula("($left -> $right)"), Binary<Implies>

    data class Equals(
            override val left: DirFormula, override val right: DirFormula
    ) : DirFormula("($left <-> $right)"), Binary<Equals>

    override fun toString(): String = string
}