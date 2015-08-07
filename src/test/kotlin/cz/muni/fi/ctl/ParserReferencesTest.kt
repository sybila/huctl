package cz.muni.fi.ctl

import org.junit.Test
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.failsWith

class References {

    val parser = Parser()

    Test fun cyclicReferenceThroughFiles() {
        val file = File.createTempFile("file", ".ctx")

        file.bufferedWriter().use {
            it.write("k = m")
        }

        failsWith(javaClass<IllegalStateException>()) {
            parser.parse("""
                m = !k
            """)
        }
        file.delete()
    }

    Test fun transitiveCyclicReference() {
        failsWith(javaClass<IllegalStateException>()) {
            parser.parse("""
                k = EX l
                l = AX m
                m = ! k
            """)
        }
    }

    Test fun simpleCyclicReference() {
        failsWith(javaClass<IllegalStateException>()) {
            parser.parse("k = !k")
        }
    }

    Test fun undefinedReference() {
        failsWith(javaClass<IllegalStateException>()) {
            parser.parse("k = EF m")
        }
    }

    Test fun declarationOrderIndependence() {

        val result = parser.parse("""
            k = ! m
            m = True
        """)

        assertEquals(2, result.size())
        assertEquals(not(True), result.get("k"))
        assertEquals(True, result.get("m"))

    }

    Test fun duplicateDeclarationInFiles() {

        val i1 = File.createTempFile("include1", ".ctl")

        i1.bufferedWriter().use {
            it.write("k = True")
        }

        failsWith(javaClass<IllegalStateException>()) {
            parser.parse(
                    "#include \"${ i1.getAbsolutePath() }\" \n" +
                            "k = False"
            )
        }

        i1.delete()
    }

    Test fun duplicateDeclarationInString() {
        failsWith(javaClass<IllegalStateException>()) {
            parser.parse("""
                k = True
                l = False
                k = False
            """)
        }
    }

    Test fun transitiveResolveInFiles() {

        val i1 = File.createTempFile("include1", ".ctl")
        val i2 = File.createTempFile("include2", ".ctl")

        i1.bufferedWriter().use {
            it.write("k = True")
        }
        i2.bufferedWriter().use {
            it.write("l = EF k")
        }

        val result = parser.parse(
                "m = !l \n" +
                        "#include \"${ i1.getAbsolutePath() }\" \n" +
                        "#include \"${ i2.getAbsolutePath() }\" \n"
        )

        assertEquals(3, result.size())
        assertEquals(True, result.get("k"))
        assertEquals(EF(True), result.get("l"))
        assertEquals(not(EF(True)), result.get("m"))

    }

    Test fun transitiveResolveInString() {

        val result = parser.parse("""
                k = True
                l = EF k
                m = !l
        """)

        assertEquals(3, result.size())
        assertEquals(True, result.get("k"))
        assertEquals(EF(True), result.get("l"))
        assertEquals(not(EF(True)), result.get("m"))

    }

    Test fun simpleResolveInInclude() {

        val i = File.createTempFile("include", ".ctl")

        i.bufferedWriter().use {
            it.write("val = False")
        }

        val result = parser.parse(
                "k = !val \n " +
                        "#include \"${ i.getAbsoluteFile() }\" \n"
        )

        assertEquals(2, result.size())
        assertEquals(False, result.get("val"))
        assertEquals(not(False), result.get("k"))

        i.delete()

    }

    Test fun simpleResolveInString() {
        val result = parser.parse("""
            k = True
            l = !k
        """)
        assertEquals(2, result.size())
        assertEquals(True, result.get("k"))
        assertEquals(not(True), result.get("l"))
    }

    Test fun aliasInString() {
        val result = parser.parse("""
            k = True
            l = k
        """)
        assertEquals(2, result.size())
        assertEquals(True, result.get("k"))
        assertEquals(True, result.get("l"))
    }

}