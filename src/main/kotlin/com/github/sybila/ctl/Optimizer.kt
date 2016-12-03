package com.github.sybila.ctl

import com.github.sybila.ctl.Formula.Unary.*
import com.github.sybila.ctl.Formula.Binary.*
import com.github.sybila.ctl.Proposition.*

fun Formula.optimize(): Formula {
    //it's hard to optimize whole formula at once, so we just compute it as a fix point
    var one = this.optimizeOnce()
    var two = one.optimizeOnce()
    while(two != one) {
        one = two
        two = this.optimizeOnce()
    }
    return two
}

private fun Formula.optimizeOnce(): Formula = this.fold<Formula>({ this }, { i ->
    when (this) {
        is Not -> when (i) {
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
        }
        else -> this.copy(i)
    }
}, { l, r ->
    when (this) {
        is And -> when {
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
        is EU, is AU -> when {
        r == True -> True                                                           // a (E/A)U True = True
            r == False -> False                                                     // a (E/A)U False = False
            else -> this.copy(l, r)
        }
        else -> this.copy(l, r)
    }
})