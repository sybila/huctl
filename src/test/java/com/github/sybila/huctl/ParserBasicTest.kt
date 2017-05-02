package com.github.sybila.huctl

import com.github.sybila.huctl.dsl.*
import com.github.sybila.huctl.parser.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Basic {

    private val p1 = !"p1"
    private val p2 = !"p2"
    private val d1 = !!"d1"
    private val d2 = !!"d2"


    @Test
    fun parenthesis() {
        formulaEquals(!"p1", "('p1')")
    }

    @Test
    fun boolOps() {
        formulaEquals(Not(!"p1"), "!'p1'")
        formulaEquals(!"p1" and !"p2", "'p1' && 'p2'")
        formulaEquals(!"p1" or !"p2", "'p1' || 'p2'")
        formulaEquals(!"p1" implies !"p2", "'p1' -> 'p2'")
        formulaEquals(!"p1" equal !"p2", "'p1' <-> 'p2'")
    }

    @Test
    fun untilOps() {

        //basic
        formulaEquals(p1 EU p2, "'p1' EU 'p2'")
        formulaEquals(p1 AU p2, "'p1' AU 'p2'")

        //path direction
        formulaEquals(p1.pEU(p2, d1), "'p1' {'d1'}pEU 'p2'")
        formulaEquals(p1.pAU(p2, d1), "'p1' {'d1'}pAU 'p2'")

        //reach direction
        formulaEquals(p1 EU EX(p2, d1), "'p1' EU{'d1'} 'p2'")
        formulaEquals(p1 AU AX(p2, d1), "'p1' AU{'d1'} 'p2'")
        formulaEquals(p1 pEU pEX(p2, d1), "'p1' pEU{'d1'} 'p2'")
        formulaEquals(p1 pAU pAX(p2, d1), "'p1' pAU{'d1'} 'p2'")

        //both
        formulaEquals(p1.pEU(pEX(p2, d1), d2), "'p1' {'d2'}pEU{'d1'} 'p2'")
        formulaEquals(p1.EU(EX(p2, d1), d2), "'p1' {'d2'}EU{'d1'} 'p2'")
        formulaEquals(p1.pAU(pAX(p2, d1), d2), "'p1' {'d2'}pAU{'d1'} 'p2'")
        formulaEquals(p1.AU(AX(p2, d1), d2), "'p1' {'d2'}AU{'d1'} 'p2'")

    }

    @Test
    fun unaryOps() {

        //basic
        formulaEquals(EX(p1), "EX 'p1'")
        formulaEquals(AX(p1), "AX 'p1'")
        formulaEquals(EF(p1), "EF 'p1'")
        formulaEquals(AF(p1), "AF 'p1'")
        formulaEquals(EG(p1), "EG 'p1'")
        formulaEquals(AG(p1), "AG 'p1'")
        formulaEquals(EwX(p1), "EwX 'p1'")
        formulaEquals(AwX(p1), "AwX 'p1'")
        formulaEquals(EwF(p1), "EwF 'p1'")
        formulaEquals(AwF(p1),"AwF 'p1'")

        //past
        formulaEquals(pEX(p1),"pEX 'p1'")
        formulaEquals(pAX(p1),"pAX 'p1'")
        formulaEquals(pEF(p1),"pEF 'p1'")
        formulaEquals(pAF(p1),"pAF 'p1'")
        formulaEquals(pEG(p1),"pEG 'p1'")
        formulaEquals(pAG(p1),"pAG 'p1'")
        formulaEquals(pEwX(p1),"pEwX 'p1'")
        formulaEquals(pAwX(p1),"pAwX 'p1'")
        formulaEquals(pEwF(p1),"pEwF 'p1'")
        formulaEquals(pAwF(p1),"pAwF 'p1'")

        //transitions
        formulaEquals(pAwF(p1, !!"d1"),"{'d1'}pAwF 'p1'")

    }

    @Test
    fun floats() {
        val v = "var".toVar()
        formulaEquals(v eq !0.0, "var == 0")
        formulaEquals(v eq !1.0,"var == 1")
        formulaEquals(v neq !(-1.0),"var != -1")
        formulaEquals(v gt !0.158,"var > 0.158")
        formulaEquals(v ge !(-0.9995),"var >= -0.9995")
        formulaEquals(v lt !1040.58,"var < 1040.58")
        formulaEquals(v le !(-586.44),"var <= -586.44")
    }

    @Test
    fun firstOrder() {
        formulaEquals(ForAll("x", p1, p2), "forall x in 'p1': 'p2'")
        formulaEquals(Exists("x", p1, p2), "exists x in 'p1': 'p2'")
        formulaEquals(ForAll("x", Formula.True, p1), "forall x: 'p1'")
        formulaEquals(Exists("x", Formula.True, p1), "exists x: 'p1'")
        formulaEquals(Exists("x", Formula.True, "x".toReference()), "exists x: x")
        formulaEquals(ForAll("x", Formula.True, "x".toReference()), "forall x: x")
    }

    @Test
    fun hybrid() {
        formulaEquals(Bind("x", p1), "bind x: 'p1'")
        formulaEquals(Bind("x", "x".toReference()), "bind x: x")
        formulaEquals(Bind("x", At("x", p1)), "bind x: at x: 'p1'")
    }

    @Test
    fun directionProposition() {
        formulaEquals(EX(p1, "x".toIncreasing()), "{x+}EX 'p1'")
        formulaEquals(EX(p1, "x".toDecreasing()), "{x-}EX 'p1'")
        formulaEquals(EX(p1, DirFormula.True), "{True}EX 'p1'")
        formulaEquals(EX(p1, DirFormula.Loop), "{Loop}EX 'p1'")
        formulaEquals(EX(p1, DirFormula.False), "{False}EX 'p1'")
    }

    @Test
    fun directionBool() {
        formulaEquals(EX(p1, Not(d1)), "{!'d1'}EX 'p1'")
        formulaEquals(EX(p1, d1 and d2), "{'d1' && 'd2'}EX 'p1'")
        formulaEquals(EX(p1, d1 or d2), "{'d1' || 'd2'}EX 'p1'")
        formulaEquals(EX(p1, d1 implies d2), "{'d1' -> 'd2'}EX 'p1'")
        formulaEquals(EX(p1, d1 equal d2), "{'d1' <-> 'd2'}EX 'p1'")
    }

    @Test
    fun transitions() {
        formulaEquals("var".toPositiveIn(), "var:in+")
        formulaEquals("var".toPositiveOut(), "var:out+")
        formulaEquals("var".toNegativeIn(), "var:in-")
        formulaEquals("var".toNegativeOut(), "var:out-")
    }

    @Test
    fun floatOps() {
        val v = "var1".toVar()
        val w = "var2".toVar()
        val zero = !0.0
        formulaEquals(((v plus w) eq zero), "var1 + var2 == 0")
        formulaEquals(((v minus w) eq zero), "var1 - var2 == 0")
        formulaEquals(((v times w) eq zero), "var1 * var2 == 0")
        formulaEquals(((v div w) eq zero), "var1 / var2 == 0")
    }

    @Test
    fun comments() {
        var result = """
            //f = False
            k = True
            //l = False""".toHUCTLp()
        assertEquals(1, result.size)
        assertEquals(Formula.True, result["k"])

        result = """
            /* Some redundant text
            f = False
            */
            k = True
            //l = False""".toHUCTLp()
        assertEquals(1, result.size)
        assertEquals(Formula.True, result["k"])

        result = """
            k = True
            # Python style comment
            f = False""".toHUCTLp()
        assertEquals(2, result.size)
        assertEquals(Formula.True, result["k"])
        assertEquals(Formula.False, result["f"])

        result = """
            /* Comment f = False
                /* With nesting */
            */
            k = True
            //l = False""".toHUCTLp()
        assertEquals(1, result.size)
        assertEquals(Formula.True, result["k"])

        result = """
            k = True
        //comment at the end of file""".toHUCTLp()
        assertEquals(1, result.size)
        assertEquals(Formula.True, result["k"])
    }

    @Test
    fun booleans() {
        val True = Formula.True
        val False = Formula.False
        formulaEquals(True, "true")
        formulaEquals(True, "True")
        formulaEquals(True, "tt")
        formulaEquals(False, "false")
        formulaEquals(False, "False")
        formulaEquals(False, "ff")
    }

    @Test
    fun invalidInput() {
        assertFailsWith<IllegalArgumentException> {
            """
                l = True && False
                k = tr&ue && False
            """.toHUCTLp()
        }
    }

    @Test
    fun flaggedFormulasParseAll() {
        val result = """
            foo = True
            :? goo = False""".toHUCTLp(onlyFlagged = false)
        assertEquals(2, result.size)
        assertEquals(Formula.True, result["foo"])
        assertEquals(Formula.False, result["goo"])
    }

    @Test
    fun flaggedFormulas() {
        val result = """
            foo = True
            :? goo = False""".toHUCTLp(onlyFlagged = true)
        assertEquals(1, result.size)
        assertEquals(Formula.False, result["goo"])
    }

    @Test
    fun dirFormulaParse() {
        assertEquals(!!"d1" and "x".toIncreasing(), "'d1' && x+".toDirFormula())
        assertEquals(!!"d1" and "x".toIncreasing(), readDirFormula("'d1' && x+"))
    }

    @Test
    fun dirFormulaInvalidParse() {
        assertFailsWith<IllegalArgumentException> {
            "True && EX False".toDirFormula()
        }
    }


    @Test
    fun garbageParse() {
        assertFailsWith<IllegalArgumentException> {
            " foo ".toHUCTLp()
        }
    }

    @Test
    fun invalidParse() {
        assertFailsWith<IllegalArgumentException> {
            "a = AF ((True)".toHUCTLp()
        }
    }

    @Test
    fun expressionParse() {
        assertEquals("a".toVar() plus !3.14, "a + 3.14".toExpression())
        assertEquals("c".toVar() minus (!4.0 + !2.2), readExpression("c - (4.0 + 2.2)"))
    }

}