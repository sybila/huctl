package com.github.sybila.huctl

import com.github.sybila.huctl.parser.toHUCTLp
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class Includes {

    @Test
    fun invalidInclude() {
        assertFailsWith(FileNotFoundException::class) {
            ":include \"bogus.foo\" ".toHUCTLp()
        }
    }

    @Test
    fun duplicateInclude() {

        val i1 = File.createTempFile("include1", ".ctl")
        val i2 = File.createTempFile("include2", ".ctl")

        i1.bufferedWriter().use {
            it.write("val = True \n")
            it.write(":include \"${ i1.absolutePath }\"")
        }
        i2.bufferedWriter().use {
            it.write(":include \"${ i1.absolutePath }\" \n")
            it.write(":include \"${ i2.absolutePath }\" \n")
            it.write("val2 = False")
        }

        val result = (
                ":include \"${ i2.absolutePath }\" \n" +
                ":include \"${ i1.absolutePath }\" "
        ).toHUCTLp()

        assertEquals(2, result.size)
        assertEquals(Formula.True, result["val"])
        assertEquals(Formula.False, result["val2"])

        i1.delete()
        i2.delete()

    }

    @Test
    fun transitiveInclude() {

        val i1 = File.createTempFile("include1", ".ctl")
        val i2 = File.createTempFile("include2", ".ctl")

        i1.bufferedWriter().use {
            it.write("val = True")
        }
        i2.bufferedWriter().use {
            it.write(":include \"${ i1.absolutePath }\"")
        }

        val result = ":include \"${ i2.absolutePath }\"".toHUCTLp()

        assertEquals(1, result.size)
        assertEquals(Formula.True, result["val"])

        i1.delete()
        i2.delete()

    }

    @Test fun simpleIncludeFromFile() {

        val include = File.createTempFile("simpleInclude", ".ctl")

        include.bufferedWriter().use {
            it.write("val = True")
        }

        val file = File.createTempFile("simpleFile", ".ctl")
        file.bufferedWriter().use {
            it.write(":include \"${ include.absolutePath }\"")
        }

        val result = file.toHUCTLp()

        assertEquals(1, result.size)
        assertEquals(Formula.True, result["val"])

        file.delete()
        include.delete()
    }

    @Test
    fun simpleIncludeFromString() {

        val file = File.createTempFile("simpleInclude", ".ctl")

        file.bufferedWriter().use {
            it.write("val = True")
        }

        val result = ":include \"${ file.absolutePath }\"".toHUCTLp()

        assertEquals(1, result.size)
        assertEquals(Formula.True, result["val"])

        file.delete()
    }

}
