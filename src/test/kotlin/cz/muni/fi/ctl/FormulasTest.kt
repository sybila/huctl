package cz.muni.fi.ctl

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MapTest {

    val formula = EX( True EU (
            FloatProposition("var", FloatOp.EQ, 13.3)
                    or
            DirectionProposition("val", Direction.IN, Facet.NEGATIVE)
        )
    )

    @Test fun treeMapId() {

        assertEquals(formula, formula.treeMap { it })

    }

    @Test fun treeMapPropositions() {

        formula.treeMap {
            if (it.operator.cardinality == 0) throw IllegalStateException("Executing tree map on a leaf")
            else it
        }
    }

    @Test fun treeMapComplex() {

        fun transform(f: Formula): Formula = when(f.operator) {
            Op.EXISTS_NEXT -> FormulaImpl(Op.ALL_NEXT, f.subFormulas.map(::transform))
            Op.EXISTS_UNTIL -> FormulaImpl(Op.ALL_UNTIL, f.subFormulas.map(::transform))
            Op.OR -> FormulaImpl(Op.AND, f.subFormulas.map(::transform))
            else -> f.treeMap(::transform)
        }

        assertEquals(
                AX( True AU (
                FloatProposition("var", FloatOp.EQ, 13.3)
                    and
                DirectionProposition("val", Direction.IN, Facet.NEGATIVE)
                )
        ), transform(formula))

    }

}

class Misc {

    @Test fun booleanToString() {
        assertEquals("True", True.toString())
        assertEquals("False", False.toString())
    }

    @Test fun formulaToString() {
        assertEquals("(True && EX (False EU True))", (True and EX(False EU True)).toString())
    }

    @Test fun floatToString() {
        assertEquals("prop > 5.3", FloatProposition("prop", FloatOp.GT, 5.3).toString())
    }

    @Test fun directionToString() {
        assertEquals("prop:in+", DirectionProposition("prop", Direction.IN, Facet.POSITIVE).toString())
    }

    @Test fun emptyFormula() {
        assertEquals("null([])",FormulaImpl().toString())
    }

    @Test fun notEnoughFormulas() {
        assertFailsWith(IllegalArgumentException::class) {
            FormulaImpl(Op.ALL_UNTIL, True)
        }
    }

    @Test fun tooManyFormulas() {
        assertFailsWith(IllegalArgumentException::class) {
            FormulaImpl(Op.ALL_UNTIL, True, False, Atom())
        }
    }

    @Test fun get() {
        val float = FloatProposition("val", FloatOp.GT, 34.12)
        assertEquals("val", float.variable)
        assertEquals(FloatOp.GT, float.floatOp)
        assertEquals(34.12, float.value)
        val dir = DirectionProposition("var", Direction.IN, Facet.NEGATIVE)
        assertEquals("var", dir.variable)
        assertEquals(Direction.IN, dir.direction)
        assertEquals(Facet.NEGATIVE, dir.facet)
    }

}