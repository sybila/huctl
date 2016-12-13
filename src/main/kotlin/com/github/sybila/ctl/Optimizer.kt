package com.github.sybila.ctl

fun Formula.optimize(): Formula {
    //currently we have a single pass optimizer.
    //In the future, we would like to add more complex, fix-point based optimizations.
    return this.optimizeOnce()
}

private fun Formula.optimizeOnce(): Formula = this.fold<Formula>({ this }, { i ->
    when (this) {
        /*is Not -> when (i) {
            True -> False                                                           // !True = False
            False -> True                                                           // !False = True
            is Formula.Atom -> when (i.proposition) {
                is Comparison<*> -> i.proposition.negate().asAtom()                 // !a > 5 = a <= 5
                else -> this.copy(i)
            }
            is Not -> i.inner                                                       // !!a = a
            else -> this.copy(i)
        }
        is EX -> when (i) {
            True -> True                                                            // EX True = True
            False -> False                                                          // EX False = False
            else -> EX(i)
        }*/
        else -> this.copy(i)
    }
}, { l, r ->
    when (this) {
        /*is And -> when {
            l == False || r == False -> False                                       // false && p = false
            l == True -> r                                                          // true && p = p
            r == True -> l                                                          // p && true = p
            l is Not && r is Not -> Not(l.inner or r.inner)                         // !a && !b = !(a || b)
            else -> l and r
        }
        is Or -> when {
            l == True || r == True -> True                                          // true || p = true
            l == False -> r                                                         // true || p = p
            r == False -> l                                                         // p || true = p
            l is Not && r is Not -> Not(l.inner and r.inner)                        // !a || !b = !(a && b)
            else -> l or r
        }
        is EU, is AU -> when (r) {
            True -> True                                                            // a (E/A)U True = True
            False -> False                                                          // a (E/A)U False = False
            else -> this.copy(l, r)
        }*/
        else -> this.copy(l, r)
    }
})