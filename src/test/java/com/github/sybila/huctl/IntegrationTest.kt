package com.github.sybila.huctl

import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

/**
 * Consider this an example of usage
 */
class Integration {

    @Test fun test() {

        val f1 = File.createTempFile("file", ".ctl")

        val parser = HUCTLParser()

        f1.bufferedWriter().use {
            it.write(":? c = p2 > 3.14 <-> False \n")
            it.write("pop = True EU c EU a \n")
        }

        val input = ":include \"${ f1.absolutePath }\" \n" +
        """
            a = True && p1:out+
            :? f = EF True && EG pop || AX a
        """
        val formulas = parser.parse(input)
        val flagged = parser.parse(input, onlyFlagged = true)

        val p1 = "p1".positiveOut()
        val p2 = ("p2".asVariable() gt 3.14.asConstant())
        val np2 =("p2".asVariable() le 3.14.asConstant())

        val a = True and p1
        val c = p2 equal False
        val pop = True EU (c EU a)
        val f = (EF(True) and EG(pop)) or AX(a)

        assertEquals(4, formulas.size)
        assertEquals(a, formulas["a"])
        assertEquals(c, formulas["c"])
        assertEquals(pop, formulas["pop"])
        assertEquals(f, formulas["f"])
        assertEquals(2, flagged.size)
        assertEquals(f, flagged["f"])
        assertEquals(c, flagged["c"])
    }

}