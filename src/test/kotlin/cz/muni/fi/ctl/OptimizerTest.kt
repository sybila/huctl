package cz.muni.fi.ctl

import org.junit.Test
import kotlin.test.assertEquals

class OptimizerTest {

    private val p1 = Reference("p1")
    private val p2 = Reference("p2")

    @Test fun complexTest() {
        val f1 = FloatProposition("var2", CompareOp.LT_EQ, -15.3)
        val nf1 = FloatProposition("var2", CompareOp.GT, -15.3)
        val d1 = DirectionProposition("var1", Direction.IN, Facet.NEGATIVE)

        val prop = p1 AU EX(not(f1) EU not(True and ( not(d1) or (p1 AU False))))
        val optimized = prop.optimize()

        assertEquals(p1 AU EX(nf1 EU d1), optimized)
        assertEquals(optimized, optimized.optimize())
    }

    @Test fun orNegation() {
        assertEquals(not(p1 and p2), (not(p1) or not(p2)).optimize())
    }

    @Test fun andNegation() {
        assertEquals(not(p1 or p2), (not(p1) and not(p2)).optimize())
    }

    @Test fun floatNegation() {
        assertEquals(
                FloatProposition("val", CompareOp.NEQ, 13.2),
                not(FloatProposition("val", CompareOp.EQ, 13.2)).optimize()
        )
        assertEquals(
                FloatProposition("val", CompareOp.LT_EQ, 13.2),
                not(FloatProposition("val", CompareOp.GT, 13.2)).optimize()
        )
    }

    @Test fun doubleNegation() {
        assertEquals(p1, not(not(p1)).optimize())
        assertEquals(not(p2), not(not(not(p2))).optimize())
    }

    @Test fun booleanReduction() {
        val prop = EX( not(False) and ( (True or False) EU (False AU True) ))
        assertEquals(True, prop.optimize())
    }

    @Test fun preserveUnsupported() {
        val prop = AX(EG(p1 implies p2))
        assertEquals(prop, prop.optimize())
    }

    @Test fun floatPreserve() {
        val prop = FloatProposition("val", CompareOp.GT_EQ, 32.2)
        assertEquals(prop, prop.optimize())
    }

    @Test fun booleanPreserve() {
        assertEquals(True, True.optimize())
        assertEquals(False, False.optimize())
    }

    @Test fun directionPreserve() {
        val prop = DirectionProposition("var", Direction.IN, Facet.POSITIVE)
        assertEquals(prop, prop.optimize())
    }

}