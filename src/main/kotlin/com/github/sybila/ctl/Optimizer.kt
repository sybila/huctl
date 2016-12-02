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
            is Formula.Atom -> when (i.proposition) {
                is True -> ff().asAtom()                                            // !True = False
                is False -> tt().asAtom()                                           // !False = True
                is Comparison<*> -> i.proposition.negate().asAtom()                 // !a > 5 = a <= 5
                else -> this.copy(i)
            }
            is Not -> i.inner                                                       // !!a = a
            else -> this.copy(i)
        }
        is EX -> when (i) {
            is Formula.Atom -> when (i.proposition) {
                is True -> tt().asAtom()                                            // EX True = True
                is False -> ff().asAtom()                                           // EX False = False
                else -> i
            }
            else -> EX(i)
        }
        else -> this.copy(i)
    }
}, { l, r ->
    when (this) {
        is And -> when {
            l is Formula.Atom && l.proposition is False ||
            r is Formula.Atom && r.proposition is False -> False                    // false && p = false
            l is Formula.Atom && l.proposition is True -> r                         // true && p = p
            r is Formula.Atom && r.proposition is True -> l                         // p && true = p
            l is Not && r is Not -> Not(l.inner or r.inner)                         // !a && !b = !(a || b)
            else -> l and r
        }
        is Or -> when {
            l is Formula.Atom && l.proposition is True ||
            r is Formula.Atom && r.proposition is True -> True                      // true || p = true
            l is Formula.Atom && l.proposition is False -> r                        // true || p = p
            r is Formula.Atom && r.proposition is False -> l                        // p || true = p
            l is Not && r is Not -> Not(l.inner and r.inner)                        // !a || !b = !(a && b)
            else -> l or r
        }
        is EU, is AU -> when {
            r is Formula.Atom && r.proposition is True -> True                      // a (E/A)U True = True
            r is Formula.Atom && r.proposition is False -> False                    // a (E/A)U False = False
            else -> this.copy(l, r)
        }
        else -> this.copy(l, r)
    }
})