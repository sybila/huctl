package cz.muni.fi.ctl

val untilNormalForm: Map<Operator, (Formula, (Formula) -> Formula) -> Formula> = mapOf(
        Operator.ALL_NEXT to {
            // AX p = !EX !p
            f, x -> not( EX( not( x(f[0]) ) ) )
        },
        Operator.EXISTS_FUTURE to {
            // EF p = E [ true U p ]
            f, x -> True EU x(f[0])
        },
        Operator.ALL_FUTURE to {
            // AF p = A [ true U p ]
            f, x -> True AU x(f[0])
        },
        Operator.EXISTS_GLOBAL to {
            // EG p = ! A [ true U ! p ]
            f, x -> not( True AU not( x(f[0]) ) )
        },
        Operator.ALL_GLOBAL to {
            // AG p = ! E [ true U ! p ]
            f, x -> not( True EU not( x(f[0]) ) )
        },
        Operator.IMPLICATION to {
            // a => b = !a || b
            f, x -> not(x(f[0])) or x(f[1])
        },
        Operator.EQUIVALENCE to {
            // a <=> b = (a && b) || (!a && !b)
            f, x -> (x(f[0]) and x(f[1])) or (not(x(f[0])) and not(x(f[1])))
        }
)

public class Normalizer(
        normalForm: Map<Operator, (Formula, (Formula) -> Formula) -> Formula> = untilNormalForm
) {

    private val normalForm = normalForm

    public fun normalize(f: Formula) : Formula {
        if (f.operator.cardinality == 0) return f   //stop on propositions - we don't know how to handle them here

        val normalize = { f: Formula -> normalize(f) }
        val identity = { f: Formula, x: (Formula) -> Formula
            -> FormulaImpl(f.operator, f.subFormulas.map(x)) }

        return normalForm[f.operator]?.invoke(f, normalize) ?: identity(f, normalize)
    }

}