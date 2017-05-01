package com.github.sybila.huctl

sealed class Expression {

    interface Binary<T> where T : Expression, T : Binary<T> {
        val left: Expression
        val right: Expression
        fun copy(left: Expression = this.left, right: Expression = this.right): T
    }

    data class Variable(val name: String) : Expression() {
        override fun toString(): String = name
    }

    data class Constant(val value: Double) : Expression() {
        override fun toString(): String = String.format("%.4f", this.value) //no scientific notation
    }

    data class Add(
            override val left: Expression, override val right: Expression
    ) : Expression(), Binary<Add> {
        override fun toString(): String = "($left + $right)"
    }

    data class Sub(
            override val left: Expression, override val right: Expression
    ) : Expression(), Binary<Add> {
        override fun toString(): String = "($left - $right)"
    }

    data class Mul(
            override val left: Expression, override val right: Expression
    ) : Expression(), Binary<Add> {
        override fun toString(): String = "($left * $right)"
    }

    data class Div(
            override val left: Expression, override val right: Expression
    ) : Expression(), Binary<Add> {
        override fun toString(): String = "($left / $right)"
    }

}