package com.github.sybila.ctl

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

infix fun Expression.gt(other: Expression): Proposition = Proposition.Comparison.GT(this, other)
infix fun Expression.ge(other: Expression): Proposition = Proposition.Comparison.GE(this, other)
infix fun Expression.eq(other: Expression): Proposition = Proposition.Comparison.EQ(this, other)
infix fun Expression.neq(other: Expression): Proposition = Proposition.Comparison.NEQ(this, other)
infix fun Expression.le(other: Expression): Proposition = Proposition.Comparison.LE(this, other)
infix fun Expression.lt(other: Expression): Proposition = Proposition.Comparison.LT(this, other)

// Propositions

fun tt() = Proposition.True()
fun ff() = Proposition.False()

val True = tt().asAtom()
val False = ff().asAtom()

fun String.positiveIn() = Proposition.DirectionProposition(this, Direction.IN, Facet.POSITIVE)
fun String.positiveOut() = Proposition.DirectionProposition(this, Direction.OUT, Facet.POSITIVE)
fun String.negativeIn() = Proposition.DirectionProposition(this, Direction.IN, Facet.NEGATIVE)
fun String.negativeOut() = Proposition.DirectionProposition(this, Direction.OUT, Facet.NEGATIVE)
fun Proposition.asAtom(): Formula = Formula.Atom(this)

// Formulas

fun not(inner: (() -> Formula)): Formula = Formula.Unary.Not(inner())
fun EF(inner: (() -> Formula)): Formula = Formula.Unary.EF(inner())
fun AF(inner: (() -> Formula)): Formula = Formula.Unary.AF(inner())
fun EG(inner: (() -> Formula)): Formula = Formula.Unary.EG(inner())
fun AG(inner: (() -> Formula)): Formula = Formula.Unary.AG(inner())
fun EX(inner: (() -> Formula)): Formula = Formula.Unary.EX(inner())
fun AX(inner: (() -> Formula)): Formula = Formula.Unary.AX(inner())

infix fun (() -> Formula).EU(reach: () -> Formula): Formula = Formula.Binary.EU(this(), reach())
infix fun (() -> Formula).AU(reach: () -> Formula): Formula = Formula.Binary.AU(this(), reach())

infix fun (() -> Formula).and(other: (() -> Formula)): Formula = Formula.Binary.And(this(), other())
infix fun (() -> Formula).or(other: (() -> Formula)): Formula = Formula.Binary.Or(this(), other())
infix fun (() -> Formula).implies(other: (() -> Formula)): Formula = Formula.Binary.Implies(this(), other())
infix fun (() -> Formula).equal(other: (() -> Formula)): Formula = Formula.Binary.Equal(this(), other())