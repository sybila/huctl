package com.github.sybila.huctl

import com.github.sybila.huctl.parser.toFormula

/**
 * Helper method so that we don't have to write .toFormula() everywhere.
 */
internal fun formulaEquals(expected: Formula, parse: String) {
    kotlin.test.assertEquals(expected, parse.toFormula())
}

/**
 * Simple notation for creating text propositions.
 *
 * This is not standard syntax. Do not use outside of tests.
 */

internal operator fun String.not(): Formula.Text = Formula.Text(this)
internal operator fun Formula.Text.not(): DirFormula.Text = DirFormula.Text(this.value)
internal operator fun Double.not(): Expression.Constant = Expression.Constant(this)