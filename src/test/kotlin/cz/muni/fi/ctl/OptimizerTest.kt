package cz.muni.fi.ctl

import org.junit.Test
import kotlin.test.assertEquals

class OptimizerTest {

    val p1 = Reference("p1")
    val p2 = Reference("p2")

    val optimizer = Optimizer()

    @Test fun complexTest() {
        val f1 = FloatProposition("var2", CompareOp.LT_EQ, -15.3)
        val nf1 = FloatProposition("var2", CompareOp.GT, -15.3)
        val d1 = DirectionProposition("var1", Direction.IN, Facet.NEGATIVE)

        val prop = p1 AU EX(not(f1) EU not(True and ( not(d1) or (p1 AU False))))
        val optimized = optimizer.optimize(prop)

        assertEquals(p1 AU EX(nf1 EU d1), optimized)
        assertEquals(optimized, optimizer.optimize(optimized))
    }

    @Test fun orNegation() {
        assertEquals(not(p1 and p2), optimizer.optimize(not(p1) or not(p2)))
    }

    @Test fun andNegation() {
        assertEquals(not(p1 or p2), optimizer.optimize(not(p1) and not(p2)))
    }

    @Test fun floatNegation() {
        assertEquals(
                FloatProposition("val", CompareOp.NEQ, 13.2),
                optimizer.optimize(
                        not(FloatProposition("val", CompareOp.EQ, 13.2))
                )
        )
        assertEquals(
                FloatProposition("val", CompareOp.LT_EQ, 13.2),
                optimizer.optimize(
                        not(FloatProposition("val", CompareOp.GT, 13.2))
                )
        )
    }

    @Test fun doubleNegation() {
        assertEquals(p1, optimizer.optimize(not(not(p1))))
        assertEquals(not(p2), optimizer.optimize(not(not(not(p2)))))
    }

    @Test fun booleanReduction() {
        val prop = EX( not(False) and ( (True or False) EU (False AU True) ))
        assertEquals(True, optimizer.optimize(prop))
    }

    @Test fun preserveUnsupported() {
        val prop = AX(EG(p1 implies p2))
        assertEquals(prop, optimizer.optimize(prop))
    }

    @Test fun floatPreserve() {
        val prop = FloatProposition("val", CompareOp.GT_EQ, 32.2)
        assertEquals(prop, optimizer.optimize(prop))
    }

    @Test fun booleanPreserve() {
        assertEquals(True, optimizer.optimize(True))
        assertEquals(False, optimizer.optimize(False))
    }

    @Test fun directionPreserve() {
        val prop = DirectionProposition("var", Direction.IN, Facet.POSITIVE)
        assertEquals(prop, optimizer.optimize(prop))
    }

}