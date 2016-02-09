package cz.muni.fi.ctl

val untilNormalForm: Map<Op, (Formula, (Formula) -> Formula) -> Formula> = mapOf(
        Op.ALL_NEXT to {
            // AX p = !EX !p
            f, x -> not( EX( not( x(f[0]) ) ) )
        },
        Op.EXISTS_FUTURE to {
            // EF p = E [ true U p ]
            f, x -> True EU x(f[0])
        },
        Op.ALL_FUTURE to {
            // AF p = A [ true U p ]
            f, x -> True AU x(f[0])
        },
        Op.EXISTS_GLOBAL to {
            // EG p = ! A [ true U ! p ]
            f, x -> not( True AU not( x(f[0]) ) )
        },
        Op.ALL_GLOBAL to {
            // AG p = ! E [ true U ! p ]
            f, x -> not( True EU not( x(f[0]) ) )
        },
        Op.IMPLICATION to {
            // a => b = !a || b
            f, x -> not(x(f[0])) or x(f[1])
        },
        Op.EQUIVALENCE to {
            // a <=> b = (a && b) || (!a && !b)
            f, x -> (x(f[0]) and x(f[1])) or (not(x(f[0])) and not(x(f[1])))
        }
)

class Normalizer(
        normalForm: Map<Op, (Formula, (Formula) -> Formula) -> Formula> = untilNormalForm
) {

    private val normalForm = normalForm

    fun normalize(f: Formula) : Formula {

        val normalize = { f: Formula -> normalize(f) }

        return normalForm[f.operator]?.invoke(f, normalize) ?: f.treeMap(normalize)
    }

}