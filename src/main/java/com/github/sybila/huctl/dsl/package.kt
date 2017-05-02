package com.github.sybila.huctl.dsl

import com.github.sybila.huctl.*

/* ========== Expression ========== */

/**
 * Convert string to variable expression.
 */
fun String.toVar(): Expression.Variable = Expression.Variable(this)

/**
 * Convert double to constant expression.
 */
fun Double.toConst(): Expression.Constant = Expression.Constant(this)

/**
 * Create addition from two expressions.
 */
infix operator fun Expression.plus(other: Expression): Expression.Add = Expression.Add(this, other)

/**
 * Create subtraction from two expressions.
 */
infix operator fun Expression.minus(other: Expression): Expression.Sub = Expression.Sub(this, other)

/**
 * Create multiplication from two expressions.
 */
infix operator fun Expression.times(other: Expression): Expression.Mul = Expression.Mul(this, other)

/**
 * Create division from two expressions.
 */
infix operator fun Expression.div(other: Expression): Expression.Div = Expression.Div(this, other)

/**
 * Create [Formula.Numeric] proposition by comparing two expressions using the [CompareOp.GT].
 */
infix fun Expression.gt(other: Expression): Formula.Numeric = Formula.Numeric(this, CompareOp.GT, other)

/**
 * Create [Formula.Numeric] proposition by comparing two expressions using the [CompareOp.GE].
 */
infix fun Expression.ge(other: Expression): Formula.Numeric = Formula.Numeric(this, CompareOp.GE, other)

/**
 * Create [Formula.Numeric] proposition by comparing two expressions using the [CompareOp.EQ].
 */
infix fun Expression.eq(other: Expression): Formula.Numeric = Formula.Numeric(this, CompareOp.EQ, other)

/**
 * Create [Formula.Numeric] proposition by comparing two expressions using the [CompareOp.NEQ].
 */
infix fun Expression.neq(other: Expression): Formula.Numeric = Formula.Numeric(this, CompareOp.NEQ, other)

/**
 * Create [Formula.Numeric] proposition by comparing two expressions using the [CompareOp.LE].
 */
infix fun Expression.le(other: Expression): Formula.Numeric = Formula.Numeric(this, CompareOp.LE, other)

/**
 * Create [Formula.Numeric] proposition by comparing two expressions using the [CompareOp.LT].
 */
infix fun Expression.lt(other: Expression): Formula.Numeric = Formula.Numeric(this, CompareOp.LT, other)

/* ========== Direction Formula ========== */

/**
 * Convert variable name to increasing direction proposition.
 */
fun String.toIncreasing(): DirFormula.Proposition = DirFormula.Proposition(this, Direction.POSITIVE)

/**
 * Convert variable name to decreasing direction proposition.
 */
fun String.toDecreasing(): DirFormula.Proposition = DirFormula.Proposition(this, Direction.NEGATIVE)

/**
 * Create a negation of target direction formula.
 */
fun Not(inner: DirFormula): DirFormula.Not = DirFormula.Not(inner)

/**
 * Create a conjunction of two direction formulas.
 */
infix fun DirFormula.and(right: DirFormula): DirFormula.And = DirFormula.And(this, right)

/**
 * Create a disjunction of two direction formulas.
 */
infix fun DirFormula.or(right: DirFormula): DirFormula = DirFormula.Or(this, right)

/**
 * Create an implication of two direction formulas.
 */
infix fun DirFormula.implies(right: DirFormula): DirFormula = DirFormula.Implies(this, right)

/**
 * Create an equality of two direction formulas.
 */
infix fun DirFormula.equal(right: DirFormula): DirFormula = DirFormula.Equals(this, right)

/* ========== HUCTLp formula ========== */

/**
 * Create a [Formula.Transition] proposition with positive direction and incoming flow.
 */
fun String.toPositiveIn(): Formula.Transition = Formula.Transition(this, Direction.POSITIVE, Flow.IN)

/**
 * Create a [Formula.Transition] proposition with positive direction and incoming flow.
 */
fun String.toPositiveOut(): Formula.Transition = Formula.Transition(this, Direction.POSITIVE, Flow.OUT)

/**
 * Create a [Formula.Transition] proposition with positive direction and incoming flow.
 */
fun String.toNegativeIn(): Formula.Transition = Formula.Transition(this, Direction.NEGATIVE, Flow.IN)

/**
 * Create a [Formula.Transition] proposition with positive direction and incoming flow.
 */
fun String.toNegativeOut(): Formula.Transition = Formula.Transition(this, Direction.NEGATIVE, Flow.OUT)

/**
 * Create a [Formula.Reference] from this string.
 */
fun String.toReference() = Formula.Reference(this)

/**
 * Create a negation of [inner] formula.
 */
// Not an extension function to match other temporal unary ops
fun Not(inner: Formula): Formula.Not = Formula.Not(inner)

/**
 * Create a conjunction of two formulas.
 */
infix fun Formula.and(other: Formula): Formula.And = Formula.And(this, other)

/**
 * Create a disjunction of two formulas.
 */
infix fun Formula.or(other: Formula): Formula.Or = Formula.Or(this, other)

/**
 * Create an implication of two formulas.
 */
infix fun Formula.implies(other: Formula): Formula.Implies = Formula.Implies(this, other)

/**
 * Create an equality of two formulas.
 */
infix fun Formula.equal(other: Formula): Formula.Equals = Formula.Equals(this, other)

/**
 * Create an exists future formula over future paths with optional [dir] direction restriction.
 */
fun EF(inner: Formula, dir: DirFormula = DirFormula.True): Formula.Future
        = Formula.Future(PathQuantifier.E, inner, dir)

/**
 * Create an exists future formula over past paths with optional [dir] direction restriction.
 */
fun pEF(inner: Formula, dir: DirFormula = DirFormula.True): Formula
        = Formula.Future(PathQuantifier.pE, inner, dir)

/**
 * Create an all future formula over future paths with optional [dir] direction restriction.
 */
fun AF(inner: Formula, dir: DirFormula = DirFormula.True): Formula.Future
        = Formula.Future(PathQuantifier.A, inner, dir)

/**
 * Create an all future formula over past paths with optional [dir] direction restriction.
 */
fun pAF(inner: Formula, dir: DirFormula = DirFormula.True): Formula.Future
        = Formula.Future(PathQuantifier.pA, inner, dir)

/**
 * Create an exists weak future formula over future paths with optional [dir] direction restriction.
 */
fun EwF(inner: Formula, dir: DirFormula = DirFormula.True): Formula.WeakFuture
        = Formula.WeakFuture(PathQuantifier.E, inner, dir)

/**
 * Create an all weak future formula over future paths with optional [dir] direction restriction.
 */
fun AwF(inner: Formula, dir: DirFormula = DirFormula.True): Formula.WeakFuture
        = Formula.WeakFuture(PathQuantifier.A, inner, dir)

/**
 * Create an exists weak future formula over past paths with optional [dir] direction restriction.
 */
fun pEwF(inner: Formula, dir: DirFormula = DirFormula.True): Formula.WeakFuture
        = Formula.WeakFuture(PathQuantifier.pE, inner, dir)

/**
 * Create an all weak future formula over past paths with optional [dir] direction restriction.
 */
fun pAwF(inner: Formula, dir: DirFormula = DirFormula.True): Formula.WeakFuture
        = Formula.WeakFuture(PathQuantifier.pA, inner, dir)

/**
 * Create an exists next formula over future paths with optional [dir] direction restriction.
 */
fun EX(inner: Formula, dir: DirFormula = DirFormula.True): Formula.Next
        = Formula.Next(PathQuantifier.E, inner, dir)

/**
 * Create an exists next formula over past paths with optional [dir] direction restriction.
 */
fun pEX(inner: Formula, dir: DirFormula = DirFormula.True): Formula.Next
        = Formula.Next(PathQuantifier.pE, inner, dir)

/**
 * Create an all next formula over future paths with optional [dir] direction restriction.
 */
fun AX(inner: Formula, dir: DirFormula = DirFormula.True): Formula.Next
        = Formula.Next(PathQuantifier.A, inner, dir)

/**
 * Create an all next formula over past paths with optional [dir] direction restriction.
 */
fun pAX(inner: Formula, dir: DirFormula = DirFormula.True): Formula.Next
        = Formula.Next(PathQuantifier.pA, inner, dir)

/**
 * Create an exists weak next formula over future paths with optional [dir] direction restriction.
 */
fun EwX(inner: Formula, dir: DirFormula = DirFormula.True): Formula.WeakNext
        = Formula.WeakNext(PathQuantifier.E, inner, dir)

/**
 * Create an exists weak next formula over past paths with optional [dir] direction restriction.
 */
fun pEwX(inner: Formula, dir: DirFormula = DirFormula.True): Formula.WeakNext
        = Formula.WeakNext(PathQuantifier.pE, inner, dir)

/**
 * Create an all weak next formula over future paths with optional [dir] direction restriction.
 */
fun AwX(inner: Formula, dir: DirFormula = DirFormula.True): Formula.WeakNext
        = Formula.WeakNext(PathQuantifier.A, inner, dir)

/**
 * Create an all weak next formula over past paths with optional [dir] direction restriction.
 */
fun pAwX(inner: Formula, dir: DirFormula = DirFormula.True): Formula.WeakNext
        = Formula.WeakNext(PathQuantifier.pA, inner, dir)

/**
 * Create an exists globally formula over future paths with optional [dir] direction restriction.
 */
fun EG(inner: Formula, dir: DirFormula = DirFormula.True): Formula.Globally
        = Formula.Globally(PathQuantifier.E, inner, dir)

/**
 * Create an exists weak next formula over past paths with optional [dir] direction restriction.
 */
fun pEG(inner: Formula, dir: DirFormula = DirFormula.True): Formula.Globally
        = Formula.Globally(PathQuantifier.pE, inner, dir)

/**
 * Create an all weak next formula over future paths with optional [dir] direction restriction.
 */
fun AG(inner: Formula, dir: DirFormula = DirFormula.True): Formula.Globally
        = Formula.Globally(PathQuantifier.A, inner, dir)

/**
 * Create an all weak next formula over past paths with optional [dir] direction restriction.
 */
fun pAG(inner: Formula, dir: DirFormula = DirFormula.True): Formula.Globally
        = Formula.Globally(PathQuantifier.pA, inner, dir)

/**
 * Create an exists until formula over future paths without direction restriction.
 */
infix fun Formula.EU(reach: Formula): Formula.Until
        = Formula.Until(PathQuantifier.E, this, reach, DirFormula.True)

/**
 * Create an all until formula over future paths without direction restriction.
 */
infix fun Formula.AU(reach: Formula): Formula.Until
        = Formula.Until(PathQuantifier.A, this, reach, DirFormula.True)

/**
 * Create an exists until formula over past paths without direction restriction.
 */
infix fun Formula.pEU(reach: Formula): Formula.Until
        = Formula.Until(PathQuantifier.pE, this, reach, DirFormula.True)

/**
 * Create an all until formula over past paths without direction restriction.
 */
infix fun Formula.pAU(reach: Formula): Formula.Until
        = Formula.Until(PathQuantifier.pA, this, reach, DirFormula.True)

/**
 * Create an exists until formula over future paths with optional direction restriction.
 */
fun Formula.EU(reach: Formula, dir: DirFormula = DirFormula.True): Formula.Until
        = Formula.Until(PathQuantifier.E, this, reach, dir)

/**
 * Create an all until formula over future paths with optional direction restriction.
 */
fun Formula.AU(reach: Formula, dir: DirFormula = DirFormula.True): Formula.Until
        = Formula.Until(PathQuantifier.A, this, reach, dir)

/**
 * Create an exists until formula over past paths with optional direction restriction.
 */
fun Formula.pEU(reach: Formula, dir: DirFormula = DirFormula.True): Formula.Until
        = Formula.Until(PathQuantifier.pE, this, reach, dir)

/**
 * Create an all until formula over past paths with optional direction restriction.
 */
fun Formula.pAU(reach: Formula, dir: DirFormula = DirFormula.True): Formula.Until
        = Formula.Until(PathQuantifier.pA, this, reach, dir)

/**
 * Create a hybrid At formula valid at the given [name].
 */
fun At(name: String, inner: Formula) : Formula.At = Formula.At(name, inner)

/**
 * Create a hybrid Bind formula, binding the given [name].
 */
fun Bind(name: String, inner: Formula) : Formula.Bind = Formula.Bind(name, inner)

/**
 * Create a first order ForAll formula, binding the given [name].
 */
fun ForAll(name: String, bound: Formula = Formula.True, inner: Formula) : Formula.ForAll
        = Formula.ForAll(name, bound, inner)

/**
 * Create a first order Exists formula, binding the given [name].
 */
fun Exists(name: String, bound: Formula = Formula.True, inner: Formula) : Formula.Exists
        = Formula.Exists(name, bound, inner)