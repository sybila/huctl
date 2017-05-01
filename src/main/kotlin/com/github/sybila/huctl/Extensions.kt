package com.github.sybila.huctl

import com.github.sybila.huctl.Formula.*
import com.github.sybila.huctl.Formula.Bool.*
import com.github.sybila.huctl.Formula.Simple.*
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

fun <R> DirectionFormula.fold(
        atom: DirectionFormula.() -> R,
        unary: DirectionFormula.Unary<*>.(R) -> R,
        binary: DirectionFormula.Binary<*>.(R, R) -> R
) : R {
    return when (this) {
        is DirectionFormula.Atom -> atom(this)
        is DirectionFormula.Unary<*> -> unary(this, this.inner.fold(atom, unary, binary))
        is DirectionFormula.Binary<*> -> binary(this,
                this.left.fold(atom, unary, binary),
                this.right.fold(atom, unary, binary)
        )
        else -> throw IllegalStateException("Cannot fold over formula $this")
    }
}

fun DirectionFormula.mapLeafs(
        atom: (DirectionFormula) -> DirectionFormula
): DirectionFormula {
    return this.fold(atom, DirectionFormula.Unary<*>::copy, DirectionFormula.Binary<*>::copy)
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

infix fun Expression.gt(other: Expression): Atom.Float = Atom.Float(this, CompareOp.GT, other)
infix fun Expression.ge(other: Expression): Atom.Float = Atom.Float(this, CompareOp.GE, other)
infix fun Expression.eq(other: Expression): Atom.Float = Atom.Float(this, CompareOp.EQ, other)
infix fun Expression.neq(other: Expression): Atom.Float = Atom.Float(this, CompareOp.NEQ, other)
infix fun Expression.le(other: Expression): Atom.Float = Atom.Float(this, CompareOp.LE, other)
infix fun Expression.lt(other: Expression): Atom.Float = Atom.Float(this, CompareOp.LT, other)

// Propositions

val True = Atom.True
val False = Atom.False

fun String.positiveIn() = Atom.Transition(this, Direction.IN, Facet.POSITIVE)
fun String.positiveOut() = Atom.Transition(this, Direction.OUT, Facet.POSITIVE)
fun String.negativeIn() = Atom.Transition(this, Direction.IN, Facet.NEGATIVE)
fun String.negativeOut() = Atom.Transition(this, Direction.OUT, Facet.NEGATIVE)
fun String.asReference() = Atom.Reference(this)

// Direction propositions

fun String.increase(): DirectionFormula = DirectionFormula.Proposition(this, Facet.POSITIVE)
fun String.decrease(): DirectionFormula = DirectionFormula.Proposition(this, Facet.NEGATIVE)

// Direction formulas

fun not(inner: DirectionFormula): DirectionFormula = DirectionFormula.Not(inner)

infix fun DirectionFormula.and(right: DirectionFormula): DirectionFormula = DirectionFormula.And(this, right)
infix fun DirectionFormula.or(right: DirectionFormula): DirectionFormula = DirectionFormula.Or(this, right)
infix fun DirectionFormula.implies(right: DirectionFormula): DirectionFormula = DirectionFormula.Implies(this, right)
infix fun DirectionFormula.equal(right: DirectionFormula): DirectionFormula = DirectionFormula.Equals(this, right)


// Formulas

fun not(inner: Formula): Formula = Not(inner)

//Future
fun EF(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Future(E, inner, dir)
fun pastEF(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Future(pE, inner, dir)
fun AF(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Future(A, inner, dir)
fun pastAF(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Future(pA, inner, dir)

//WeakFuture
fun weakEF(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = WeakFuture(E, inner, dir)
fun weakAF(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = WeakFuture(A, inner, dir)
fun pastWeakEF(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = WeakFuture(pE, inner, dir)
fun pastWeakAF(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = WeakFuture(pA, inner, dir)

//Next
fun EX(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Next(E, inner, dir)
fun pastEX(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Next(pE, inner, dir)
fun AX(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Next(A, inner, dir)
fun pastAX(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Next(pA, inner, dir)

//WeakNext
fun weakEX(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = WeakNext(E, inner, dir)
fun pastWeakEX(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = WeakNext(pE, inner, dir)
fun weakAX(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = WeakNext(A, inner, dir)
fun pastWeakAX(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = WeakNext(pA, inner, dir)

//Globally
fun EG(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Globally(E, inner, dir)
fun pastEG(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Globally(pE, inner, dir)
fun AG(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Globally(A, inner, dir)
fun pastAG(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Globally(pA, inner, dir)

//Until
infix fun Formula.EU(reach: Formula): Formula
        = Until(E, this, reach, DirectionFormula.True)
fun Formula.EU(reach: Formula, dir: DirectionFormula = DirectionFormula.True): Formula
        = Until(E, this, reach, dir)
infix fun Formula.AU(reach: Formula): Formula
        = Until(A, this, reach, DirectionFormula.True)
fun Formula.AU(reach: Formula, dir: DirectionFormula = DirectionFormula.True): Formula
        = Until(A, this, reach, dir)
infix fun Formula.pastEU(reach: Formula): Formula
        = Until(pE, this, reach, DirectionFormula.True)
fun Formula.pastEU(reach: Formula, dir: DirectionFormula = DirectionFormula.True): Formula
        = Until(pE, this, reach, dir)
infix fun Formula.pastAU(reach: Formula): Formula
        = Until(pA, this, reach, DirectionFormula.True)
fun Formula.pastAU(reach: Formula, dir: DirectionFormula = DirectionFormula.True): Formula
        = Until(pA, this, reach, dir)

//Hybrid and first order

fun at(name: String, inner: Formula) : Formula = Formula.Hybrid.At(name, inner)
fun bind(name: String, inner: Formula) : Formula = Formula.Hybrid.Bind(name, inner)
fun forall(name: String, bound: Formula, inner: Formula) : Formula = Formula.FirstOrder.ForAll(name, bound, inner)
fun exists(name: String, bound: Formula, inner: Formula) : Formula = Formula.FirstOrder.Exists(name, bound, inner)

//Bool
infix fun Formula.and(other: Formula): Formula = And(this, other)
infix fun Formula.or(other: Formula): Formula = Or(this, other)
infix fun Formula.implies(other: Formula): Formula = Implies(this, other)
infix fun Formula.equal(other: Formula): Formula = Equals(this, other)