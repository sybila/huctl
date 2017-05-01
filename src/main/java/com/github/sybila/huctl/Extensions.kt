package com.github.sybila.huctl

import com.github.sybila.huctl.Formula.*
import com.github.sybila.huctl.PathQuantifier.*

// -- Catamorphism --

fun <R> Expression.fold(
        constant: Expression.Constant.() -> R,
        variable: Expression.Variable.() -> R,
        arithmetic: Expression.Binary<*>.(R, R) -> R): R {
    return when (this) {
        is Expression.Constant -> constant(this)
        is Expression.Variable -> variable(this)
        is Expression.Binary<*> -> arithmetic(this,
                this.left.fold(constant, variable, arithmetic),
                this.right.fold(constant, variable, arithmetic)
        )
        else -> throw IllegalStateException("Cannot fold over expression $this")
    }
}

fun Expression.mapLeafs(
        constant: (Expression.Constant) -> Expression,
        variable: (Expression.Variable) -> Expression
): Expression {
    return this.fold(constant, variable, Expression.Binary<*>::copy)
}

fun <R> DirFormula.fold(
        atom: DirFormula.() -> R,
        unary: DirFormula.Unary<*>.(R) -> R,
        binary: DirFormula.Binary<*>.(R, R) -> R
) : R {
    return when (this) {
        is DirFormula.Atom -> atom(this)
        is DirFormula.Unary<*> -> unary(this, this.inner.fold(atom, unary, binary))
        is DirFormula.Binary<*> -> binary(this,
                this.left.fold(atom, unary, binary),
                this.right.fold(atom, unary, binary)
        )
        else -> throw IllegalStateException("Cannot fold over formula $this")
    }
}

fun DirFormula.mapLeafs(
        atom: (DirFormula) -> DirFormula
): DirFormula {
    return this.fold(atom, DirFormula.Unary<*>::copy, DirFormula.Binary<*>::copy)
}

fun <R> Formula.fold(
        atom: Atom.() -> R,
        unary: Unary<*>.(R) -> R,
        binary: Binary<*>.(R, R) -> R
): R {
    return when (this) {
        is Atom -> atom(this)
        is Unary<*> -> unary(this, this.inner.fold(atom, unary, binary))
        is Binary<*> -> binary(this,
                this.left.fold(atom, unary, binary),
                this.right.fold(atom, unary, binary)
        )
        else -> throw IllegalStateException("Cannot fold over formula $this")
    }
}

fun Formula.mapLeafs(
        atom: (Atom) -> Formula
): Formula {
    return this.fold(atom, Unary<*>::copy, Binary<*>::copy)
}

// -- Construction Utils --

// Expressions

fun String.asVariable(): Expression.Variable = Expression.Variable(this)
fun Double.asConstant(): Expression.Constant = Expression.Constant(this)

infix operator fun Expression.plus(other: Expression) = Expression.Add(this, other)
infix operator fun Expression.minus(other: Expression) = Expression.Sub(this, other)
infix operator fun Expression.times(other: Expression) = Expression.Mul(this, other)
infix operator fun Expression.div(other: Expression) = Expression.Div(this, other)

infix fun Expression.gt(other: Expression): Numeric = Numeric(this, CompareOp.GT, other)
infix fun Expression.ge(other: Expression): Numeric = Numeric(this, CompareOp.GE, other)
infix fun Expression.eq(other: Expression): Numeric = Numeric(this, CompareOp.EQ, other)
infix fun Expression.neq(other: Expression): Numeric = Numeric(this, CompareOp.NEQ, other)
infix fun Expression.le(other: Expression): Numeric = Numeric(this, CompareOp.LE, other)
infix fun Expression.lt(other: Expression): Numeric = Numeric(this, CompareOp.LT, other)

// Propositions

val True = Formula.True
val False = Formula.False

fun String.positiveIn() = Transition(this, Direction.IN, Facet.POSITIVE)
fun String.positiveOut() = Transition(this, Direction.OUT, Facet.POSITIVE)
fun String.negativeIn() = Transition(this, Direction.IN, Facet.NEGATIVE)
fun String.negativeOut() = Transition(this, Direction.OUT, Facet.NEGATIVE)
fun String.asReference() = Reference(this)

// Direction propositions

fun String.increase(): DirFormula = DirFormula.Proposition(this, Facet.POSITIVE)
fun String.decrease(): DirFormula = DirFormula.Proposition(this, Facet.NEGATIVE)

// Direction formulas

fun not(inner: DirFormula): DirFormula = DirFormula.Not(inner)

infix fun DirFormula.and(right: DirFormula): DirFormula = DirFormula.And(this, right)
infix fun DirFormula.or(right: DirFormula): DirFormula = DirFormula.Or(this, right)
infix fun DirFormula.implies(right: DirFormula): DirFormula = DirFormula.Implies(this, right)
infix fun DirFormula.equal(right: DirFormula): DirFormula = DirFormula.Equals(this, right)


// Formulas

fun not(inner: Formula): Formula = Not(inner)

//Future
fun EF(inner: Formula, dir: DirFormula = DirFormula.True): Formula = Future(E, inner, dir)
fun pastEF(inner: Formula, dir: DirFormula = DirFormula.True): Formula = Future(pE, inner, dir)
fun AF(inner: Formula, dir: DirFormula = DirFormula.True): Formula = Future(A, inner, dir)
fun pastAF(inner: Formula, dir: DirFormula = DirFormula.True): Formula = Future(pA, inner, dir)

//WeakFuture
fun weakEF(inner: Formula, dir: DirFormula = DirFormula.True): Formula = WeakFuture(E, inner, dir)
fun weakAF(inner: Formula, dir: DirFormula = DirFormula.True): Formula = WeakFuture(A, inner, dir)
fun pastWeakEF(inner: Formula, dir: DirFormula = DirFormula.True): Formula = WeakFuture(pE, inner, dir)
fun pastWeakAF(inner: Formula, dir: DirFormula = DirFormula.True): Formula = WeakFuture(pA, inner, dir)

//Next
fun EX(inner: Formula, dir: DirFormula = DirFormula.True): Formula = Next(E, inner, dir)
fun pastEX(inner: Formula, dir: DirFormula = DirFormula.True): Formula = Next(pE, inner, dir)
fun AX(inner: Formula, dir: DirFormula = DirFormula.True): Formula = Next(A, inner, dir)
fun pastAX(inner: Formula, dir: DirFormula = DirFormula.True): Formula = Next(pA, inner, dir)

//WeakNext
fun weakEX(inner: Formula, dir: DirFormula = DirFormula.True): Formula = WeakNext(E, inner, dir)
fun pastWeakEX(inner: Formula, dir: DirFormula = DirFormula.True): Formula = WeakNext(pE, inner, dir)
fun weakAX(inner: Formula, dir: DirFormula = DirFormula.True): Formula = WeakNext(A, inner, dir)
fun pastWeakAX(inner: Formula, dir: DirFormula = DirFormula.True): Formula = WeakNext(pA, inner, dir)

//Globally
fun EG(inner: Formula, dir: DirFormula = DirFormula.True): Formula = Globally(E, inner, dir)
fun pastEG(inner: Formula, dir: DirFormula = DirFormula.True): Formula = Globally(pE, inner, dir)
fun AG(inner: Formula, dir: DirFormula = DirFormula.True): Formula = Globally(A, inner, dir)
fun pastAG(inner: Formula, dir: DirFormula = DirFormula.True): Formula = Globally(pA, inner, dir)

//Until
infix fun Formula.EU(reach: Formula): Formula
        = Until(E, this, reach, DirFormula.True)
fun Formula.EU(reach: Formula, dir: DirFormula = DirFormula.True): Formula
        = Until(E, this, reach, dir)
infix fun Formula.AU(reach: Formula): Formula
        = Until(A, this, reach, DirFormula.True)
fun Formula.AU(reach: Formula, dir: DirFormula = DirFormula.True): Formula
        = Until(A, this, reach, dir)
infix fun Formula.pastEU(reach: Formula): Formula
        = Until(pE, this, reach, DirFormula.True)
fun Formula.pastEU(reach: Formula, dir: DirFormula = DirFormula.True): Formula
        = Until(pE, this, reach, dir)
infix fun Formula.pastAU(reach: Formula): Formula
        = Until(pA, this, reach, DirFormula.True)
fun Formula.pastAU(reach: Formula, dir: DirFormula = DirFormula.True): Formula
        = Until(pA, this, reach, dir)

//Hybrid and first order

fun at(name: String, inner: Formula) : Formula = Formula.At(name, inner)
fun bind(name: String, inner: Formula) : Formula = Formula.Bind(name, inner)
fun forall(name: String, bound: Formula, inner: Formula) : Formula = ForAll(name, bound, inner)
fun exists(name: String, bound: Formula, inner: Formula) : Formula = Exists(name, bound, inner)

//Bool
infix fun Formula.and(other: Formula): Formula = And(this, other)
infix fun Formula.or(other: Formula): Formula = Or(this, other)
infix fun Formula.implies(other: Formula): Formula = Implies(this, other)
infix fun Formula.equal(other: Formula): Formula = Equals(this, other)