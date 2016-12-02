package com.github.sybila.ctl

import org.junit.Test
import kotlin.test.assertEquals

class FoldTest {

    val formula = EX(tt() EU (
            "var".asVariable() eq 13.3.asConstant()
                or
            "val".negativeIn()
        )
    )

    @Test
    fun foldIdentity() {
        assertEquals(formula, formula.fold<Formula>({this},{this.copy(it)},{ l, r -> this.copy(l,r) }))
    }

    @Test
    fun foldLeafs() {
        assertEquals(
            EX(False EU (
                    "var".positiveOut()
                        or
                    ("val".asVariable() neq 10.3.asConstant())
                )
            ), formula.mapLeafs {
                when (it.proposition) {
                    is Proposition.True -> False
                    is Proposition.Comparison.EQ ->
                        "var".positiveOut().asAtom()
                    is Proposition.DirectionProposition ->
                        ("val".asVariable() neq 10.3.asConstant()).asAtom()
                    else -> it
                }
            }
        )
    }

    @Test
    fun foldOperators() {
        assertEquals(
                AX(True AU (
                    "var".asVariable() eq 13.3.asConstant()
                        and
                    "val".negativeIn()
                )
            ), formula.fold<Formula>({this}, {
                if (this is Formula.Unary.EX) {
                    AX(it)
                } else this.copy(it)
            }, { l,r ->
                if (this is Formula.Binary.EU) {
                    l AU r
                } else if (this is Formula.Binary.Or) {
                    l and r
                } else this.copy(l, r)
            })
        )
    }

    @Test
    fun foldHeight() {
        assertEquals(4, formula.fold({1}, { it + 1 }, { l, r -> Math.max(l,r) + 1 } ))
    }

}

class Misc {

    @Test
    fun booleanToString() {
        assertEquals("true", True.toString())
        assertEquals("false", False.toString())
    }

    @Test
    fun variableToString() {
        assertEquals("test", "test".asVariable().toString())
    }

    @Test
    fun constantToString() {
        assertEquals("3.1400", 3.14.asConstant().toString())
    }

    @Test
    fun expressionToString() {
        assertEquals("((a + 12.0000) / ((3.0000 * 4.0000) - Var))",
                (
                        ("a".asVariable() plus 12.0.asConstant())
                                div
                        ((3.0.asConstant() times 4.0.asConstant()) minus "Var".asVariable())
                ).toString())
    }

    @Test
    fun formulaToString() {
        assertEquals("(true && EX (false EU true))", (True and EX(False EU True)).toString())
    }

    @Test
    fun floatPropositionToString() {
        assertEquals("(prop > 5.3000)", ("prop".asVariable() gt 5.3.asConstant()).toString())
    }

    @Test
    fun directionToString() {
        assertEquals("prop:in+", "prop".positiveIn().toString())
    }

}