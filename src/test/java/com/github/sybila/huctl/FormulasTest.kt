package com.github.sybila.huctl

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class FoldTest {

    val formula = EX((True EU (
            "var".asVariable() eq 13.3.asConstant()
                or
            "val".negativeIn()))
        , dir = "x".increase()
    )


    @Test
    fun foldIdentity() {
        assertEquals(formula, formula.fold({this}, Formula.Unary<*>::copy, Formula.Binary<*>::copy))
    }

    @Test
    fun foldLeafs() {
        assertEquals(
            EX(False EU (
                    "var".positiveOut()
                        or
                    ("val".asVariable() neq 10.3.asConstant())
                ), dir = "x".increase()
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
                ), dir = "y".decrease()
            ), formula.fold<Formula>({this}, {
                if (this is Formula.Simple.Next) {
                    AX(it, dir = "y".decrease())
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
    fun ctlFormulaToString() {
        assertEquals("(!true && {true}pEwX (false {true}EU true))", (not(True) and pastWeakEX(False EU True)).toString())
    }

    @Test
    fun hybridFormulaToString() {
        assertEquals("(at x : (bind y : true))", at("x", bind("y", True)).toString())
    }

    @Test
    fun firstOrderFormulaToString() {
        assertEquals(
                "(forall x in false : (exists y in true : false))",
                forall("x", False, exists("y", True, False)).toString()
        )
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
        assertEquals(Direction.POSITIVE, dir1.facet)
        assertEquals(Flow.IN, dir1.direction)

        val u = (prop1 EU prop2) as Formula.Until
        assertEquals(prop1, u.path)
        assertEquals(prop2, u.reach)
    }

    @Test
    fun directionCast() {
        val tt = Formula.Atom.True
        val ff = Formula.Atom.False
        val dTT = DirFormula.Atom.True
        val dFF = DirFormula.Atom.False

        //simple invalid
        assertNull(("a".asVariable() gt "b".asVariable()).asDirectionFormula())
        assertNull("a".positiveIn().asDirectionFormula())
        assertNull(EX(tt).asDirectionFormula())
        assertNull((tt EU ff).asDirectionFormula())
        assertNull(forall("x", tt, tt).asDirectionFormula())
        assertNull(bind("x", tt).asDirectionFormula())

        //simple valid
        assertEquals(dTT, tt.asDirectionFormula())
        assertEquals(dFF, ff.asDirectionFormula())
        assertEquals(not(dTT), not(tt).asDirectionFormula())
        assertEquals(dTT and dFF, (tt and ff).asDirectionFormula())
        assertEquals(dTT or dFF, (tt or ff).asDirectionFormula())
        assertEquals(dTT implies dFF, (tt implies ff).asDirectionFormula())
        assertEquals(dTT equal dFF, (tt equal ff).asDirectionFormula())

        //complex invalid
        assertNull((tt and "a".positiveIn()).asDirectionFormula())
        assertNull(("a".positiveIn() and ff).asDirectionFormula())
        assertNull((tt or "a".positiveIn()).asDirectionFormula())
        assertNull(("a".positiveIn() or ff).asDirectionFormula())
        assertNull((tt implies "a".positiveIn()).asDirectionFormula())
        assertNull(("a".positiveIn() implies ff).asDirectionFormula())
        assertNull((tt equal "a".positiveIn()).asDirectionFormula())
        assertNull(("a".positiveIn() equal ff).asDirectionFormula())
    }

}