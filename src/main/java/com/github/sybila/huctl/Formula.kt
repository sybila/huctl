package com.github.sybila.huctl

/**
 * A
 */
sealed class Formula(
        private val string: String
) {

    internal open fun asDirFormula(): DirFormula? = null

    /* ========== Atoms ========== */

    object True : Formula("true") {
        override fun asDirFormula(): DirFormula? = DirFormula.True
    }

    object False : Formula("false") {
        override fun asDirFormula(): DirFormula? = DirFormula.False
    }

    data class Reference(val name: String) : Formula(name) {
        override fun asDirFormula(): DirFormula? = DirFormula.Reference(name)
    }

    data class Transition(val name: String, val direction: Direction, val flow: Flow)
        : Formula("$name:$flow$direction")

    data class Numeric(val left: Expression, val cmp: CompareOp, val right: Expression)
        : Formula("($left $cmp $right)")

    /* ========== Hybrid ========== */

    data class ForAll(val name: String, val bound: Formula, val target: Formula)
        : Formula("(forall $name in $bound : $target)"), Binary<ForAll, Formula> {
        override val left: Formula = bound; override val right: Formula = target
        override fun copy(left: Formula, right: Formula): ForAll = this.copy(bound = left, target = right)
    }

    data class Exists(val name: String, val bound: Formula, val target: Formula)
        : Formula("(exists $name in $bound : $target)"), Binary<Exists, Formula> {
        override val left: Formula = bound; override val right: Formula = target
        override fun copy(left: Formula, right: Formula): Exists = this.copy(bound = left, target = right)
    }

    data class Bind(val name: String, val target: Formula)
        : Formula("(bind $name : $target)"), Unary<Bind, Formula> {
        override val inner: Formula = target
        override fun copy(inner: Formula): Bind = this.copy(target = inner)
    }

    data class At(val name: String, val target: Formula)
        : Formula("(at $name  $target)"), Unary<At, Formula> {
        override val inner: Formula = target
        override fun copy(inner: Formula): At = this.copy(target = inner)
    }

    /* ========== Boolean ========== */

    data class Not(override val inner: Formula) : Formula("!$inner"), Unary<Not, Formula>

    data class And(override val left: Formula, override val right: Formula)
        : Formula("($left && $right)"), Binary<And, Formula> {
        override fun asDirFormula(): DirFormula? = this.directionFold(DirFormula::And)
    }

    data class Or(override val left: Formula, override val right: Formula)
        : Formula("($left && $right)"), Binary<Or, Formula> {
        override fun asDirFormula(): DirFormula? = this.directionFold(DirFormula::Or)
    }

    data class Implies(override val left: Formula, override val right: Formula)
        : Formula("($left -> $right)"), Binary<Implies, Formula> {
        override fun asDirFormula(): DirFormula? = this.directionFold(DirFormula::Implies)
    }

    data class Equals(override val left: Formula, override val right: Formula)
        : Formula("($left <-> $right)"), Binary<Equals, Formula> {
        override fun asDirFormula(): DirFormula? = this.directionFold(DirFormula::Equals)
    }

    /* ========== Temporal, Simple ========== */

    data class Next(
            val quantifier: PathQuantifier, override val inner: Formula, val direction: DirFormula
    ) : Formula("({$direction}${quantifier}X $inner)"), Unary<Next, Formula> {
        override fun copy(inner: Formula): Next = this.copy(inner = inner)
    }

    data class Future(
            val quantifier: PathQuantifier, override val inner: Formula, val direction: DirFormula
    ) : Formula("({$direction}${quantifier}F $inner)"), Unary<Future, Formula> {
        override fun copy(inner: Formula): Future = this.copy(inner = inner)
    }

    data class Globally(
            val quantifier: PathQuantifier, override val inner: Formula, val direction: DirFormula
    ) : Formula("({$direction}${quantifier}G $inner)"), Unary<Globally, Formula> {
        override fun copy(inner: Formula): Globally = this.copy(inner = inner)
    }

    data class WeakNext(
            val quantifier: PathQuantifier, override val inner: Formula, val direction: DirFormula
    ) : Formula("({$direction}${quantifier}wX $inner)"), Unary<WeakNext, Formula> {
        override fun copy(inner: Formula): WeakNext = this.copy(inner = inner)
    }

    data class WeakFuture(
            val quantifier: PathQuantifier, override val inner: Formula, val direction: DirFormula
    ) : Formula("({$direction}${quantifier}wF $inner)"), Unary<WeakFuture, Formula> {
        override fun copy(inner: Formula): WeakFuture = this.copy(inner = inner)
    }

    /* ========== Temporal, Until ========== */

    data class Until(
            val quantifier: PathQuantifier,
            val path: Formula,
            val reach: Formula,
            val direction: DirFormula
    ) : Formula("($path {$direction}${quantifier}U $reach)"), Binary<Until, Formula> {
        override val left: Formula = path ; override val right: Formula = reach
        override fun copy(left: Formula, right: Formula): Until = this.copy(path = left, reach = right)
    }

    override fun toString(): String = string

}