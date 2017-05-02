package com.github.sybila.huctl

import com.github.sybila.huctl.dsl.*
import com.github.sybila.huctl.parser.toHUCTLp
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class Complex {

    val p1 = ("p1".toVar() eq !4.0)
    val p2 = "p2".toNegativeIn()
    val p3 = (("p3".toVar() div !1.3) plus !2.0 lt
            ((!(-3.14) plus (!12.0 minus "f".toVar())) times !2.0)
            )
    val True = Formula.True
    val False = Formula.False

    @Test
    fun complexFiles() {

        val f1 = File.createTempFile("file", ".ctl")
        val f2 = File.createTempFile("file2", ".ctl")
        val f3 = File.createTempFile("file3", ".ctl")

        f1.bufferedWriter().use {
            it.write("c = p2:in- || EX z + 2 < x*2 EU a")
        }

        f2.bufferedWriter().use {
            it.write(":include \"${f1.absolutePath}\" \n")
            it.write("b = EX c <-> True \n")
            it.write("d = e -> e \n")
            it.write("q = 12 - f \n")
            it.write("x = -3.14 + q \n")
        }

        f3.bufferedWriter().use {
            it.write("a = ! p1 == 4 \n")
            it.write("z = p3 / 1.3 \n")
            it.write(":include \"${f2.absolutePath}\" \n")
            it.write("e = True && c || c && False")
        }

        val result = f3.toHUCTLp()

        val a = Not(p1)
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

        val result = """
            dir = true || x+
            z = p3
            x = 12 - f
            b = EX z/1.3 + 2 < (-3.14 + x) * 2 EU a
            a = (p1 == 4) && p2:in-
            d = {(y-) -> dir}AG {dir}AX c
            c = b <-> b
            e = c && (forall x: True) || (exists y in d: False) && b
        """.toHUCTLp()

        val dir = DirFormula.True or "x".toIncreasing()
        val a = p1 and p2
        val b = EX(p3) EU a
        val c = b equal b
        val d = AG(AX(c, dir), "y".toDecreasing() implies dir)
        val e = (c and ForAll("x", True, True)) or (Exists("y", d, False) and b)

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
        formulaEquals(True EU (False EU True), "True EU False EU True")
        formulaEquals(True AU (False AU True), "True AU False AU True")
        formulaEquals(True implies (False implies True), "True -> False -> True")
    }

    @Test
    fun directionOperatorAssociativity() {
        //Implication is right associative!
        formulaEquals(
                EX(True, ("z".toIncreasing() implies ("x".toIncreasing() implies "y".toDecreasing()))),
                "{z+ -> x+ -> y-}EX True"
        )
    }

    @Test
    fun operatorPriority() {
        //we do not test every combination, since priority should be transitive
        formulaEquals(Not(False) and Not(True), "!False && !True")
        formulaEquals((True and False) or (False and True), "True && False || False && True")
        formulaEquals((True or False) implies (False or True), "True || False -> False || True")
        formulaEquals((True implies False) equal (False implies True), "True -> False <-> False -> True")
        formulaEquals((True equal False) EU (False equal True), "True <-> False EU False <-> True")
        formulaEquals((True EU False) AU (False EU True), "True EU False AU False EU True")
        formulaEquals(
                ForAll("x", False, (True or False) AU (True and Exists("y", True, False))),
                "forall x in False: True || False AU True && exists y in True: False"
        )
    }

    @Test
    fun directionOperatorPriority() {
        //we do not test every combination, since priority should be transitive
        val xp = "x".toIncreasing()
        val yd = "y".toDecreasing()
        formulaEquals(EX(True, Not(xp) and Not(yd)), "{!x+ && !y-} EX true")
        formulaEquals(EX(True, (xp and yd) or (yd and xp)), "{x+ && y- || y- && x+} EX True")
        formulaEquals(EX(True, (xp or yd) implies (yd or xp)), "{x+ || y- -> y- || x+} EX True")
        formulaEquals(EX(True, (xp implies yd) equal (yd implies xp)), "{x+ -> y- <-> y- -> x+} EX True")
    }

    @Test
    fun expressionOperatorPriority() {
        //We don't care about priority of * vs. / and + vs. -
        val three = !3.0
        val zero = !0.0
        formulaEquals(((three times three) plus three eq zero), "3 * 3 + 3 == 0")
        formulaEquals(((three times three) minus three eq zero), "3 * 3 - 3 == 0")
        formulaEquals(((three div three) plus three eq zero), "3 / 3 + 3 == 0")
        formulaEquals(((three div three) minus three eq zero), "3 / 3 - 3 == 0")
    }

    @Test
    fun ambiguousFormulas() {
        val r = """
            k = True && False
            l = {k}EX True
            m = k || False
        """.toHUCTLp()
        assertEquals(3, r.size)
        assertEquals(True and False, r["k"])
        assertEquals(EX(True, DirFormula.True and DirFormula.False), r["l"])
        assertEquals((True and False) or False, r["m"])
    }

    @Test
    fun formulaCastError() {
        //flow to formula
        assertFailsWith<IllegalStateException> {
            """
                k = x+ || y-
                l = EX k
            """.toHUCTLp()
        }
        //formula to flow
        assertFailsWith<IllegalStateException> {
            """
                k = EX True
                l = {k}EF False
            """.toHUCTLp()
        }
        //expression to flow formula
        assertFailsWith<IllegalStateException> {
            """
                k = 1 + 2
                l = {k} EX False
            """.toHUCTLp()
        }
        //expression to formula
        assertFailsWith<IllegalStateException> {
            """
                k = a + b
                l = EX k
            """.toHUCTLp()
        }
        //formula to expression
        assertFailsWith<IllegalStateException> {
            """
                k = EX True
                a = k + 2
            """.toHUCTLp()
        }
    }

}