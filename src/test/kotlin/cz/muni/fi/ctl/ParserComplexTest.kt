package cz.muni.fi.ctl

import org.junit.Test
import java.io.File
import kotlin.test.assertEquals

class Complex {

    val parser = Parser()


    val p1 = FloatProposition("p1", FloatOp.EQ, 4.0)
    val p2 = DirectionProposition("p2", Direction.IN, Facet.NEGATIVE)
    val p3 = FloatProposition("p3", FloatOp.LT, -3.14)

    Test fun complexFiles() {

        val f1 = File.createTempFile("file", ".ctl")
        val f2 = File.createTempFile("file2", ".ctl")
        val f3 = File.createTempFile("file3", ".ctl")

        f1.bufferedWriter().use {
            it.write("c = p2:in- || EX p3 < -3.14 EU a")
        }

        f2.bufferedWriter().use {
            it.write("#include \"${ f1.getAbsolutePath() }\" \n")
            it.write("b = EX c <=> True \n")
            it.write("d = e => e")
        }

        f3.bufferedWriter().use {
            it.write("a = ! p1 == 4 \n")
            it.write("#include \"${ f2.getAbsolutePath() }\" \n")
            it.write("e = True && c || c && False")
        }

        val result = parser.parse(f3)

        val a = not(p1)
        val c = (p2 or EX(p3)) EU a
        val b = EX(c) equal True
        val e = (True and c) or (c and False)
        val d = e implies e

        assertEquals(5, result.size())
        assertEquals(a, result.get("a"))
        assertEquals(b, result.get("b"))
        assertEquals(c, result.get("c"))
        assertEquals(d, result.get("d"))
        assertEquals(e, result.get("e"))

        f1.delete()
        f2.delete()
        f3.delete()

    }

    Test fun complexString() {

        val result = parser.parse("""
            b = EX p3 < -3.14 EU a
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

        assertEquals(5, result.size())
        assertEquals(a, result.get("a"))
        assertEquals(b, result.get("b"))
        assertEquals(c, result.get("c"))
        assertEquals(d, result.get("d"))
        assertEquals(e, result.get("e"))

    }

    Test fun operatorAssociativity() {
        //These binary operators are by convention right-associative
        //Other binary operators, such as &&, ||, <=> are associative,
        //so it doesn't matter if we resolve them left to right or right to left
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

    Test fun operatorPriority() {
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

}

