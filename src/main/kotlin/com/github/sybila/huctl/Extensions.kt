package com.github.sybila.huctl

import com.github.sybila.huctl.Formula.*
import com.github.sybila.huctl.Formula.Bool.*
import com.github.sybila.huctl.Formula.Simple.*
import com.github.sybila.huctl.PathQuantifier.*

// -- Catamorphism --

fun <R> Expression.fold(
        constant: Expression.Constant.() -> R,
        variable: Expression.Variable.() -> R,
        arithmetic: Expression.Arithmetic<*>.(R, R) -> R): R {
    return when (this) {
        is Expression.Constant -> constant(this)
        is Expression.Variable -> variable(this)
        is Expression.Arithmetic<*> -> arithmetic(this,
                this.left.fold(constant, variable, arithmetic),
                this.right.fold(constant, variable, arithmetic)
        )
    }
}

fun Expression.mapLeafs(
        constant: (Expression.Constant) -> Expression,
        variable: (Expression.Variable) -> Expression
): Expression {
    return this.fold(constant, variable, Expression.Arithmetic<*>::copy)
}

fun <R> DirectionFormula.fold(
        atom: DirectionFormula.Atom.() -> R,
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
        atom: (DirectionFormula.Atom) -> DirectionFormula
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

infix operator fun Expression.plus(other: Expression) = Expression.Arithmetic.Add(this, other)
infix operator fun Expression.minus(other: Expression) = Expression.Arithmetic.Sub(this, other)
infix operator fun Expression.times(other: Expression) = Expression.Arithmetic.Mul(this, other)
infix operator fun Expression.div(other: Expression) = Expression.Arithmetic.Div(this, other)

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

// Direction propositions

fun String.increase(): DirectionFormula = DirectionFormula.Atom.Proposition(this, Facet.POSITIVE)
fun String.decrease(): DirectionFormula = DirectionFormula.Atom.Proposition(this, Facet.NEGATIVE)

// Direction formulas

fun not(inner: DirectionFormula): DirectionFormula = DirectionFormula.Not(inner)

infix fun DirectionFormula.and(right: DirectionFormula): DirectionFormula = DirectionFormula.Bool.And(this, right)
infix fun DirectionFormula.or(right: DirectionFormula): DirectionFormula = DirectionFormula.Bool.Or(this, right)
infix fun DirectionFormula.implies(right: DirectionFormula): DirectionFormula = DirectionFormula.Bool.Implies(this, right)
infix fun DirectionFormula.equal(right: DirectionFormula): DirectionFormula = DirectionFormula.Bool.Equals(this, right)


// Formulas

fun not(inner: Formula): Formula = Not(inner)

//Future
fun EF(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = Future(E, inner, dir)
fun pastEF(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = Future(pE, inner, dir)
fun AF(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = Future(A, inner, dir)
fun pastAF(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = Future(pA, inner, dir)

//WeakFuture
fun weakEF(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = WeakFuture(E, inner, dir)
fun weakAF(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = WeakFuture(A, inner, dir)
fun pastWeakEF(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = WeakFuture(pE, inner, dir)
fun pastWeakAF(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = WeakFuture(pA, inner, dir)

//Next
fun EX(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = Next(E, inner, dir)
fun pastEX(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = Next(pE, inner, dir)
fun AX(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = Next(A, inner, dir)
fun pastAX(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = Next(pA, inner, dir)

//WeakNext
fun weakEX(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = WeakNext(E, inner, dir)
fun pastWeakEX(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = WeakNext(pE, inner, dir)
fun weakAX(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = WeakNext(A, inner, dir)
fun pastWeakAX(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = WeakNext(pA, inner, dir)

//Globally
fun EG(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = Globally(E, inner, dir)
fun pastEG(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = Globally(pE, inner, dir)
fun AG(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = Globally(A, inner, dir)
fun pastAG(inner: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula = Globally(pA, inner, dir)

//Until
infix fun Formula.EU(reach: Formula): Formula
        = Until(E, this, reach, DirectionFormula.Atom.True)
fun Formula.EU(reach: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula
        = Until(E, this, reach, dir)
infix fun Formula.AU(reach: Formula): Formula
        = Until(A, this, reach, DirectionFormula.Atom.True)
fun Formula.AU(reach: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula
        = Until(A, this, reach, dir)
infix fun Formula.pastEU(reach: Formula): Formula
        = Until(pE, this, reach, DirectionFormula.Atom.True)
fun Formula.pastEU(reach: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula
        = Until(pE, this, reach, dir)
infix fun Formula.pastAU(reach: Formula): Formula
        = Until(pA, this, reach, DirectionFormula.Atom.True)
fun Formula.pastAU(reach: Formula, dir: DirectionFormula = DirectionFormula.Atom.True): Formula
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