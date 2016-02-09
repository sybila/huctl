package cz.muni.fi.ctl

//Formulas
interface Formula {
    val operator: Op
    val subFormulas: List<Formula>

    operator fun get(i: Int): Formula = subFormulas[i]
}

data class FormulaImpl (
        override val operator: Op = Op.ATOM,
        override val subFormulas: List<Formula> = listOf()
) : Formula {

    init {
        if (subFormulas.size != operator.cardinality) {
            throw IllegalArgumentException(
                    "Operator cardinality(${operator.cardinality}) and " +
                    "number of sub formulas(${subFormulas.size}) do not match")
        }
    }

    constructor(operator: Op, vararg formulas: Formula) : this(operator, formulas.toList())

    override fun toString(): String = when (operator.cardinality) {
        1 -> "$operator ${subFormulas[0]}"
        2 -> "(${subFormulas[0]} $operator ${subFormulas[1]})"
        else -> "$operator($subFormulas)"   //this never happens
    }

}

interface Atom : Formula

//Boolean Atoms
val True: Atom = object : Atom {
    final override val operator = Op.ATOM
    final override val subFormulas = listOf<Formula>()
    override fun toString():String = "True"
}

val False: Atom = object : Atom {
    final override val operator = Op.ATOM
    final override val subFormulas = listOf<Formula>()
    override fun toString():String = "False"
}

//Float atoms
data class FloatProposition (
        val left: Expression,
        val compareOp: CompareOp,
        val right: Expression
) : Atom {
    constructor(left: String, operator: CompareOp, right: Double) : this(left.toVariable(), operator, right.toConstant())
    final override val operator = Op.ATOM
    final override val subFormulas = listOf<Formula>()
    override fun toString(): String = "$left $compareOp $right"
}

//Direction atoms
data class DirectionProposition (
        val variable: String,
        val direction: Direction,
        val facet: Facet
) : Atom {
    final override val operator = Op.ATOM
    final override val subFormulas = listOf<Formula>()
    override fun toString(): String = "$variable:$direction$facet"
}

//Float expressions
interface Expression;

data class Variable(
        val name: String
) : Expression {
    override fun toString(): String = name
}

data class Constant(
        val value: Double
) : Expression {
    override fun toString(): String = value.toString()
}

data class ExpressionImpl(
        val left: Expression,
        val operator: FloatOp,
        val right: Expression
) : Expression {
    override fun toString() = "($left $operator $right)"
}

//Simplified builders
fun not(f: Formula): Formula = FormulaImpl(Op.NEGATION, f)
fun EX(f: Formula): Formula = FormulaImpl(Op.EXISTS_NEXT, f)
fun AX(f: Formula): Formula = FormulaImpl(Op.ALL_NEXT, f)
fun EF(f: Formula): Formula = FormulaImpl(Op.EXISTS_FUTURE, f)
fun AF(f: Formula): Formula = FormulaImpl(Op.ALL_FUTURE, f)
fun EG(f: Formula): Formula = FormulaImpl(Op.EXISTS_GLOBAL, f)
fun AG(f: Formula): Formula = FormulaImpl(Op.ALL_GLOBAL, f)
infix fun Formula.or(f2: Formula): Formula = FormulaImpl(Op.OR, this, f2)
infix fun Formula.and(f2: Formula): Formula = FormulaImpl(Op.AND, this, f2)
infix fun Formula.implies(f2: Formula): Formula = FormulaImpl(Op.IMPLICATION, this, f2)
infix fun Formula.equal(f2: Formula): Formula = FormulaImpl(Op.EQUIVALENCE, this, f2)
infix fun Formula.EU(f2: Formula): Formula = FormulaImpl(Op.EXISTS_UNTIL, this, f2)
infix fun Formula.AU(f2: Formula): Formula = FormulaImpl(Op.ALL_UNTIL, this, f2)

fun Double.toConstant(): Expression = Constant(this)
fun String.toVariable(): Variable = Variable(this)
infix fun Expression.plus(other: Expression): Expression = ExpressionImpl(this, FloatOp.ADD, other)
infix fun Expression.minus(other: Expression): Expression = ExpressionImpl(this, FloatOp.SUBTRACT, other)
infix fun Expression.times(other: Expression): Expression = ExpressionImpl(this, FloatOp.MULTIPLY, other)
infix fun Expression.over(other: Expression): Expression = ExpressionImpl(this, FloatOp.DIVIDE, other)

//this is not typical map semantics -> don't make it public
fun Formula.treeMap(x: (Formula) -> Formula) =
        if (this.operator.cardinality == 0) {
            this
        } else {
            FormulaImpl(this.operator, this.subFormulas.map(x))
        }

fun Expression.treeMap(x: (Expression) -> Expression): Expression =
        if (this is ExpressionImpl) {
            ExpressionImpl(
                    x(left),
                    this.operator,
                    x(right))
        } else this