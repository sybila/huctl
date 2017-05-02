package com.github.sybila.huctl

import com.github.sybila.huctl.Formula.*
import com.github.sybila.huctl.PathQuantifier.*

// -- Catamorphism --

fun <R> Expression.fold(
        constant: Expression.Constant.() -> R,
        variable: Expression.Variable.() -> R,
        arithmetic: Binary<*, Expression>.(R, R) -> R): R {
    return when (this) {
        is Expression.Constant -> constant(this)
        is Expression.Variable -> variable(this)
        is Binary<*, *> -> arithmetic(this,
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
    return this.fold<Expression>(constant, variable, Binary<*, Expression>::copy)
}

fun <R> DirFormula.fold(
        atom: DirFormula.() -> R,
        unary: DirFormula.Unary<*>.(R) -> R,
        binary: DirFormula.Binary<*>.(R, R) -> R
) : R {
    return when (this) {
        is DirFormula.Unary<*> -> unary(this, this.inner.fold(atom, unary, binary))
        is DirFormula.Binary<*> -> binary(this,
                this.left.fold(atom, unary, binary),
                this.right.fold(atom, unary, binary)
        )
        else -> atom(this)
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