package cz.muni.fi.ctl

interface NormalForm {
    val transformations: Map<Op, (Formula, (Formula) -> Formula) -> Formula>
}


val untilNormalForm = object : NormalForm {
    override val transformations: Map<Op, (Formula, (Formula) -> Formula) -> Formula> = mapOf(
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
}

/**
 * Normalize the formula using specified normal form.
 * Normal form is basically a mapping from unsupported operators to transformations.
 * Each transformation takes a formula and a function. It should update given formula so that
 * the dangerous operator is removed and then recursively call given function on
 * children of the formula (and include those as children instead of original children).
 *
 * See untilNormalForm for more details.
 *
 * Note that you shouldn't transform one unsupported operator to other unsupported operator,
 * because the tree is transformed only once!
 */
fun Formula.normalize(normalForm: NormalForm = untilNormalForm) : Formula {

    val normalize = { f: Formula -> this.normalize(normalForm) }

    return normalForm.transformations[this.operator]?.invoke(this, normalize) ?: this.treeMap(normalize)
}
