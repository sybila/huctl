package com.github.sybila.huctl

/**
 * Algebraic data type representing a HUCTL formula:
 *
 * With Kotlin 1.1, we will be able to rewrite this in a more concise manner.
 *
 * Meanwhile, here is a simple overview:
 * class Formula {
 *      interface Unary
 *      interface Binary
 *      class Atom : Formula {
 *          class True
 *          class False
 *          class Float
 *          class Transition
 *          internal class Reference
 *      }
 *      class FirstOrder : Formula {
 *          class ForAll : FirstOrder, Binary
 *          class Exists : FirstOrder, Binary
 *      }
 *      class Hybrid : Formula {
 *          class Bind : Hybrid, Unary
 *          class At : Hybrid, Unary
 *      }
 *      class Not : Formula
 *      class Bool : Formula, Binary {
 *          class And : Bool
 *          class Or : Bool
 *          class Implies : Bool
 *          class Equals : Bool
 *      }
 *      class Temporal : Formula {
 *          class Basic : Unary {
 *              class Next : Basic
 *              class Future : Basic
 *              class Globally : Basic
 *              class WeakNext : Basic
 *              class WeakFuture : Basic
 *          }
 *          class Until : Binary
 *      }
 * }
 */
sealed class Formula : () -> Formula {

    abstract fun asDirectionFormula(): DirectionFormula?

    interface Unary<T> where T : Formula, T : Unary<T> {
        val inner: Formula
        fun copy(inner: Formula = this.inner): T
    }

    interface Binary<T> where T : Formula, T : Binary<T> {
        val left: Formula
        val right: Formula
        fun copy(left: Formula = this.left, right: Formula = this.right): T
    }

    interface Temporal {
        val quantifier: PathQuantifier
        val direction: DirectionFormula
    }

    sealed class Atom : Formula() {
        object True : Atom() {
            override fun toString(): String = "true"
            override fun asDirectionFormula(): DirectionFormula? = DirectionFormula.Atom.True
        }
        object False : Atom() {
            override fun toString(): String = "false"
            override fun asDirectionFormula(): DirectionFormula? = DirectionFormula.Atom.False
        }
        class Float(val left: Expression, val cmp: CompareOp, val right: Expression) : Atom() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other?.javaClass != javaClass) return false

                other as Float

                if (left != other.left) return false
                if (cmp != other.cmp) return false
                if (right != other.right) return false

                return true
            }

            override fun hashCode(): Int {
                var result = left.hashCode()
                result = 31 * result + cmp.hashCode()
                result = 31 * result + right.hashCode()
                return result
            }

            override fun toString(): String = "($left $cmp $right)"

            fun copy(left: Expression = this.left, cmp: CompareOp = this.cmp, right: Expression = this.right): Float
                    = Float(left, cmp, right)

            override fun asDirectionFormula(): DirectionFormula? = null
        }
        class Transition(val name: String, val direction: Direction, val facet: Facet) : Atom() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other?.javaClass != javaClass) return false

                other as Transition

                if (name != other.name) return false
                if (direction != other.direction) return false

                return true
            }

            override fun hashCode(): Int {
                var result = name.hashCode()
                result = 31 * result + direction.hashCode()
                return result
            }

            override fun toString(): String = "$name:$direction$facet"
            override fun asDirectionFormula(): DirectionFormula? = null
        }
        internal class Reference(val name: String) : Atom() {
            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other?.javaClass != javaClass) return false

                other as Reference

                if (name != other.name) return false

                return true
            }

            override fun hashCode(): Int {
                return name.hashCode()
            }

            override fun toString(): String = name
            override fun asDirectionFormula(): DirectionFormula? = DirectionFormula.Atom.Reference(this.name)
        }
    }

    sealed class FirstOrder<T: FirstOrder<T>>(
            val name: String, val bound: Formula, val target: Formula, private val op: String
    ) : Formula(), Binary<T> {

        override fun toString(): String = "($op $name in $bound : $target)"

        abstract fun copy(name: String = this.name, bound: Formula = this.bound, target: Formula = this.target): T
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other?.javaClass != javaClass) return false

            other as FirstOrder<*>

            if (name != other.name) return false
            if (bound != other.bound) return false
            if (target != other.target) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + bound.hashCode()
            result = 31 * result + target.hashCode()
            return result
        }

        override val left: Formula = bound
        override val right: Formula = target
        override fun copy(left: Formula, right: Formula): T = copy(bound = left, target = right)
        override fun asDirectionFormula(): DirectionFormula? = null

        class ForAll(name: String, bound: Formula, target: Formula) : FirstOrder<ForAll>(name, bound, target, "forall") {
            override fun copy(name: String, bound: Formula, target: Formula) = ForAll(name, bound, target)
        }
        class Exists(name: String, bound: Formula, target: Formula) : FirstOrder<Exists>(name, bound, target, "exists") {
            override fun copy(name: String, bound: Formula, target: Formula): Exists = Exists(name, bound, target)
        }
    }

    sealed class Hybrid<T: Hybrid<T>>(val name: String, val target: Formula, private val op: String) : Formula(), Unary<T> {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other?.javaClass != javaClass) return false

            other as Hybrid<*>

            if (name != other.name) return false
            if (target != other.target) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + target.hashCode()
            return result
        }

        override fun toString(): String = "($op $name : $target)"

        abstract fun copy(name: String = this.name, target: Formula = this.target): T

        override val inner: Formula = target
        override fun copy(inner: Formula): T = copy(target = inner)
        override fun asDirectionFormula(): DirectionFormula? = null

        class Bind(name: String, target: Formula) : Hybrid<Bind>(name, target, "bind") {
            override fun copy(name: String, target: Formula): Bind = Bind(name, target)
        }

        class At(name: String, target: Formula) : Hybrid<At>(name, target, "at") {
            override fun copy(name: String, target: Formula): At = At(name, target)
        }
    }

    sealed class Simple<T: Simple<T>>(override val quantifier: PathQuantifier,
                                      override val inner: Formula, override val direction: DirectionFormula,
                                      private val op: String
    ) : Formula(), Temporal, Unary<T> {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other?.javaClass != javaClass) return false

            other as Simple<*>

            if (quantifier != other.quantifier) return false
            if (inner != other.inner) return false
            if (direction != other.direction) return false

            return true
        }

        override fun hashCode(): Int {
            var result = inner.hashCode()
            result = 31 * result + direction.hashCode()
            result = 31 * result + quantifier.hashCode()
            return result
        }

        override fun toString(): String = "{$direction}$quantifier$op $inner"

        abstract fun copy(quantifier: PathQuantifier = this.quantifier,
                          inner: Formula = this.inner,
                          direction: DirectionFormula = this.direction
        ) : T

        override fun copy(inner: Formula): T = copy(this.quantifier, inner, this.direction)
        override fun asDirectionFormula(): DirectionFormula? = null

        class Next(quantifier: PathQuantifier, inner: Formula, direction: DirectionFormula) : Simple<Next>(quantifier, inner, direction, "X") {
            override fun copy(quantifier: PathQuantifier, inner: Formula, direction: DirectionFormula): Next = Next(quantifier, inner, direction)
        }
        class Future(quantifier: PathQuantifier, inner: Formula, direction: DirectionFormula) : Simple<Future>(quantifier, inner, direction,"F") {
            override fun copy(quantifier: PathQuantifier, inner: Formula, direction: DirectionFormula): Future = Future(quantifier, inner, direction)
        }
        class Globally(quantifier: PathQuantifier, inner: Formula, direction: DirectionFormula) : Simple<Globally>(quantifier, inner, direction,"G") {
            override fun copy(quantifier: PathQuantifier, inner: Formula, direction: DirectionFormula): Globally = Globally(quantifier, inner, direction)
        }
        class WeakNext(quantifier: PathQuantifier, inner: Formula, direction: DirectionFormula) : Simple<Next>(quantifier, inner, direction, "wX") {
            override fun copy(quantifier: PathQuantifier, inner: Formula, direction: DirectionFormula): Next = Next(quantifier, inner, direction)
        }
        class WeakFuture(quantifier: PathQuantifier, inner: Formula, direction: DirectionFormula) : Simple<Future>(quantifier, inner, direction,"wF") {
            override fun copy(quantifier: PathQuantifier, inner: Formula, direction: DirectionFormula): Future = Future(quantifier, inner, direction)
        }
    }

    class Until(
            override val quantifier: PathQuantifier,
            val path: Formula,
            val reach: Formula,
            override val direction: DirectionFormula
    ) : Formula(), Temporal, Binary<Until> {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other?.javaClass != javaClass) return false

            other as Until

            if (quantifier != other.quantifier) return false
            if (path != other.path) return false
            if (reach != other.reach) return false
            if (direction != other.direction) return false

            return true
        }

        override fun hashCode(): Int {
            var result = path.hashCode()
            result = 31 * result + reach.hashCode()
            result = 31 * result + direction.hashCode()
            result = 31 * result + quantifier.hashCode()
            return result
        }

        override fun toString(): String = "($path {$direction}${quantifier}U $reach)"

        override val left: Formula = path
        override val right: Formula = reach
        override fun copy(left: Formula, right: Formula): Until = copy(path = left, reach = right)
        override fun asDirectionFormula(): DirectionFormula? = null

        fun copy(
                quantifier: PathQuantifier = this.quantifier,
                path: Formula = this.path,
                reach: Formula = this.path,
                direction: DirectionFormula = this.direction
        ) = Until(quantifier, path, reach, direction)

    }


    class Not(override val inner: Formula) : Formula(), Unary<Not> {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other?.javaClass != javaClass) return false

            other as Not

            if (inner != other.inner) return false

            return true
        }

        override fun hashCode(): Int {
            return inner.hashCode()
        }

        override fun toString(): String = "!$inner"

        override fun copy(inner: Formula) = Not(inner)
        override fun asDirectionFormula(): DirectionFormula? = inner.asDirectionFormula()?.let {
            DirectionFormula.Not(it)
        }
    }

    sealed class Bool<T: Bool<T>>(
            override val left: Formula, override val right: Formula,
            private val op: String
    ) : Formula(), Binary<T> {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other?.javaClass != javaClass) return false

            other as Bool<*>

            if (left != other.left) return false
            if (right != other.right) return false

            return true
        }

        override fun hashCode(): Int {
            var result = left.hashCode()
            result = 31 * result + right.hashCode()
            return result
        }

        override fun toString(): String = "($left $op $right)"

        class And(left: Formula, right: Formula) : Bool<And>(left, right, "&&") {
            override fun copy(left: Formula, right: Formula): And = And(left, right)
            override fun asDirectionFormula(): DirectionFormula?
                    =   left.asDirectionFormula()?.let { l ->
                        right.asDirectionFormula()?.let { r ->
                            DirectionFormula.Bool.And(l, r)
                        }}
        }
        class Or(left: Formula, right: Formula) : Bool<Or>(left, right, "||") {
            override fun copy(left: Formula, right: Formula): Or = Or(left, right)
            override fun asDirectionFormula(): DirectionFormula?
                    =   left.asDirectionFormula()?.let { l ->
                right.asDirectionFormula()?.let { r ->
                    DirectionFormula.Bool.Or(l, r)
                }}
        }
        class Implies(left: Formula, right: Formula) : Bool<Implies>(left, right, "->") {
            override fun copy(left: Formula, right: Formula): Implies = Implies(left, right)
            override fun asDirectionFormula(): DirectionFormula?
                    =   left.asDirectionFormula()?.let { l ->
                right.asDirectionFormula()?.let { r ->
                    DirectionFormula.Bool.Implies(l, r)
                }}
        }
        class Equals(left: Formula, right: Formula) : Bool<Equals>(left, right, "<->") {
            override fun copy(left: Formula, right: Formula): Equals = Equals(left, right)
            override fun asDirectionFormula(): DirectionFormula?
                    =   left.asDirectionFormula()?.let { l ->
                right.asDirectionFormula()?.let { r ->
                    DirectionFormula.Bool.Equals(l, r)
                }}
        }
    }

    override fun invoke(): Formula = this
}

enum class PathQuantifier {
    A, E, pA, pE;
}

enum class CompareOp(private val str: String) {
    GT(">"), GE(">="), EQ("=="), NEQ("!="), LE("<="), LT("<");

    val negation: CompareOp
            get() = when (this) {
                GT -> LE
                GE -> LT
                EQ -> NEQ
                NEQ -> EQ
                LE -> GT
                LT -> GE
            }

    override fun toString(): String = str
}

enum class Direction(private val str: String) {
    IN("in"), OUT("out");
    override fun toString(): String = str
}

enum class Facet(private val str: String) {
    POSITIVE("+"), NEGATIVE("-");
    override fun toString(): String = str
}