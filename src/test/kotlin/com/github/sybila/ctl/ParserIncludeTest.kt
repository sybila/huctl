package com.github.sybila.ctl

import org.junit.Test
import java.io.File
import java.io.FileNotFoundException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class Includes {

    val parser = CTLParser()

    @Test fun invalidInclude() {
        assertFailsWith(FileNotFoundException::class) {
            parser.parse(":include \"bogus.foo\" ")
        }
    }

    @Test fun duplicateInclude() {

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

        val result = parser.parse(
                ":include \"${ i2.absolutePath }\" \n" +
                ":include \"${ i1.absolutePath }\" "
        )

        assertEquals(2, result.size)
        assertEquals(True, result["val"])
        assertEquals(False, result["val2"])

        i1.delete()
        i2.delete()

    }

    @Test fun transitiveInclude() {

        val i1 = File.createTempFile("include1", ".ctl")
        val i2 = File.createTempFile("include2", ".ctl")

        i1.bufferedWriter().use {
            it.write("val = True")
        }
        i2.bufferedWriter().use {
            it.write(":include \"${ i1.absolutePath }\"")
        }

        val result = parser.parse(":include \"${ i2.absolutePath }\"")

        assertEquals(1, result.size)
        assertEquals(True, result["val"])

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

        val result = parser.parse(file)

        assertEquals(1, result.size)
        assertEquals(True, result["val"])

        file.delete()
        include.delete()
    }

    @Test fun simpleIncludeFromString() {

        val file = File.createTempFile("simpleInclude", ".ctl")

        file.bufferedWriter().use {
            it.write("val = True")
        }

        val result = parser.parse(
                ":include \"${ file.absolutePath }\""
        )

        assertEquals(1, result.size)
        assertEquals(True, result["val"])

        file.delete()
    }

}
