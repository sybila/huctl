package com.github.sybila.ctl

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Basic {

    val parser = CTLParser()

    @Test
    fun parenthesis() {
        assertEquals(
                True,
                parser.formula("(True)")
        )
    }

    @Test
    fun binaryOps() {
        assertEquals(
                True EU False,
                parser.formula("True EU False")
        )
        assertEquals(
                True AU False,
                parser.formula("True AU False")
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

        //TODO True => False should be a failing error!
    }

    @Test
    fun unaryOps() {
        assertEquals(
                not(True),
                parser.formula("!True")
        )
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
    fun directions() {
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

}