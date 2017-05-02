package com.github.sybila.huctl

import com.github.sybila.huctl.dsl.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class Misc {

    @Test
    fun booleanToString() {
        assertEquals("true", Formula.True.toString())
        assertEquals("false", Formula.False.toString())
    }

    @Test
    fun variableToString() {
        assertEquals("test", "test".toVar().toString())
    }

    @Test
    fun constantToString() {
        assertEquals("3.140000", (!3.14).toString())
    }

    @Test
    fun expressionToString() {
        assertEquals("((a + 12.000000) / ((3.000000 * 4.000000) - Var))",
                (
                        ("a".toVar() plus !12.0)
                                div
                        ((!3.0 times !4.0) minus "Var".toVar())
                ).toString())
    }

    @Test
    fun ctlFormulaToString() {
        assertEquals(
                "(!true && ({true}pEwX (false {true}EU true)))",
                (Not(Formula.True) and pEwX(Formula.False EU Formula.True)).toString()
        )
    }

    @Test
    fun hybridFormulaToString() {
        assertEquals("(at x : (bind y : true))", At("x", Bind("y", Formula.True)).toString())
    }

    @Test
    fun firstOrderFormulaToString() {
        assertEquals(
                "(forall x in false : (exists y in true : false))",
                ForAll("x", Formula.False, Exists("y", Formula.True, Formula.False)).toString()
        )
    }

    @Test
    fun floatPropositionToString() {
        val prop = ("prop".toVar() gt !5.3)
        assertEquals("(prop > 5.300000)", prop.toString())
    }

    @Test
    fun directionToString() {
        assertEquals("prop:in+", "prop".toPositiveIn().toString())
    }

    @Test
    fun basicProperties() {
        val v1 = "v1".toVar()
        val v2 = "v2".toVar()
        assertNotEquals(v1.hashCode(), v2.hashCode())
        assertNotEquals(v1, v2)

        val prop1 = ("prop1".toVar() gt !5.3)
        val prop2 = ("prop2".toVar() gt (!54.3 plus !3.2))
        assertNotEquals(prop1.hashCode(), prop2.hashCode())
        assertNotEquals(prop1, prop2)

        val dir1 = "v1".toPositiveIn()
        val dir2 = "v1".toNegativeOut()
        assertNotEquals(dir1.hashCode(), dir2.hashCode())
        assertNotEquals(dir1, dir2)
        assertEquals("v1", dir1.name)
        assertEquals(Direction.POSITIVE, dir1.direction)
        assertEquals(Flow.IN, dir1.flow)

        val u = (prop1 EU prop2) as Formula.Until
        assertEquals(prop1, u.path)
        assertEquals(prop2, u.reach)
    }

}
