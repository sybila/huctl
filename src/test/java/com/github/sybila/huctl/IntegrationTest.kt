package com.github.sybila.huctl

import com.github.sybila.huctl.dsl.*
import com.github.sybila.huctl.parser.toHUCTLp
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

/**
 * Consider this an example of usage
 */
class Integration {

    @Test fun test() {

        val f1 = File.createTempFile("file", ".ctl")

        f1.bufferedWriter().use {
            it.write(":? c = p2 > 3.14 <-> False \n")
            it.write("pop = True EU c EU a \n")
        }

        val input = ":include \"${ f1.absolutePath }\" \n" +
        """
            a = True && p1:out+
            :? f = EF True && EG pop || AX a
        """
        val formulas = input.toHUCTLp()
        val flagged = input.toHUCTLp(onlyFlagged = true)

        val p1 = "p1".toPositiveOut()
        val p2 = ("p2".toVar() gt 3.14.toConst())
        //val np2 =("p2".toVar() le 3.14.toConst())

        val a = Formula.True and p1
        val c = p2 equal Formula.False
        val pop = Formula.True EU (c EU a)
        val f = (EF(Formula.True) and EG(pop)) or AX(a)

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