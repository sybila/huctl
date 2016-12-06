package com.github.sybila.ctl

//Note: Sealed class structure is a preparation for HUCTL, where operators are much more diverse.

sealed class Formula : () -> Formula {
    class Atom(val proposition: Proposition) : Formula() {
        override fun equals(other: Any?): Boolean = other is Atom && other.proposition == this.proposition
        override fun hashCode(): Int = this.proposition.hashCode()
        override fun toString(): String = this.proposition.toString()
    }
    sealed class Binary<T: Binary<T>>(
            val left: Formula,
            val right: Formula,
            private val clazz: Class<T>,
            private val op: String
    ) : Formula() {
        override fun equals(other: Any?): Boolean
                = clazz.isInstance(other) && clazz.cast(other).let { that ->
            this.left == that.left && this.right == that.right
        }
        override fun hashCode(): Int = 31 * (31 * clazz.hashCode() + left.hashCode()) + right.hashCode()
        override fun toString(): String = "($left $op $right)"

        abstract fun copy(left: Formula = this.left, right: Formula = this.right): Binary<T>

        class And(left: Formula, right: Formula) : Binary<And>(left, right, And::class.java, "&&") {
            override fun copy(left: Formula, right: Formula): Binary<And> = And(left, right)
        }
        class Or(left: Formula, right: Formula) : Binary<Or>(left, right, Or::class.java, "||") {
            override fun copy(left: Formula, right: Formula): Binary<Or> = Or(left, right)
        }
        class Implies(left: Formula, right: Formula) : Binary<Implies>(left, right, Implies::class.java, "=>") {
            override fun copy(left: Formula, right: Formula): Binary<Implies> = Implies(left, right)
        }
        class Equal(left: Formula, right: Formula) : Binary<Equal>(left, right, Equal::class.java, "<=>") {
            override fun copy(left: Formula, right: Formula): Binary<Equal> = Equal(left, right)
        }
        class EU(val path: Formula, val reach: Formula) : Binary<EU>(path, reach, EU::class.java, "EU") {
            override fun copy(left: Formula, right: Formula): Binary<EU> = EU(left, right)
        }
        class AU(val path: Formula, val reach: Formula) : Binary<AU>(path, reach, AU::class.java, "AU") {
            override fun copy(left: Formula, right: Formula): Binary<AU> = AU(left, right)
        }
    }
    sealed class Unary<T: Unary<T>>(
            val inner: Formula,
            private val clazz: Class<T>,
            private val op: String
    ) : Formula() {
        override fun equals(other: Any?): Boolean
                = clazz.isInstance(other) && clazz.cast(other).let { that -> this.inner == that.inner }
        override fun hashCode(): Int = 31 * clazz.hashCode() + inner.hashCode()
        override fun toString(): String = "$op $inner"

        abstract fun copy(inner: Formula = this.inner): Unary<T>

        class Not(inner: Formula) : Unary<Not>(inner, Not::class.java, "!") {
            override fun copy(inner: Formula): Unary<Not> = Not(inner)
        }
        class EF(inner: Formula) : Unary<EF>(inner, EF::class.java, "EF") {
            override fun copy(inner: Formula): Unary<EF> = EF(inner)
        }
        class AF(inner: Formula) : Unary<AF>(inner, AF::class.java, "AF") {
            override fun copy(inner: Formula): Unary<AF> = AF(inner)
        }
        class EG(inner: Formula) : Unary<EG>(inner, EG::class.java, "AG") {
            override fun copy(inner: Formula): Unary<EG> = EG(inner)
        }
        class AG(inner: Formula) : Unary<AG>(inner, AG::class.java, "EG") {
            override fun copy(inner: Formula): Unary<AG> = AG(inner)
        }
        class EX(inner: Formula) : Unary<EX>(inner, EX::class.java, "EX") {
            override fun copy(inner: Formula): Unary<EX> = EX(inner)
        }
        class AX(inner: Formula) : Unary<AX>(inner, AX::class.java, "AX") {
            override fun copy(inner: Formula): Unary<AX> = AX(inner)
        }
    }
    override fun invoke(): Formula = this
}