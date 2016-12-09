package com.github.sybila.ctl

import com.github.sybila.ctl.Formula.*
import com.github.sybila.ctl.Formula.Bool.*
import com.github.sybila.ctl.Formula.Temporal.Simple.*
import com.github.sybila.ctl.PathQuantifier.*

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

fun <R> Formula.fold(
        atom: Formula.Atom.() -> R,
        unary: Formula.Unary<*>.(R) -> R,
        binary: Formula.Binary<*>.(R, R) -> R
): R {
    return when (this) {
        is Formula.Atom -> atom(this)
        is Formula.Unary<*> -> unary(this, this.inner.fold(atom, unary, binary))
        is Formula.Binary<*> -> binary(this,
                this.left.fold(atom, unary, binary),
                this.right.fold(atom, unary, binary)
        )
        else -> throw IllegalStateException("Cannot fold over formula $this")
    }
}

fun Formula.mapLeafs(
        atom: (Formula.Atom) -> Formula
): Formula {
    return this.fold(atom, Formula.Unary<*>::copy, Formula.Binary<*>::copy)
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

// Formulas

fun not(inner: Formula): Formula = Not(inner)

//Future
fun EF(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Future(EXISTS, inner(), dir)
fun pastEF(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Future(EXISTS_PAST, inner(), dir)
fun AF(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Future(ALL, inner(), dir)
fun pastAF(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Future(ALL_PAST, inner(), dir)

//WeakFuture
fun weakEF(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = WeakFuture(EXISTS, inner(), dir)
fun weakAF(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = WeakFuture(ALL, inner(), dir)
fun pastWeakEF(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = WeakFuture(EXISTS_PAST, inner(), dir)
fun pastWeakAF(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = WeakFuture(ALL_PAST, inner(), dir)

//Next
fun EX(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Next(EXISTS, inner(), dir)
fun pastEX(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Next(EXISTS_PAST, inner(), dir)
fun AX(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Next(ALL, inner(), dir)
fun pastAX(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Next(ALL_PAST, inner(), dir)

//WeakNext
fun weakEX(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = WeakNext(EXISTS, inner(), dir)
fun pastWeakEX(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = WeakNext(EXISTS_PAST, inner(), dir)
fun weakAX(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = WeakNext(ALL, inner(), dir)
fun pastWeakAX(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = WeakNext(ALL_PAST, inner(), dir)

//Globally
fun EG(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Globally(EXISTS, inner(), dir)
fun pastEG(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Globally(EXISTS_PAST, inner(), dir)
fun AG(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Globally(ALL, inner(), dir)
fun pastAG(inner: Formula, dir: DirectionFormula = DirectionFormula.True): Formula = Globally(ALL_PAST, inner(), dir)

//Until
infix fun Formula.EU(reach: Formula): Formula
        = Temporal.Until(EXISTS, this, reach, DirectionFormula.True)
fun Formula.EU(reach: Formula, dir: DirectionFormula = DirectionFormula.True): Formula
        = Temporal.Until(EXISTS, this, reach, dir)
infix fun Formula.AU(reach: Formula): Formula
        = Temporal.Until(ALL, this, reach, DirectionFormula.True)
fun Formula.AU(reach: Formula, dir: DirectionFormula = DirectionFormula.True): Formula
        = Temporal.Until(ALL, this, reach, dir)
infix fun Formula.pastEU(reach: Formula): Formula
        = Temporal.Until(EXISTS_PAST, this, reach, DirectionFormula.True)
fun Formula.pastEU(reach: Formula, dir: DirectionFormula = DirectionFormula.True): Formula
        = Temporal.Until(EXISTS_PAST, this, reach, dir)
infix fun Formula.pastAU(reach: Formula): Formula
        = Temporal.Until(ALL_PAST, this, reach, DirectionFormula.True)
fun Formula.pastAU(reach: Formula, dir: DirectionFormula = DirectionFormula.True): Formula
        = Temporal.Until(ALL_PAST, this, reach, dir)

//Bool
infix fun Formula.and(other: Formula): Formula = And(this(), other())
infix fun Formula.or(other: Formula): Formula = Or(this(), other())
infix fun Formula.implies(other: Formula): Formula = Implies(this(), other())
infix fun Formula.equal(other: Formula): Formula = Equals(this(), other())