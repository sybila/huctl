package com.github.sybila.huctl

import com.github.sybila.Binary
import com.github.sybila.TreeNode
import com.github.sybila.Unary

/**
 * An HUCTLp Formula that can be used to describe general temporal properties.
 *
 * It supports [Transition] and [Numeric] propositions together with standard true/false literals and boolean logic.
 *
 * Furthermore, it supports the basic temporal operators: [Next] (X), [Future] (F), [Globally] (G) and [Until] (U),
 * plus the special weak operators ([WeakFuture] and [WeakNext]) together with path quantification
 * (see [PathQuantifier]) and optional direction modifiers (see [DirFormula]).
 *
 * Finally, it also supports first order ([ForAll] and [Exists]) and hybrid ([Bind] and [At]) operators.
 *
 * Semantically, an HUCTLp formula is evaluated at a state of a transition system. For full description
 * of the semantics, see the project readme.
 *
 * Note that each formula is either [Binary], [Unary] or an atomic proposition.
 */
sealed class Formula(
        protected val string: String
) : TreeNode<Formula> {

    /* ========== Atoms ========== */

    /**
     * Logical tautology. Any state satisfies this formula.
     */
    object True : Formula("true") {
        override fun toString(): String = string
    }

    /**
     * Logical contradiction. No state satisfies this formula.
     */
    object False : Formula("false") {
        override fun toString(): String = string
    }

    /**
     * The Reference proposition represents a special predicate "current state is the
     * state referenced by [name]" which can be (together with hybrid operators) used
     * to define things like cycles (for example `bind x : EX EF x` describes a cycle).
     */
    data class Reference(
            /** A unique name of the data referenced by this object */
            val name: String
    ) : Formula(name) {
        override fun toString(): String = string
    }

    /**
     * Transition proposition. A state satisfies this proposition if there is a transition
     * in the specified direction (up/down) and flow in/out of the state.
     */
    data class Transition(
            /** The name of the variable which should perform the transition */
            val name: String,
            /** The direction (up/down) of the variable change (relative to this state) */
            val direction: Direction,
            /** The flow in the specified direction (in/out) */
            val flow: Flow)
        : Formula("$name:$flow$direction") {
        override fun toString(): String = string
    }

    /**
     * Numeric proposition. The semantics of this proposition are not strictly defined.
     * However, usually each state maps to a certain point/area in a cartesian space. In
     * this case, the proposition outlines an area of valid states.
     */
    data class Numeric(
            /** Left side of the comparison */
            val left: Expression,
            /** Comparison operator */
            val cmp: CompareOp,
            /** Right side of the comparison */
            val right: Expression)
        : Formula("($left $cmp $right)") {
        override fun toString(): String = string
    }

    /**
     * A general purpose proposition that can contain any string value.
     */
    data class Text(
            /** The text value of this proposition. */
            val value: String
    ) : Formula("\"$value\"") {
        override fun toString(): String = string
    }

    /* ========== Hybrid ========== */

    /**
     * First-order forall operator. Specifies that for all states in [bound] substituted for [name], the
     * [target] formula is valid at inspected state.
     *
     * Note that `(forall x in A : B)` is semantically equivalent to `(forall x : ((at x: A) -> B))`.
     * However, we allow the `in` syntax because it is much more readable and can yield interesting
     * performance optimisations.
     *
     * Warning: [name] must usually be a free name (not assigned yet).
     */
    data class ForAll(
            /** The name that should be substituted in the [target] formula. */
            val name: String,
            /** The bound formula which limits the states considered for substitution. */
            val bound: Formula,
            /** The target formula which is subject to substitution. */
            val target: Formula)
        : Formula("(forall $name in $bound : $target)"), Binary<ForAll, Formula> {
        override val left: Formula = bound; override val right: Formula = target
        override fun copy(left: Formula, right: Formula): ForAll = this.copy(bound = left, target = right)
        override fun toString(): String = string
    }

    /**
     * First-order exists operator. Specifies that there exists a state in [bound], such that when substituted
     * for [name], the [target] formula is valid at the inspected state.
     *
     * Note that `(exists x in A : B)` is semantically equivalent to `(exists x : ((at x: A) && B))`.
     * However, we allow the `in` syntax because it is much more readable and can yield interesting
     * performance optimisations.
     *
     * Warning: [name] must usually be a free name (not assigned yet).
     */
    data class Exists(
            /** The name that should be substituted in the [target] formula. */
            val name: String,
            /** The bound formula which limits the states considered for substitution */
            val bound: Formula,
            /** The target formula which is subject to substitution */
            val target: Formula)
        : Formula("(exists $name in $bound : $target)"), Binary<Exists, Formula> {
        override val left: Formula = bound; override val right: Formula = target
        override fun copy(left: Formula, right: Formula): Exists = this.copy(bound = left, target = right)
        override fun toString(): String = string
    }

    /**
     * Hybrid operator which specifies that at the inspected state, the [target] formula holds
     * with [name] substituted for the inspected state.
     *
     * Warning: [name] must usually be a free name (not assigned yet).
     */
    data class Bind(
            /** The name that should be substituted in the [target] formula */
            val name: String,
            /** The target formula which is subject to substitution */
            val target: Formula)
        : Formula("(bind $name : $target)"), Unary<Bind, Formula> {
        override val inner: Formula = target
        override fun copy(inner: Formula): Bind = this.copy(target = inner)
        override fun toString(): String = string
    }

    /**
     * At operator specifies that a formula holds at the state with the given [name].
     *
     * Warning: [name] must be a bound name (assigned).
     */
    data class At(
            /** The name of the state that should be considered as a new point of interest */
            val name: String,
            /** The formula which should be inspected at the new point of interest */
            val target: Formula)
        : Formula("(at $name : $target)"), Unary<At, Formula> {
        override val inner: Formula = target
        override fun copy(inner: Formula): At = this.copy(target = inner)
        override fun toString(): String = string
    }

    /* ========== Boolean ========== */

    /**
     * Logical negation. A state satisfies this formula if it does not satisfy [inner].
     */
    data class Not(override val inner: Formula) : Formula("!$inner"), Unary<Not, Formula> {
        override fun toString(): String = string
    }

    /**
     * Logical conjunction. A state satisfies this formula if it satisfies both [left] and [right].
     */
    data class And(override val left: Formula, override val right: Formula)
        : Formula("($left && $right)"), Binary<And, Formula> {
        override fun toString(): String = string
    }

    /**
     * Logical disjunction. A state satisfies this formula if it satisfies any of the [left] and [right] formulas.
     */
    data class Or(override val left: Formula, override val right: Formula)
        : Formula("($left && $right)"), Binary<Or, Formula> {
        override fun toString(): String = string
    }

    /**
     * Logical implication. A state satisfies this formula if it does not satisfy [left] or if it
     * satisfies both [left] and [right].
     */
    data class Implies(override val left: Formula, override val right: Formula)
        : Formula("($left -> $right)"), Binary<Implies, Formula> {
        override fun toString(): String = string
    }

    /**
     * Logical equivalence. A state satisfies this formula if it does not satisfy neither [left] nor [right] or
     * if it satisfies both.
     */
    data class Equals(override val left: Formula, override val right: Formula)
        : Formula("($left <-> $right)"), Binary<Equals, Formula> {
        override fun toString(): String = string
    }

    /* ========== Temporal, Simple ========== */

    /**
     * Temporal next (X) operator. A state satisfies next, if direct successors specified by the [quantifier]
     * match the [direction] and satisfy the [inner] formula.
     *
     * Note that for the [PathQuantifier.A] and [PathQuantifier.pA] this means that all paths
     * must satisfy the [direction]. See [WeakNext] for less strict alternative.
     */
    data class Next(
            override val quantifier: PathQuantifier, override val inner: Formula, override val direction: DirFormula
    ) : Formula("({$direction}${quantifier}X $inner)"), Temporal<Next> {
        override fun copy(inner: Formula): Next = this.copy(inner = inner)
        override fun toString(): String = string
    }

    /**
     * Temporal future (F) operator. A state satisfies future if the paths specified by the [quantifier]
     * match the [direction] and eventually satisfy the [inner] formula.
     *
     * Note that for the [PathQuantifier.A] and [PathQuantifier.pA] this means that all paths
     * must satisfy the [direction]. See [WeakFuture] for less strict alternative.
     */
    data class Future(
            override val quantifier: PathQuantifier, override val inner: Formula, override val direction: DirFormula
    ) : Formula("({$direction}${quantifier}F $inner)"), Temporal<Future> {
        override fun copy(inner: Formula): Future = this.copy(inner = inner)
        override fun toString(): String = string
    }

    /**
     * Temporal globally (G) operator. A state satisfies globally if the paths specified by the [quantifier]
     * match the [direction] and always satisfy the [inner] formula.
     */
    data class Globally(
            override val quantifier: PathQuantifier, override val inner: Formula, override val direction: DirFormula
    ) : Formula("({$direction}${quantifier}G $inner)"), Temporal<Globally> {
        override fun copy(inner: Formula): Globally = this.copy(inner = inner)
        override fun toString(): String = string
    }

    /**
     * Weak temporal next (wX) operator. A state satisfies weak next when all successors
     * (specified by the [quantifier]) that match the [direction] also satisfy the [inner] formula.
     *
     * Note that this is essentially equivalent to [Next], but instead of conjunction, the direction requirement is
     * an implication.
     *
     * When [direction] is [DirFormula.True], [WeakNext] and [Next] are equivalent.
     */
    data class WeakNext(
            override val quantifier: PathQuantifier, override val inner: Formula, override val direction: DirFormula
    ) : Formula("({$direction}${quantifier}wX $inner)"), Temporal<WeakNext> {
        override fun copy(inner: Formula): WeakNext = this.copy(inner = inner)
        override fun toString(): String = string
    }

    /**
     * Weak temporal future (wF) operator. A state satisfying weak future when all paths
     * (specified by the [quantifier]) that match the [direction] also satisfy the [inner] formula.
     *
     * Note that this is essentially equivalent to [Future], but instead of conjunction, the direction requirement is
     * an implication.
     *
     * When [direction] is [DirFormula.True], [WeakFuture] and [Future] are equivalent.
     */
    data class WeakFuture(
            override val quantifier: PathQuantifier, override val inner: Formula, override val direction: DirFormula
    ) : Formula("({$direction}${quantifier}wF $inner)"), Temporal<WeakFuture> {
        override fun copy(inner: Formula): WeakFuture = this.copy(inner = inner)
        override fun toString(): String = string
    }

    /* ========== Temporal, Until ========== */

    /**
     * Temporal until (U) operator. A state satisfies until if the paths specified by the [quantifier]
     * match the [direction] and always satisfy the [path] formula until the eventually, the [reach]
     * formula is satisfied.
     */
    data class Until(
            /** Specifies the condition for selecting paths originating in the inspected state. */
            val quantifier: PathQuantifier,
            /** The formula which needs to be valid along a path until reach is found. */
            val path: Formula,
            /** The formula which needs to be eventually found in the path. */
            val reach: Formula,
            /** Specifies the direction requirement expected from the selected paths. */
            val direction: DirFormula
    ) : Formula("($path {$direction}${quantifier}U $reach)"), Binary<Until, Formula> {
        override val left: Formula = path ; override val right: Formula = reach
        override fun copy(left: Formula, right: Formula): Until = this.copy(path = left, reach = right)
        override fun toString(): String = string
    }

    /**
     * Return string which uniquely represents this formula and can be parsed to create an equivalent object.
     */
    override fun toString(): String = string

    /**
     * Formula is also the node in the tree, so `node == this`
     */
    override val node: Formula
        get() = this

}