package com.github.sybila.huctl

sealed class Expression(
        private val string: String
) {

    interface Binary<T> where T : Expression, T : Binary<T> {
        val left: Expression
        val right: Expression
        fun copy(left: Expression = this.left, right: Expression = this.right): T
    }

    data class Variable(val name: String) : Expression(name)

    data class Constant(val value: Double) : Expression(String.format("%.6f", value))   // avoid scientific notation

    data class Add(
            override val left: Expression, override val right: Expression
    ) : Expression("($left + $right)"), Binary<Add>

    data class Sub(
            override val left: Expression, override val right: Expression
    ) : Expression("($left - $right)"), Binary<Add>

    data class Mul(
            override val left: Expression, override val right: Expression
    ) : Expression("$left * $right"), Binary<Add>

    data class Div(
            override val left: Expression, override val right: Expression
    ) : Expression("($left / $right)"), Binary<Add>

    override fun toString(): String = string

}