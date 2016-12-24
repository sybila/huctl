package com.github.sybila.huctl

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Basic {

    val parser = HUCTLParser()

    @Test
    fun parenthesis() {
        assertEquals(
                True,
                parser.formula("(True)")
        )
    }

    @Test
    fun boolOps() {
        assertEquals(
                not(True),
                parser.formula("!True")
        )
        assertEquals(
                True and False,
                parser.formula("True && False")
        )
        assertEquals(
                True or False,
                parser.formula("True || False")
        )
        assertEquals(
                True implies False,
                parser.formula("True -> False")
        )
        assertEquals(
                True equal False,
                parser.formula("True <-> False")
        )
    }

    @Test
    fun untilOps() {
        //basic
        assertEquals(
                True EU False,
                parser.formula("True EU False")
        )
        assertEquals(
                True AU False,
                parser.formula("True AU False")
        )
        //path direction
        assertEquals(
                True.pastEU(False, True.asDirectionFormula()!!),
                parser.formula("True {True}pEU False")
        )
        assertEquals(
                True.pastAU(False, True.asDirectionFormula()!!),
                parser.formula("True {True}pAU False")
        )
        //reach direction
        assertEquals(
                True EU (EX(False, False.asDirectionFormula()!!)),
                parser.formula("True EU{False} False")
        )
        assertEquals(
                True pastEU (pastEX(False, False.asDirectionFormula()!!)),
                parser.formula("True pEU{False} False")
        )
        assertEquals(
                True AU (AX(False, False.asDirectionFormula()!!)),
                parser.formula("True AU{False} False")
        )
        assertEquals(
                True pastAU (pastAX(False, False.asDirectionFormula()!!)),
                parser.formula("True pAU{False} False")
        )
        //both
        assertEquals(
                True.pastEU(pastEX(False, False.asDirectionFormula()!!), True.asDirectionFormula()!!),
                parser.formula("True {True}pEU{False} False")
        )
        assertEquals(
                True.EU(EX(False, False.asDirectionFormula()!!), True.asDirectionFormula()!!),
                parser.formula("True {True}EU{False} False")
        )
        assertEquals(
                True.pastAU(pastAX(False, False.asDirectionFormula()!!), True.asDirectionFormula()!!),
                parser.formula("True {True}pAU{False} False")
        )
        assertEquals(
                True.AU(AX(False, False.asDirectionFormula()!!), True.asDirectionFormula()!!),
                parser.formula("True {True}AU{False} False")
        )
    }

    @Test
    fun unaryOps() {
        //basic
        assertEquals(
                EX(True),
                parser.formula("EX True")
        )
        assertEquals(
                AX(True),
                parser.formula("AX True")
        )
        assertEquals(
                EF(True),
                parser.formula("EF True")
        )
        assertEquals(
                AF(True),
                parser.formula("AF True")
        )
        assertEquals(
                EG(True),
                parser.formula("EG True")
        )
        assertEquals(
                AG(True),
                parser.formula("AG True")
        )
        assertEquals(
                weakEX(True),
                parser.formula("EwX True")
        )
        assertEquals(
                weakAX(True),
                parser.formula("AwX True")
        )
        assertEquals(
                weakEF(True),
                parser.formula("EwF True")
        )
        assertEquals(
                weakAF(True),
                parser.formula("AwF True")
        )
        //past
        assertEquals(
                pastEX(True),
                parser.formula("pEX True")
        )
        assertEquals(
                pastAX(True),
                parser.formula("pAX True")
        )
        assertEquals(
                pastEF(True),
                parser.formula("pEF True")
        )
        assertEquals(
                pastAF(True),
                parser.formula("pAF True")
        )
        assertEquals(
                pastEG(True),
                parser.formula("pEG True")
        )
        assertEquals(
                pastAG(True),
                parser.formula("pAG True")
        )
        assertEquals(
                pastWeakEX(True),
                parser.formula("pEwX True")
        )
        assertEquals(
                pastWeakAX(True),
                parser.formula("pAwX True")
        )
        assertEquals(
                pastWeakEF(True),
                parser.formula("pEwF True")
        )
        assertEquals(
                pastWeakAF(True),
                parser.formula("pAwF True")
        )
        //transitions
        assertEquals(
                pastWeakAF(True, False.asDirectionFormula()!!),
                parser.formula("{False}pAwF True")
        )
    }

    @Test
    fun floats() {
        val v = "var".asVariable()
        assertEquals(
                (v eq 0.0.asConstant()),
                parser.formula("var == 0")
        )
        assertEquals(
                (v eq 1.0.asConstant()),
                parser.formula("var == 1")
        )
        assertEquals(
                (v neq (-1.0).asConstant()),
                parser.formula("var != -1")
        )
        assertEquals(
                (v gt 0.158.asConstant()),
                parser.formula("var > 0.158")
        )
        assertEquals(
                (v ge (-0.9995).asConstant()),
                parser.formula("var >= -0.9995")
        )
        assertEquals(
                (v lt 1040.58.asConstant()),
                parser.formula("var < 1040.58")
        )
        assertEquals(
                (v le (-586.44).asConstant()),
                parser.formula("var <= -586.44")
        )
    }

    @Test
    fun firstOrder() {
        assertEquals(
                forall("x", True, False), parser.formula("forall x: False")
        )
        assertEquals(
                forall("x", False, False), parser.formula("forall x in False: False")
        )
        assertEquals(
                exists("x", True, False), parser.formula("exists x: False")
        )
        assertEquals(
                exists("x", False, False), parser.formula("exists x in False: False")
        )
        assertEquals(
                exists("x", True, "x".asReference()), parser.formula("exists x: x")
        )
        assertEquals(
                forall("x", True, "x".asReference()), parser.formula("forall x: x")
        )
    }

    @Test
    fun hybrid() {
        assertEquals(
                bind("x", True), parser.formula("bind x: True")
        )
        assertEquals(
                bind("x", "x".asReference()), parser.formula("bind x: x")
        )
        assertEquals(
                bind("x", at("x", True)), parser.formula("bind x: at x: True")
        )
    }

    @Test
    fun directionProposition() {
        assertEquals(EX(True, "x".increase()), parser.formula("{x+}EX True"))
        assertEquals(EX(True, "x".decrease()), parser.formula("{x-}EX True"))
        assertEquals(EX(True, True.asDirectionFormula()!!), parser.formula("{True}EX True"))
        assertEquals(EX(True, DirectionFormula.Atom.Loop), parser.formula("{Loop}EX True"))
        assertEquals(EX(True, False.asDirectionFormula()!!), parser.formula("{False}EX True"))
    }

    @Test
    fun directionBool() {
        assertEquals(EX(True, not("x".increase())), parser.formula("{!x+}EX True"))
        assertEquals(EX(True, "x".increase() and "y".decrease()), parser.formula("{x+ && y-}EX True"))
        assertEquals(EX(True, "x".increase() or "y".decrease()), parser.formula("{x+ || y-}EX True"))
        assertEquals(EX(True, "x".increase() implies "y".decrease()), parser.formula("{x+ -> y-}EX True"))
        assertEquals(EX(True, "x".increase() equal "y".decrease()), parser.formula("{x+ <-> y-}EX True"))
    }

    @Test
    fun transitions() {
        assertEquals(
                "var".positiveIn(),
                parser.formula("var:in+")
        )
        assertEquals(
                "var".positiveOut(),
                parser.formula("var:out+")
        )
        assertEquals(
                "var".negativeIn(),
                parser.formula("var:in-")
        )
        assertEquals(
                "var".negativeOut(),
                parser.formula("var:out-")
        )
    }

    @Test
    fun floatOps() {
        val v = "var1".asVariable()
        val w = "var2".asVariable()
        val zero = 0.0.asConstant()
        assertEquals(
                ((v plus w) eq zero),
                parser.formula("var1 + var2 == 0")
        )
        assertEquals(
                ((v minus w) eq zero),
                parser.formula("var1 - var2 == 0")
        )
        assertEquals(
                ((v times w) eq zero),
                parser.formula("var1 * var2 == 0")
        )
        assertEquals(
                ((v div w) eq zero),
                parser.formula("var1 / var2 == 0")
        )
    }

    @Test
    fun comments() {
        var result = parser.parse("""
            //f = False
            k = True
            //l = False
        """)
        assertEquals(1, result.size)
        assertEquals(True, result["k"])

        result = parser.parse("""
            /* Some redundant text
            f = False
            */
            k = True
            //l = False
        """)
        assertEquals(1, result.size)
        assertEquals(True, result["k"])

        result = parser.parse("""
            k = True
            # Python style comment
            f = False
        """)
        assertEquals(2, result.size)
        assertEquals(True, result["k"])
        assertEquals(False, result["f"])

        result = parser.parse("""
            /* Comment f = False
                /* With nesting */
            */
            k = True
            //l = False
        """)
        assertEquals(1, result.size)
        assertEquals(True, result["k"])

        result = parser.parse("""
            k = True
        //comment at the end of file""")
        assertEquals(1, result.size)
        assertEquals(True, result["k"])
    }

    @Test
    fun booleans() {
        assertEquals(True, parser.formula("true"))
        assertEquals(True, parser.formula("True"))
        assertEquals(True, parser.formula("tt"))
        assertEquals(False, parser.formula("false"))
        assertEquals(False, parser.formula("False"))
        assertEquals(False, parser.formula("ff"))
    }

    @Test
    fun invalidInput() {
        assertFailsWith<IllegalArgumentException> {
            parser.parse("""
                l = True && False
                k = tr&ue && False
            """)
        }
    }

    @Test
    fun flaggedFormulasParseAll() {
        val result = parser.parse("""
            foo = True
            :? goo = False
        """, onlyFlagged = false)
        assertEquals(2, result.size)
        assertEquals(True, result["foo"])
        assertEquals(False, result["goo"])
    }

    @Test
    fun flaggedFormulas() {
        val result = parser.parse("""
            foo = True
            :? goo = False
        """, onlyFlagged = true)
        assertEquals(1, result.size)
        assertEquals(False, result["goo"])
    }

    @Test
    fun dirFormulaParse() {
        val r = parser.dirFormula("True && x+")
        assertEquals(True.asDirectionFormula()!! and "x".increase(), r)
    }

    @Test
    fun dirFormulaInvalidParse() {
        assertFailsWith<IllegalStateException> {
            parser.dirFormula("True && EX False")
        }
    }

    @Test
    fun atomParse() {
        assertEquals("x".positiveIn(), parser.atom("x:in+"))
        assertEquals("x".asVariable() gt 3.4.asConstant(), parser.atom("x > 3.4"))
        assertEquals(True, parser.atom("true"))
    }

    @Test
    fun atomInvalidParse() {
        assertFailsWith<IllegalStateException> {
            parser.atom("EX x > 2.2")
        }
    }

    @Test
    fun dirAtomParse() {
        assertEquals("x".increase(), parser.dirAtom("x+"))
        assertEquals(True.asDirectionFormula()!!, parser.dirAtom("true"))
    }

    @Test
    fun dirAtomInvalidParse() {
        assertFailsWith<IllegalStateException> {
            parser.dirAtom("x+ && y-")
        }
    }

}