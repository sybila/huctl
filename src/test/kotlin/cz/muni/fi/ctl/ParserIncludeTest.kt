package cz.muni.fi.ctl

import org.junit.Test
import java.io.File
import java.io.FileNotFoundException
import kotlin.test.assertEquals

class Includes {

    val parser = Parser()

    Test(expected = FileNotFoundException::class)
    fun invalidInclude() {
        parser.parse("#include \"bogus.foo\" ")
    }

    Test fun duplicateInclude() {

        val i1 = File.createTempFile("include1", ".ctl")
        val i2 = File.createTempFile("include2", ".ctl")

        i1.bufferedWriter().use {
            it.write("val = True \n")
            it.write("#include \"${ i1.getAbsolutePath() }\"")
        }
        i2.bufferedWriter().use {
            it.write("#include \"${ i1.getAbsolutePath() }\" \n")
            it.write("#include \"${ i2.getAbsolutePath() }\" \n")
            it.write("val2 = False")
        }

        val result = parser.parse(
                "#include \"${ i2.getAbsolutePath() }\" \n" +
                        "#include \"${ i1.getAbsolutePath() }\" "
        )

        assertEquals(2, result.size())
        assertEquals(True, result.get("val"))
        assertEquals(False, result.get("val2"))

        i1.delete()
        i2.delete()

    }

    Test fun transitiveInclude() {

        val i1 = File.createTempFile("include1", ".ctl")
        val i2 = File.createTempFile("include2", ".ctl")

        i1.bufferedWriter().use {
            it.write("val = True")
        }
        i2.bufferedWriter().use {
            it.write("#include \"${ i1.getAbsolutePath() }\"")
        }

        val result = parser.parse("#include \"${ i2.getAbsolutePath() }\"")

        assertEquals(1, result.size())
        assertEquals(True, result.get("val"))

        i1.delete()
        i2.delete()

    }

    Test fun simpleIncludeFromFile() {

        val include = File.createTempFile("simpleInclude", ".ctl")

        include.bufferedWriter().use {
            it.write("val = True")
        }

        val file = File.createTempFile("simpleFile", ".ctl")
        file.bufferedWriter().use {
            it.write("#include \"${include.getAbsolutePath()}\"")
        }

        val result = parser.parse(file)

        assertEquals(1, result.size())
        assertEquals(True, result.get("val"))

        file.delete()
        include.delete()
    }

    Test fun simpleIncludeFromString() {

        val file = File.createTempFile("simpleInclude", ".ctl")

        file.bufferedWriter().use {
            it.write("val = True")
        }

        val result = parser.parse(
                "#include \"${ file.getAbsolutePath() }\""
        )

        assertEquals(1, result.size())
        assertEquals(True, result.get("val"))

        file.delete()
    }

}
