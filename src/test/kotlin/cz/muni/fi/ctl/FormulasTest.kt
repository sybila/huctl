package cz.muni.fi.ctl

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MapTest {

    val formula = EX( True EU (
            FloatProposition("var", CompareOp.EQ, 13.3)
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
                FloatProposition("var", CompareOp.EQ, 13.3)
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

    @Test fun variableToString() {
        assertEquals("test", "test".toVariable().toString())
    }

    @Test fun constantToString() {
        assertEquals("3.14", 3.14.toConstant().toString())
    }

    @Test fun expressionToString() {
        assertEquals("((a + 12.0) / ((3.0 * 4.0) - Var))",
                (
                        ("a".toVariable() plus 12.0.toConstant())
                                over
                        ((3.0.toConstant() times 4.0.toConstant()) minus "Var".toVariable())
                ).toString())
    }

    @Test fun formulaToString() {
        assertEquals("(True && EX (False EU True))", (True and EX(False EU True)).toString())
    }

    @Test fun floatPropositionToString() {
        assertEquals("prop > 5.3", FloatProposition("prop".toVariable(), CompareOp.GT, 5.3.toConstant()).toString())
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
            FormulaImpl(Op.ALL_UNTIL, True, False, Reference("nothing"))
        }
    }

    @Test fun get() {
        val float = FloatProposition("val", CompareOp.GT, 34.12)
        assert(float.left is Variable)
        assertEquals("val", (float.left as Variable).name)
        assertEquals(CompareOp.GT, float.compareOp)
        assert(float.right is Constant)
        assertEquals(34.12, (float.right as Constant).value)
        val dir = DirectionProposition("var", Direction.IN, Facet.NEGATIVE)
        assertEquals("var", dir.variable)
        assertEquals(Direction.IN, dir.direction)
        assertEquals(Facet.NEGATIVE, dir.facet)
    }

}