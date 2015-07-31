package cz.muni.fi.ctl

import org.junit.Test
import kotlin.test.assertEquals

class MapTest {

    val formula = EX( True EU (
            FloatProposition("var", FloatOp.EQ, 13.3)
                    or
            DirectionProposition("val", DirectionProposition.Direction.IN, DirectionProposition.Facet.NEGATIVE)
        )
    )

    Test fun treeMapId() {

        assertEquals(formula, formula.treeMap { it })

    }

    Test fun treeMapPropositions() {

        formula.treeMap {
            if (it.operator.cardinality == 0) throw IllegalStateException("Executing tree map on a leaf")
            else it
        }
    }

    Test fun treeMapComplex() {

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
                DirectionProposition("val", DirectionProposition.Direction.IN, DirectionProposition.Facet.NEGATIVE)
                )
        ), transform(formula))

    }

}

class Misc {

    Test fun booleanToString() {
        assertEquals("True", True.toString())
        assertEquals("False", False.toString())
    }

    Test fun formulaToString() {
        assertEquals("True && EX(False EU True)", (True and EX(False EU True)).toString())
    }

    Test fun floatToString() {
        assertEquals("prop > 5.3", FloatProposition("prop", FloatOp.GT, 5.3).toString())
    }

    Test fun directionToString() {
        assertEquals("prop:in+", DirectionProposition("prop", DirectionProposition.Direction.IN, DirectionProposition.Facet.POSITIVE).toString())
    }

    Test fun emptyFormula() {
        assertEquals("null([])",FormulaImpl().toString())
    }

    Test(expected = IllegalArgumentException::class)
    fun notEnoughFormulas() {
        FormulaImpl(Op.ALL_UNTIL, True)
    }

    Test(expected = IllegalArgumentException::class)
    fun tooManyFormulas() {
        FormulaImpl(Op.ALL_UNTIL, True, False, Atom())
    }

}