package com.github.sybila.ctl

import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class Complex {

    val parser = CTLParser()


    val p1 = FloatProposition("p1", CompareOp.EQ, 4.0)
    val p2 = DirectionProposition("p2", Direction.IN, Facet.NEGATIVE)
    val p3 = FloatProposition(
            ("p3".toVariable() over 1.3.toConstant()) plus 2.0.toConstant(),
            CompareOp.LT,
            ((-3.14).toConstant() plus (12.0.toConstant() minus "f".toVariable())) times 2.0.toConstant()
    )

    @Test fun complexFiles() {

        val f1 = File.createTempFile("file", ".ctl")
        val f2 = File.createTempFile("file2", ".ctl")
        val f3 = File.createTempFile("file3", ".ctl")

        f1.bufferedWriter().use {
            it.write("c = p2:in- || EX z + 2 < x*2 EU a")
        }

        f2.bufferedWriter().use {
            it.write("#include \"${ f1.absolutePath }\" \n")
            it.write("b = EX c <=> True \n")
            it.write("d = e => e \n")
            it.write("q = 12 - f \n")
            it.write("x = -3.14 + q \n")
        }

        f3.bufferedWriter().use {
            it.write("a = ! p1 == 4 \n")
            it.write("z = p3 / 1.3 \n")
            it.write("#include \"${ f2.absolutePath }\" \n")
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

    @Test fun complexString() {

        val result = parser.parse("""
            z = p3
            x = 12 - f
            b = EX z/1.3 + 2 < (-3.14 + x) * 2 EU a
            a = (p1 == 4) && p2:in-
            d = AG AX c
            c = b <=> b
            e = c && True || False && b
        """)

        val a = p1 and p2
        val b = EX(p3) EU a
        val c = b equal b
        val d = AG(AX(c))
        val e = (c and True) or (False and b)

        assertEquals(5, result.size)
        assertEquals(a, result["a"])
        assertEquals(b, result["b"])
        assertEquals(c, result["c"])
        assertEquals(d, result["d"])
        assertEquals(e, result["e"])

    }

    @Test fun operatorAssociativity() {
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
                parser.formula("True => False => True")
        )
    }

    @Test fun operatorPriority() {
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
                parser.formula("True || False => False || True")
        )
        assertEquals(
                (True implies False) equal (False implies True),
                parser.formula("True => False <=> False => True")
        )
        assertEquals(
                (True equal False) EU (False equal True),
                parser.formula("True <=> False EU False <=> True")
        )
        assertEquals(
                (True EU False) AU (False EU True),
                parser.formula("True EU False AU False EU True")
        )
    }

    @Test fun expressionOperatorPriority() {
        //We don't care about priority of * vs. / and + vs. -
        val three = 3.0.toConstant()
        assertEquals(FloatProposition(
                (three times three) plus three, CompareOp.EQ, 0.0.toConstant()
        ), parser.formula("3 * 3 + 3 == 0"))
        assertEquals(FloatProposition(
                (three times three) minus three, CompareOp.EQ, 0.0.toConstant()
        ), parser.formula("3 * 3 - 3 == 0"))
        assertEquals(FloatProposition(
                (three over three) plus three, CompareOp.EQ, 0.0.toConstant()
        ), parser.formula("3 / 3 + 3 == 0"))
        assertEquals(FloatProposition(
                (three over three) minus three, CompareOp.EQ, 0.0.toConstant()
        ), parser.formula("3 / 3 - 3 == 0"))
    }

}

