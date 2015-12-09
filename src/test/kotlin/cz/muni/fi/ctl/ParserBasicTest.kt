package cz.muni.fi.ctl

import org.junit.Test
import kotlin.test.assertEquals

class Basic {

    val parser = Parser()

    @Test fun parenthesis() {
        assertEquals(
                True,
                parser.formula("(True)")
        )
    }

    @Test fun binaryOps() {
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
                parser.formula("True => False")
        )
        assertEquals(
                True equal False,
                parser.formula("True <=> False")
        )
    }

    @Test fun unaryOps() {
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

    @Test fun floats() {
        assertEquals(
                FloatProposition("var", CompareOp.EQ, 0.0),
                parser.formula("var == 0")
        )
        assertEquals(
                FloatProposition("var", CompareOp.EQ, 1.0),
                parser.formula("var == 1")
        )
        assertEquals(
                FloatProposition("var", CompareOp.NEQ, -1.0),
                parser.formula("var != -1")
        )
        assertEquals(
                FloatProposition("var", CompareOp.GT, 0.158),
                parser.formula("var > 0.158")
        )
        assertEquals(
                FloatProposition("var", CompareOp.GT_EQ, -0.9995),
                parser.formula("var >= -0.9995")
        )
        assertEquals(
                FloatProposition("var", CompareOp.LT, 1040.58),
                parser.formula("var < 1040.58")
        )
        assertEquals(
                FloatProposition("var", CompareOp.LT_EQ, -586.44),
                parser.formula("var <= -586.44")
        )
    }

    @Test fun directions() {
        assertEquals(
                DirectionProposition("var", Direction.IN, Facet.POSITIVE),
                parser.formula("var:in+")
        )
        assertEquals(
                DirectionProposition("var", Direction.OUT, Facet.POSITIVE),
                parser.formula("var:out+")
        )
        assertEquals(
                DirectionProposition("var", Direction.IN, Facet.NEGATIVE),
                parser.formula("var:in-")
        )
        assertEquals(
                DirectionProposition("var", Direction.OUT, Facet.NEGATIVE),
                parser.formula("var:out-")
        )
    }

    @Test fun booleans() {
        assertEquals(True, parser.formula("true"))
        assertEquals(True, parser.formula("True"))
        assertEquals(True, parser.formula("tt"))
        assertEquals(False, parser.formula("false"))
        assertEquals(False, parser.formula("False"))
        assertEquals(False, parser.formula("ff"))
    }

}