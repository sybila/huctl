package com.github.sybila.huctl

import com.github.sybila.huctl.dsl.*
import com.github.sybila.huctl.parser.toHUCTLp
import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class References {

    @Test
    fun cyclicReferenceThroughFiles() {
        val file = File.createTempFile("file", ".ctx")

        file.bufferedWriter().use {
            it.write("k = m")
        }

        assertFailsWith(IllegalStateException::class) {
            """
                m = !k
            """.toHUCTLp()
        }
        file.delete()
    }

    @Test
    fun transitiveCyclicReference() {
        assertFailsWith(IllegalStateException::class) {
            """
                k = EX l
                l = AX m
                m = ! k
            """.toHUCTLp()
        }
        assertFailsWith(IllegalStateException::class) {
            """
                e = a + 2
                a = b - 2
                b = 2 * e
            """.toHUCTLp()
        }
    }

    @Test
    fun simpleCyclicReference() {
        //formula
        assertFailsWith<IllegalStateException> {
            "k = !k".toHUCTLp()
        }
        //expression
        assertFailsWith<IllegalStateException> {
            "a = a + a".toHUCTLp()
        }
        //flow formula
        assertFailsWith<IllegalStateException> {
            "a = b+ || a".toHUCTLp()
        }
        //alias
        assertFailsWith<IllegalStateException> {
            "a = a".toHUCTLp()
        }
    }

    @Test
    fun undefinedReference() {
        assertFailsWith(IllegalStateException::class) {
            "k = EF m".toHUCTLp()
        }
    }

    @Test
    fun declarationOrderIndependence() {

        val result = """
            k = ! m
            m = True
        """.toHUCTLp()

        assertEquals(2, result.size)
        assertEquals(Not(Formula.True), result["k"])
        assertEquals(Formula.True, result["m"])

    }

    @Test
    fun duplicateDeclarationInFiles() {

        val i1 = File.createTempFile("include1", ".ctl")

        i1.bufferedWriter().use {
            it.write("k = True")
        }

        assertFailsWith(IllegalArgumentException::class) {
            (
                    ":include \"${ i1.absolutePath }\" \n" +
                    "k = False"
            ).toHUCTLp()
        }

        i1.delete()
    }

    @Test
    fun duplicateDeclarationInString() {
        assertFailsWith(IllegalArgumentException::class) {
            """
                k = True
                l = False
                k = False
            """.toHUCTLp()
        }
    }

    @Test
    fun duplicateDeclarationExpression() {
        assertFailsWith(IllegalArgumentException::class) {
            """
                k = 1
                l = 2
                k = 1.5
            """.toHUCTLp()
        }
    }

    @Test
    fun transitiveResolveInFiles() {

        val i1 = File.createTempFile("include1", ".ctl")
        val i2 = File.createTempFile("include2", ".ctl")

        i1.bufferedWriter().use {
            it.write("k = True")
        }
        i2.bufferedWriter().use {
            it.write("l = EF k")
        }

        val result = (
                "m = !l \n" +
                ":include \"${ i1.absolutePath }\" \n" +
                ":include \"${ i2.absolutePath }\" \n"
        ).toHUCTLp()

        assertEquals(3, result.size)
        assertEquals(Formula.True, result["k"])
        assertEquals(EF(Formula.True), result["l"])
        assertEquals(Not(EF(Formula.True)), result["m"])

    }

    @Test
    fun transitiveResolveInString() {

        val result = """
                j = True
                k = j
                l = EF k
                m = !l
        """.toHUCTLp()

        assertEquals(4, result.size)
        assertEquals(Formula.True, result["j"])
        assertEquals(Formula.True, result["k"])
        assertEquals(EF(Formula.True), result["l"])
        assertEquals(Not(EF(Formula.True)), result["m"])

    }

    @Test
    fun simpleResolveInInclude() {

        val i = File.createTempFile("include", ".ctl")

        i.bufferedWriter().use {
            it.write("val = False")
        }

        val result = (
                "k = !val \n " +
                ":include \"${ i.absolutePath }\" \n"
        ).toHUCTLp()

        assertEquals(2, result.size)
        assertEquals(Formula.False, result["val"])
        assertEquals(Not(Formula.False), result["k"])

        i.delete()

    }

    @Test
    fun simpleResolveInString() {
        val result = """
            k = True
            l = !k
        """.toHUCTLp()
        assertEquals(2, result.size)
        assertEquals(Formula.True, result["k"])
        assertEquals(Not(Formula.True), result["l"])
    }

    @Test
    fun simpleResolveExpression() {
        val result = """
            k = a + b
            l = k / 2 == 0
        """.toHUCTLp()
        assertEquals(1, result.size)
        assertEquals((("a".toVar() plus "b".toVar()) div !2.0 eq !0.0), result["l"])
    }

    @Test
    fun aliasInString() {
        try {
            val result = """
            k = True
            l = k
            m = l
            n = m
        """.toHUCTLp()
            val True = Formula.True
            assertEquals(4, result.size)
            assertEquals(True, result["k"])
            assertEquals(True, result["l"])
            assertEquals(True, result["m"])
            assertEquals(True, result["n"])
        } catch (e: IllegalStateException) { e.printStackTrace() }
    }

    @Test
    fun expressionAlias() {
        val result = """
            k = name
            l = k
            m = l
            n = m > 0
        """.toHUCTLp()
        assertEquals(1, result.size)
        assertEquals(("name".toVar() gt !0.0), result["n"])
    }

    @Test
    fun unboundReference() {
        assertFailsWith<IllegalStateException> {
            """
                f = EX k
            """.toHUCTLp()
        }
        assertFailsWith<IllegalStateException> {
            """
                f = exists x in (x && True): False
            """.toHUCTLp()
        }
    }

    @Test
    fun firstOrderReferences() {
        formulaEquals(
                ForAll("x", Formula.True, "x".toReference() and "x".toPositiveIn()),
                "forall x: x && x:in+"
        )
    }

}