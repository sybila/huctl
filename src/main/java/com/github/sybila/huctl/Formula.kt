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

sealed class Formula {

    internal open fun asDirectionFormula(): DirFormula? = null

    interface Atom

    interface Unary<T> where T : Formula, T : Unary<T> {
        val inner: Formula
        fun copy(inner: Formula = this.inner): T
    }

    interface Binary<T> where T : Formula, T : Binary<T> {
        val left: Formula
        val right: Formula
        fun copy(left: Formula = this.left, right: Formula = this.right): T
    }

    /* ========== Atoms ========== */

    object True : Formula(), Atom {
        override fun asDirectionFormula(): DirFormula? = DirFormula.True
        override fun toString(): String = "true"
    }

    object False : Formula(), Atom {
        override fun asDirectionFormula(): DirFormula? = DirFormula.False
        override fun toString(): String = "false"
    }

    data class Reference(val name: String) : Formula(), Atom {
        override fun asDirectionFormula(): DirFormula? = DirFormula.Reference(name)
        override fun toString(): String = name
    }

    data class Transition(val name: String, val direction: Direction, val facet: Facet) : Formula(), Atom {
        override fun toString(): String = "$name:$direction$facet"
    }

    data class Numeric(val left: Expression, val cmp: CompareOp, val right: Expression) : Formula(), Atom {
        override fun toString(): String = "($left $cmp $right)"
    }

    /* ========== Hybrid ========== */

    data class ForAll(val name: String, val bound: Formula, val target: Formula) : Formula(), Binary<ForAll> {
        override val left: Formula = bound
        override val right: Formula = target
        override fun copy(left: Formula, right: Formula): ForAll = this.copy(bound = left, target = right)
        override fun toString(): String = "(forall $name in $bound : $target)"
    }

    data class Exists(val name: String, val bound: Formula, val target: Formula) : Formula(), Binary<Exists> {
        override val left: Formula = bound
        override val right: Formula = target
        override fun copy(left: Formula, right: Formula): Exists = this.copy(bound = left, target = right)
        override fun toString(): String = "(exists $name in $bound : $target)"
    }

    data class Bind(val name: String, val target: Formula) : Formula(), Unary<Bind> {
        override val inner: Formula = target
        override fun copy(inner: Formula): Bind = this.copy(target = inner)
        override fun toString(): String = "(bind $name : $target)"
    }

    data class At(val name: String, val target: Formula) : Formula(), Unary<At> {
        override val inner: Formula = target
        override fun copy(inner: Formula): At = this.copy(target = inner)
        override fun toString(): String = "(at $name : $target)"
    }

    /* ========== Boolean ========== */

    data class Not(override val inner: Formula) : Formula(), Unary<Not> {
        override fun toString(): String = "!$inner"
    }

    data class And(override val left: Formula, override val right: Formula) : Formula(), Binary<And> {
        override fun asDirectionFormula(): DirFormula? {
            return  left.asDirectionFormula()?.let { left ->
                    right.asDirectionFormula()?.let { right ->
                        left and right
            } }
        }
        override fun toString(): String = "($left && $right)"
    }

    data class Or(override val left: Formula, override val right: Formula) : Formula(), Binary<Or> {
        override fun asDirectionFormula(): DirFormula? {
            return  left.asDirectionFormula()?.let { left ->
                right.asDirectionFormula()?.let { right ->
                    left or right
                } }
        }
        override fun toString(): String = "($left || $right)"
    }

    data class Implies(override val left: Formula, override val right: Formula) : Formula(), Binary<Implies> {
        override fun asDirectionFormula(): DirFormula? {
            return  left.asDirectionFormula()?.let { left ->
                right.asDirectionFormula()?.let { right ->
                    left implies right
                } }
        }
        override fun toString(): String = "($left -> $right)"
    }

    data class Equals(override val left: Formula, override val right: Formula) : Formula(), Binary<Equals> {
        override fun asDirectionFormula(): DirFormula? {
            return  left.asDirectionFormula()?.let { left ->
                right.asDirectionFormula()?.let { right ->
                    left equal right
                } }
        }
        override fun toString(): String = "($left <-> $right)"
    }

    /* ========== Temporal, Simple ========== */

    data class Next(
            val quantifier: PathQuantifier, override val inner: Formula, val direction: DirFormula
    ) : Formula(), Unary<Next> {
        override fun copy(inner: Formula): Next = this.copy(inner = inner)
        override fun toString(): String = if (direction == DirFormula.True) {
            "${quantifier}X $inner"
        } else {
            "{$direction}${quantifier}X $inner"
        }
    }

    data class Future(
            val quantifier: PathQuantifier, override val inner: Formula, val direction: DirFormula
    ) : Formula(), Unary<Future> {
        override fun copy(inner: Formula): Future = this.copy(inner = inner)
        override fun toString(): String = if (direction == DirFormula.True) {
            "${quantifier}F $inner"
        } else {
            "{$direction}${quantifier}F $inner"
        }
    }

    data class Globally(
            val quantifier: PathQuantifier, override val inner: Formula, val direction: DirFormula
    ) : Formula(), Unary<Globally> {
        override fun copy(inner: Formula): Globally = this.copy(inner = inner)
        override fun toString(): String = if (direction == DirFormula.True) {
            "${quantifier}G $inner"
        } else {
            "{$direction}${quantifier}G $inner"
        }
    }

    data class WeakNext(
            val quantifier: PathQuantifier, override val inner: Formula, val direction: DirFormula
    ) : Formula(), Unary<WeakNext> {
        override fun copy(inner: Formula): WeakNext = this.copy(inner = inner)
        override fun toString(): String = if (direction == DirFormula.True) {
            "${quantifier}wX $inner"
        } else {
            "{$direction}${quantifier}wX $inner"
        }
    }

    data class WeakFuture(
            val quantifier: PathQuantifier, override val inner: Formula, val direction: DirFormula
    ) : Formula(), Unary<WeakFuture> {
        override fun copy(inner: Formula): WeakFuture = this.copy(inner = inner)
        override fun toString(): String = if (direction == DirFormula.True) {
            "${quantifier}wF $inner"
        } else {
            "{$direction}${quantifier}wF $inner"
        }
    }

    /* ========== Temporal, Until ========== */

    data class Until(
            val quantifier: PathQuantifier,
            val path: Formula,
            val reach: Formula,
            val direction: DirFormula
    ) : Formula(), Binary<Until> {
        override val left: Formula = path
        override val right: Formula = reach
        override fun copy(left: Formula, right: Formula): Until = this.copy(path = left, reach = right)
        override fun toString(): String = if (direction == DirFormula.True) {
            "($path ${quantifier}U $reach)"
        } else {
            "($path ${direction}${quantifier}U $reach)"
        }
    }
}