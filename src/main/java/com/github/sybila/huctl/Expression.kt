package com.github.sybila.huctl

import com.github.sybila.Binary
import com.github.sybila.TreeNode

/**
 * Arithmetic expressions which are used in the [Formula.Numeric] proposition.
 *
 * They support standard arithmetic operations (+, -, *, /), floating point
 * literals and variables. Note that any unresolved variables are expected to
 * be resolved "at runtime", meaning they are considered to be a part of the model.
 *
 * Note that each expressions is either [Binary] or a constant or a variable.
 */
sealed class Expression(
        private val string: String
) : TreeNode<Expression> {

    /**
     * A variable [name]. If there is an existing alias with this name, it will be substituted.
     * Otherwise is considered to be a model variable.
     */
    data class Variable(
            /** The name of the variable whose value should be substitued at this position. */
            val name: String
    ) : Expression(name)

    /**
     * A floating point constant. Scientific notation (2.3e10) is not supported.
     */
    data class Constant(
            /** The value of the floating point constant. */
            val value: Double
    ) : Expression(String.format("%.6f", value))   // avoid scientific notation

    /**
     * Addition: A + B
     */
    data class Add(
            override val left: Expression, override val right: Expression
    ) : Expression("($left + $right)"), Binary<Add, Expression>

    /**
     * Subtraction: A - B
     */
    data class Sub(
            override val left: Expression, override val right: Expression
    ) : Expression("($left - $right)"), Binary<Sub, Expression>

    /**
     * Multiplication: A * B
     */
    data class Mul(
            override val left: Expression, override val right: Expression
    ) : Expression("$left * $right"), Binary<Mul, Expression>

    /**
     * Division: A / B
     */
    data class Div(
            override val left: Expression, override val right: Expression
    ) : Expression("($left / $right)"), Binary<Div, Expression>

    /**
     * Return string which uniquely represents this expression and can be parsed to create an equivalent object.
     */
    override fun toString(): String = string

    /**
     * Expression is also the node in the tree, so `node == this`
     */
    override val node: Expression
        get() = this

}