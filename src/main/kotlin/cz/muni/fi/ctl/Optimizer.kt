package cz.muni.fi.ctl


class Optimizer {

    fun optimize(f: Formula): Formula {
        //it's hard to optimize while formula at once, so we just compute it as a fix point
        var one = optimizeTree(f)
        var two = optimizeTree(one)
        while(two != one) {
            one = two
            two = optimizeTree(two)
        }
        return two
    }

    private val optimize = { f: Formula -> optimizeTree(f) }

    private fun optimizeTree(f: Formula): Formula = when {
        f.operator.cardinality == 0 -> f
        f.operator == Op.NEGATION -> {
            val child = f[0]
            when {
                // !True = False
                child == True -> False
                // !False = True
                child == False -> True
                // !a > 5 = a <= 5
                child is FloatProposition -> child.copy(compareOp = child.compareOp.neg)
                // !!a = a
                child.operator == Op.NEGATION -> optimizeTree(child[0])
                else -> f.treeMap(optimize)
            }
        }
        f.operator == Op.AND ->
            when {
                // a && False = False
                False in f.subFormulas -> False
                // a && True = a
                f[0] == True -> optimizeTree(f[1])
                f[1] == True -> optimizeTree(f[0])
                //!a && !b = !(a || b)
                f[0].operator == Op.NEGATION && f[1].operator == Op.NEGATION ->
                    not(optimizeTree(f[0][0]) or optimizeTree(f[1][0]))
                else -> f.treeMap(optimize)
            }
        f.operator == Op.OR ->
            when {
                // a || True = True
                True in f.subFormulas -> True
                // a || False = a
                f[0] == False -> optimizeTree(f[1])
                f[1] == False -> optimizeTree(f[0])
                // !a || !b = !(a && b)
                f[0].operator == Op.NEGATION && f[1].operator == Op.NEGATION ->
                    not(optimizeTree(f[0][0]) and optimizeTree(f[1][0]))
                else -> f.treeMap(optimize)
            }
        f.operator == Op.EXISTS_UNTIL ->
            when {
                // E a U True = True
                f[1] == True -> True
                // E a U False = False
                f[1] == False -> False
                else -> f.treeMap(optimize)
            }
        f.operator == Op.ALL_UNTIL ->
            when {
                // A a U True = True
                f[1] == True -> True
                // A a U False = False
                f[1] == False -> False
                else -> f.treeMap(optimize)
            }
        f.operator == Op.EXISTS_NEXT ->
            when {
                // EX True = True
                f[0] == True -> True
                // EX False = False
                f[0] == False -> False
                else -> f.treeMap(optimize)
            }
        else ->
            f.treeMap(optimize)
    }
}