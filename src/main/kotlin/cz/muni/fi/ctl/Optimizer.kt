package cz.muni.fi.ctl


public class Optimizer {

    private val optimize = { f: Formula -> optimizeTree(f) }

    fun optimizeTree(f: Formula): Formula = when {
        f.operator.cardinality == 0 -> f
        f.operator == Op.NEGATION ->
            when {
                // !True = False
                f[0] == True -> False
                // !False = True
                f[0] == False -> True
                // !a > 5 = a <= 5
                f is FloatProposition -> f.copy(floatOp = f.floatOp.neg)
                // !!a = a
                f[0].operator == Op.NEGATION -> optimizeTree(f[0][0])
                else -> f.map(optimize)
            }
        f.operator == Op.AND ->
            when {
                // a && False = False
                False in f.subFormulas -> False
                // a && True = a
                f[0] == True -> optimizeTree(f[1])
                f[1] == True -> optimizeTree(f[0])
                //!a && !b = a || b
                f[0].operator == Op.NEGATION && f[1].operator == Op.NEGATION ->
                    optimizeTree(f[0][0]) and optimizeTree(f[1][0])
                else -> f.map(optimize)
            }
        f.operator == Op.OR ->
            when {
                // a || True = True
                True in f.subFormulas -> True
                // a || False = a
                f[0] == False -> optimizeTree(f[1])
                f[1] == False -> optimizeTree(f[0])
                // !a || !b = a && b
                f[0].operator == Op.NEGATION && f[1].operator == Op.NEGATION ->
                    optimizeTree(f[0][0]) and optimizeTree(f[1][0])
                else -> f.map(optimize)
            }
        f.operator == Op.EXISTS_UNTIL ->
            when {
                // E a U True = True
                f[1] == True -> True
                // E a U False = False
                f[1] == False -> False
                else -> f.map(optimize)
            }
        f.operator == Op.ALL_UNTIL ->
            when {
                // A a U True = True
                f[1] == True -> True
                // A a U False = False
                f[1] == False -> False
                else -> f.map(optimize)
            }
        f.operator == Op.EXISTS_NEXT ->
            when {
                // EX True = True
                f[0] == True -> True
                // EX False = False
                f[0] == False -> False
                else -> f.map(optimize)
            }
        else ->
            f.map(optimize)
    }
}