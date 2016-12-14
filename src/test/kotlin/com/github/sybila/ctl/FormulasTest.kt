package com.github.sybila.ctl

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class FoldTest {

    val formula = EX(True EU (
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
                when (it) {
                    is Formula.Atom.True -> False
                    is Formula.Atom.Float -> "var".positiveOut()
                    is Formula.Atom.Transition -> ("val".asVariable() neq 10.3.asConstant())
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
                if (this is Formula.Simple.Next) {
                    AX(it)
                } else this.copy(it)
            }, { l,r ->
                if (this is Formula.Until) {
                    l AU r
                } else if (this is Formula.Bool.Or) {
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
        assertEquals("(true && {true}EX (false {true}EU true))", (True and EX(False EU True)).toString())
    }

    @Test
    fun floatPropositionToString() {
        val prop = ("prop".asVariable() gt 5.3.asConstant())
        assertEquals("(prop > 5.3000)", prop.toString())
    }

    @Test
    fun directionToString() {
        assertEquals("prop:in+", "prop".positiveIn().toString())
    }

    @Test
    fun basicProperties() {
        val v1 = "v1".asVariable()
        val v2 = "v2".asVariable()
        assertNotEquals(v1.hashCode(), v2.hashCode())
        assertNotEquals(v1, v2)

        val prop1 = ("prop1".asVariable() gt 5.3.asConstant())
        val prop2 = ("prop2".asVariable() gt (54.3.asConstant() plus 3.2.asConstant()))
        assertNotEquals(prop1.hashCode(), prop2.hashCode())
        assertNotEquals(prop1, prop2)

        val dir1 = "v1".positiveIn()
        val dir2 = "v1".negativeOut()
        assertNotEquals(dir1.hashCode(), dir2.hashCode())
        assertNotEquals(dir1, dir2)
        assertEquals("v1", dir1.name)
        assertEquals(Facet.POSITIVE, dir1.facet)
        assertEquals(Direction.IN, dir1.direction)
    }

}