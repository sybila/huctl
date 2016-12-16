package com.github.sybila.ctl

import org.junit.Test
import kotlin.test.assertEquals

class OptimizerTest {

    private val p1 = Proposition.Reference("p1")
    private val p2 = Proposition.Reference("p2")

    @Test fun complexTest() {
        val f1 = ("var2".asVariable() le (-15.3).asConstant())
        val nf1 = ("var2".asVariable() gt (-15.3).asConstant())
        val d1 = "var1".negativeIn()

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
                ("val".asVariable() neq 13.2.asConstant()).asAtom(),
                not(("val".asVariable() eq 13.2.asConstant())).optimize()
        )
        assertEquals(
                ("val".asVariable() eq 13.2.asConstant()).asAtom(),
                not(("val".asVariable() neq 13.2.asConstant())).optimize()
        )
        assertEquals(
                ("val".asVariable() ge 13.2.asConstant()).asAtom(),
                not(("val".asVariable() lt 13.2.asConstant())).optimize()
        )
        assertEquals(
                ("val".asVariable() gt 13.2.asConstant()).asAtom(),
                not(("val".asVariable() le 13.2.asConstant())).optimize()
        )
        assertEquals(
                ("val".asVariable() le 13.2.asConstant()).asAtom(),
                not(("val".asVariable() gt 13.2.asConstant())).optimize()
        )
        assertEquals(
                ("val".asVariable() lt 13.2.asConstant()).asAtom(),
                not(("val".asVariable() ge 13.2.asConstant())).optimize()
        )
    }

    @Test fun doubleNegation() {
        assertEquals(p1.asAtom(), not(not(p1)).optimize())
        assertEquals(not(p2), not(not(not(p2))).optimize())
    }

    @Test fun booleanReduction() {
        val prop = EX(not(False) and ( (True or False) EU (False AU True) ))
        assertEquals(True, prop.optimize())
    }

    @Test fun preserveUnsupported() {
        val prop = AX(EG(p1 implies p2))
        assertEquals(prop, prop.optimize())
    }

    @Test fun floatPreserve() {
        val prop = ("val".asVariable() ge 32.2.asConstant()).asAtom()
        assertEquals(prop, prop.optimize())
    }

    @Test fun booleanPreserve() {
        assertEquals(True, True.optimize())
        assertEquals(False, False.optimize())
    }

    @Test fun directionPreserve() {
        val prop = "var".positiveIn().asAtom()
        assertEquals(prop, prop.optimize())
    }

    @Test
    fun cannotOptimize() {
        val p = "val".negativeIn()
        val q = "var".positiveIn()
        val prop = (p and q) or (p and not(q))
        assertEquals(prop, prop.optimize())
    }

}