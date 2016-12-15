package com.github.sybila.huctl

import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Complex {

    val parser = HUCTLParser()


    val p1 = ("p1".asVariable() eq 4.0.asConstant())
    val p2 = "p2".negativeIn()
    val p3 = (("p3".asVariable() div 1.3.asConstant()) plus 2.0.asConstant() lt
            (((-3.14).asConstant() plus (12.0.asConstant() minus "f".asVariable())) times 2.0.asConstant())
    )

    @Test
    fun complexFiles() {

        val f1 = File.createTempFile("file", ".ctl")
        val f2 = File.createTempFile("file2", ".ctl")
        val f3 = File.createTempFile("file3", ".ctl")

        f1.bufferedWriter().use {
            it.write("c = p2:in- || EX z + 2 < x*2 EU a")
        }

        f2.bufferedWriter().use {
            it.write(":include \"${ f1.absolutePath }\" \n")
            it.write("b = EX c <-> True \n")
            it.write("d = e -> e \n")
            it.write("q = 12 - f \n")
            it.write("x = -3.14 + q \n")
        }

        f3.bufferedWriter().use {
            it.write("a = ! p1 == 4 \n")
            it.write("z = p3 / 1.3 \n")
            it.write(":include \"${ f2.absolutePath }\" \n")
            it.write("e = True && c || c && False")
        }

        val result = parser.parse(f3)

        val a = not(p1)
        val c = (p2 or EX(p3)) EU a
        val b = EX(c) equal True
        val e = (True and c) or (c and False)
        val d = e implies e

        assertEquals(5, result.size)
        assertEquals(a, result["a"])
        assertEquals(b, result["b"])
        assertEquals(c, result["c"])
        assertEquals(d, result["d"])
        assertEquals(e, result["e"])

        f1.delete()
        f2.delete()
        f3.delete()

    }

    @Test
    fun complexString() {

        val result = parser.parse("""
            dir = true || x+
            z = p3
            x = 12 - f
            b = EX z/1.3 + 2 < (-3.14 + x) * 2 EU a
            a = (p1 == 4) && p2:in-
            d = {(y-) -> dir}AG {dir}AX c
            c = b <-> b
            e = c && (forall x: True) || (exists y in d: False) && b
        """)

        val dir = DirectionFormula.Atom.True or "x".increase()
        val a = p1 and p2
        val b = EX(p3) EU a
        val c = b equal b
        val d = AG(AX(c, dir), "y".decrease() implies dir)
        val e = (c and forall("x", True, True)) or (exists("y", d, False) and b)

        assertEquals(5, result.size)
        assertEquals(a, result["a"])
        assertEquals(b, result["b"])
        assertEquals(c, result["c"])
        assertEquals(d, result["d"])
        assertEquals(e, result["e"])

    }

    @Test
    fun operatorAssociativity() {
        //These binary operators are by convention right-associative
        //Other binary operators, such as &&, ||, <=> are associative,
        //so it doesn't matter if we resolve them left to right or right to left
        // +, -, *, / are also associative
        assertEquals(
                True EU (False EU True),
                parser.formula("True EU False EU True")
        )
        assertEquals(
                True AU (False AU True),
                parser.formula("True AU False AU True")
        )
        assertEquals(
                True implies (False implies True),
                parser.formula("True -> False -> True")
        )
    }

    @Test
    fun directionOperatorAssociativity() {
        //Implication is right associative!
        assertEquals(
                EX(True, ("z".increase() implies ("x".increase() implies "y".decrease()))),
                parser.formula("{z+ -> x+ -> y-}EX True")
        )
    }

    @Test
    fun operatorPriority() {
        //we do not test every combination, since priority should be transitive
        assertEquals(
                not(False) and not(True),
                parser.formula("!False && !True")
        )
        assertEquals(
                (True and False) or (False and True),
                parser.formula("True && False || False && True")
        )
        assertEquals(
                (True or False) implies (False or True),
                parser.formula("True || False -> False || True")
        )
        assertEquals(
                (True implies False) equal (False implies True),
                parser.formula("True -> False <-> False -> True")
        )
        assertEquals(
                (True equal False) EU (False equal True),
                parser.formula("True <-> False EU False <-> True")
        )
        assertEquals(
                (True EU False) AU (False EU True),
                parser.formula("True EU False AU False EU True")
        )
        assertEquals(
                forall("x", False, (True or False) AU (True and exists("y", True, False))),
                parser.formula("forall x in False: True || False AU True && exists y in True: False")
        )
    }

    @Test
    fun directionOperatorPriority() {
        //we do not test every combination, since priority should be transitive
        val xp = "x".increase()
        val yd = "y".decrease()
        assertEquals(
                EX(True, not(xp) and not(yd)),
                parser.formula("{!x+ && !y-} EX true")
        )
        assertEquals(
                EX(True, (xp and yd) or (yd and xp)),
                parser.formula("{x+ && y- || y- && x+} EX True")
        )
        assertEquals(
                EX(True, (xp or yd) implies (yd or xp)),
                parser.formula("{x+ || y- -> y- || x+} EX True")
        )
        assertEquals(
                EX(True, (xp implies yd) equal (yd implies xp)),
                parser.formula("{x+ -> y- <-> y- -> x+} EX True")
        )
    }

    @Test
    fun expressionOperatorPriority() {
        //We don't care about priority of * vs. / and + vs. -
        val three = 3.0.asConstant()
        assertEquals(((three times three) plus three eq 0.0.asConstant()),
                parser.formula("3 * 3 + 3 == 0"))
        assertEquals(((three times three) minus three eq 0.0.asConstant()),
                parser.formula("3 * 3 - 3 == 0"))
        assertEquals(((three div three) plus three eq 0.0.asConstant()),
                parser.formula("3 / 3 + 3 == 0"))
        assertEquals(((three div three) minus three eq 0.0.asConstant()),
                parser.formula("3 / 3 - 3 == 0"))
    }

    @Test
    fun ambiguousFormulas() {
        val r = parser.parse("""
            k = True && False
            l = {k}EX True
            m = k || False
        """)
        assertEquals(3, r.size)
        assertEquals(True and False, r["k"])
        assertEquals(EX(True, True.asDirectionFormula()!! and False.asDirectionFormula()!!), r["l"])
        assertEquals((True and False) or False, r["m"])
    }

    @Test
    fun formulaCastError() {
        //direction to formula
        assertFailsWith<IllegalStateException> {
            parser.parse("""
                k = x+ || y-
                l = EX k
            """)
        }
        //formula to direction
        assertFailsWith<IllegalStateException> {
            parser.parse("""
                k = EX True
                l = {k}EF False
            """)
        }
        //expression to formula
        assertFailsWith<IllegalStateException> {
            parser.parse("""
                k = a + b
                l = EX k
            """)
        }
    }

}

