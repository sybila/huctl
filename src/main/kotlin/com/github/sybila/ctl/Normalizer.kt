package com.github.sybila.ctl

import com.github.sybila.ctl.Formula.Unary.*
import com.github.sybila.ctl.Formula.Binary.*

val unaryEqualities = mapOf<Class<out Formula.Unary<*>>, (Formula) -> Formula>(
        AX::class.java to { i -> not(EX(not(i))) },     // AX p = ! EX ! p
        EF::class.java to { i -> tt() EU i },           // EF p = true EU p
        AF::class.java to { i -> tt() AU i },           // AF p = true AU p
        EG::class.java to { i -> not(tt() AU not(i)) }, // EG p = ! (true AU ! p)
        AG::class.java to { i -> not(tt() EU not(i)) }  // AG p = ! (true EU ! p)
)

val binaryEqualities = mapOf<Class<out Formula.Binary<*>>, (Formula, Formula) -> Formula>(
        Implies::class.java to { l, r -> not(l) or r }, // a => b = !a || b
        Equal::class.java to { l, r ->                  // a <=> b = (a && b) || (!a && !b)
            (l and r) or (not(l) and not(r))
        }
)


/*
 * Normalize the formula using specified normal form.
 * Normal form is basically a mapping from unsupported operators to transformations.
 *
 * Note that you shouldn't transform one unsupported operator to other unsupported operator,
 * because the tree is transformed only once! (no fix-point is computed)
 */
fun Formula.normalize(
        unaryMap: Map<Class<out Formula.Unary<*>>, (Formula) -> Formula> = unaryEqualities,
        binaryMap: Map<Class<out Formula.Binary<*>>, (Formula, Formula) -> Formula> = binaryEqualities
) : Formula {
    return this.fold<Formula>({ this }, { i ->
        unaryMap[this.javaClass]?.invoke(i) ?: this.copy(i)
    }, { l, r ->
        binaryMap[this.javaClass]?.invoke(l, r) ?: this.copy(l, r)
    })
}
