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

sealed class Proposition : () -> Formula {
    class True : Proposition() {
        override fun equals(other: Any?): Boolean = other is True
        override fun hashCode(): Int = 11
        override fun toString(): String = "true"
    }
    class False : Proposition() {
        override fun equals(other: Any?): Boolean = other is False
        override fun hashCode(): Int = 13
        override fun toString(): String = "false"
    }
    internal class Reference(val value: String) : Proposition() {
        override fun equals(other: Any?): Boolean = other is Reference && this.value == other.value
        override fun hashCode(): Int = 17 + value.hashCode()
        override fun toString(): String = value
    }
    sealed class Comparison<T: Comparison<T>>(
            val left: Expression,
            val right: Expression,
            private val clazz: Class<T>,
            private val op: String
    ) : Proposition() {
        override fun equals(other: Any?): Boolean
                = clazz.isInstance(other) && clazz.cast(other).let { that ->
            this.left == that.left && this.right == that.right
        }
        override fun hashCode(): Int = 31 * (31 * clazz.hashCode() + left.hashCode()) + right.hashCode()
        override fun toString(): String = "($left $op $right)"
        abstract fun copy(left: Expression = left, right: Expression = right): Comparison<T>
        abstract fun negate(): Comparison<*>
        class LT(left: Expression, right: Expression) : Comparison<LT>(left, right, LT::class.java, "<") {
            override fun negate(): Comparison<*> = GE(left, right)
            override fun copy(left: Expression, right: Expression): Comparison<LT> = LT(left, right)
        }
        class LE(left: Expression, right: Expression) : Comparison<LE>(left, right, LE::class.java, "<=") {
            override fun negate(): Comparison<*> = GT(left, right)
            override fun copy(left: Expression, right: Expression): Comparison<LE> = LE(left, right)
        }
        class EQ(left: Expression, right: Expression) : Comparison<EQ>(left, right, EQ::class.java, "==") {
            override fun negate(): Comparison<*> = NEQ(left, right)
            override fun copy(left: Expression, right: Expression): Comparison<EQ> = EQ(left, right)
        }
        class NEQ(left: Expression, right: Expression) : Comparison<NEQ>(left, right, NEQ::class.java, "!=") {
            override fun negate(): Comparison<*> = EQ(left, right)
            override fun copy(left: Expression, right: Expression): Comparison<NEQ> = NEQ(left, right)
        }
        class GE(left: Expression, right: Expression) : Comparison<GE>(left, right, GE::class.java, ">=") {
            override fun negate(): Comparison<*> = LT(left, right)
            override fun copy(left: Expression, right: Expression): Comparison<GE> = GE(left, right)
        }
        class GT(left: Expression, right: Expression) : Comparison<GT>(left, right, GT::class.java, ">") {
            override fun negate(): Comparison<*> = LE(left, right)
            override fun copy(left: Expression, right: Expression): Comparison<GT> = GT(left, right)
        }
    }
    class DirectionProposition(
            val variable: String,
            val direction: Direction,
            val facet: Facet
    ) : Proposition() {
        override fun equals(other: Any?): Boolean
                = other is DirectionProposition && other.variable == this.variable
                && other.direction == this.direction && other.facet == this.facet
        override fun hashCode(): Int = 31 * (31 * variable.hashCode() + direction.hashCode()) + facet.hashCode()
        override fun toString(): String = "$variable:$direction$facet"
    }
    override fun invoke(): Formula = Formula.Atom(this)
}

enum class Direction(private val str: String) {
    IN("in"), OUT("out");
    override fun toString(): String = str
}

enum class Facet(private val str: String) {
    POSITIVE("+"), NEGATIVE("-");
    override fun toString(): String = str
}