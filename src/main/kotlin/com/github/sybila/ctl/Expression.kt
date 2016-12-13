package com.github.sybila.ctl

sealed class Expression {
    class Variable(val name: String) : Expression() {
        override fun equals(other: Any?): Boolean = other is Variable && other.name == this.name
        override fun hashCode(): Int = this.name.hashCode()
        override fun toString(): String = this.name
    }
    class Constant(val value: Double) : Expression() {
        override fun equals(other: Any?): Boolean = other is Constant && other.value == this.value
        override fun hashCode(): Int = this.value.hashCode()
        override fun toString(): String = String.format("%.4f", this.value) //no scientific notation
    }
    sealed class Arithmetic<T: Arithmetic<T>>(
            val left: Expression,
            val right: Expression,
            private val clazz: Class<T>,
            private val op: String
    ) : Expression() {
        override fun equals(other: Any?): Boolean
                = clazz.isInstance(other) && clazz.cast(other).let { that ->
            this.left == that.left && this.right == that.right
        }
        override fun hashCode(): Int = 31 * (31 * clazz.hashCode() + left.hashCode()) + right.hashCode()
        override fun toString(): String = "($left $op $right)"

        abstract fun copy(left: Expression = this.left, right: Expression = this.right): Arithmetic<T>

        class Add(left: Expression, right: Expression) : Arithmetic<Add>(left, right, Add::class.java, "+") {
            override fun copy(left: Expression, right: Expression): Arithmetic<Add> = Add(left, right)
        }

        class Sub(left: Expression, right: Expression) : Arithmetic<Sub>(left, right, Sub::class.java, "-") {
            override fun copy(left: Expression, right: Expression): Arithmetic<Sub> = Sub(left, right)
        }

        class Mul(left: Expression, right: Expression) : Arithmetic<Mul>(left, right, Mul::class.java, "*") {
            override fun copy(left: Expression, right: Expression): Arithmetic<Mul> = Mul(left, right)
        }

        class Div(left: Expression, right: Expression) : Arithmetic<Div>(left, right, Div::class.java, "/") {
            override fun copy(left: Expression, right: Expression): Arithmetic<Div> = Div(left, right)
        }
    }
}