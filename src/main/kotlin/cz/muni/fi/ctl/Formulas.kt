package cz.muni.fi.ctl

//Formulas
public interface Formula {
    val operator: Op
    val subFormulas: List<Formula>

    operator public fun get(i: Int): Formula = subFormulas[i]
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

public interface Atom : Formula

//Boolean Atoms
public val True: Atom = object : Atom {
    final override val operator = Op.ATOM
    final override val subFormulas = listOf<Formula>()
    override fun toString():String = "True"
}

public val False: Atom = object : Atom {
    final override val operator = Op.ATOM
    final override val subFormulas = listOf<Formula>()
    override fun toString():String = "False"
}

//Float atoms
public data class FloatProposition (
        val left: Expression,
        val compareOp: CompareOp,
        val right: Expression
) : Atom {
    final override val operator = Op.ATOM
    final override val subFormulas = listOf<Formula>()
    override fun toString(): String = "$left $compareOp $right"
}

//Direction atoms
public data class DirectionProposition (
        val variable: String,
        val direction: Direction,
        val facet: Facet
) : Atom {
    final override val operator = Op.ATOM
    final override val subFormulas = listOf<Formula>()
    override fun toString(): String = "$variable:$direction$facet"
}

//Float expressions
public interface Expression;

public data class Variable(
        val name: String
) : Expression

public data class Constant(
        val value: Double
) : Expression

public data class ExpressionImpl(
        val left: Expression,
        val operator: FloatOp,
        val right: Expression
) : Expression

//Simplified builders
public fun not(f: Formula): Formula = FormulaImpl(Op.NEGATION, f)
public fun EX(f: Formula): Formula = FormulaImpl(Op.EXISTS_NEXT, f)
public fun AX(f: Formula): Formula = FormulaImpl(Op.ALL_NEXT, f)
public fun EF(f: Formula): Formula = FormulaImpl(Op.EXISTS_FUTURE, f)
public fun AF(f: Formula): Formula = FormulaImpl(Op.ALL_FUTURE, f)
public fun EG(f: Formula): Formula = FormulaImpl(Op.EXISTS_GLOBAL, f)
public fun AG(f: Formula): Formula = FormulaImpl(Op.ALL_GLOBAL, f)
public infix fun Formula.or(f2: Formula): Formula = FormulaImpl(Op.OR, this, f2)
public infix fun Formula.and(f2: Formula): Formula = FormulaImpl(Op.AND, this, f2)
public infix fun Formula.implies(f2: Formula): Formula = FormulaImpl(Op.IMPLICATION, this, f2)
public infix fun Formula.equal(f2: Formula): Formula = FormulaImpl(Op.EQUIVALENCE, this, f2)
public infix fun Formula.EU(f2: Formula): Formula = FormulaImpl(Op.EXISTS_UNTIL, this, f2)
public infix fun Formula.AU(f2: Formula): Formula = FormulaImpl(Op.ALL_UNTIL, this, f2)

public fun Double.toExpression(): Expression = Constant(this)
public fun String.toVariable(): Variable = Variable(this)
public infix fun Expression.plus(other: Expression): Expression = ExpressionImpl(this, FloatOp.ADD, other)
public infix fun Expression.minus(other: Expression): Expression = ExpressionImpl(this, FloatOp.SUBTRACT, other)
public infix fun Expression.times(other: Expression): Expression = ExpressionImpl(this, FloatOp.MULTIPLY, other)
public infix fun Expression.over(other: Expression): Expression = ExpressionImpl(this, FloatOp.DIVIDE, other)

//this is not typical map semantics -> don't make it public
fun Formula.treeMap(x: (Formula) -> Formula) =
        if (this.operator.cardinality == 0) {
            this
        } else {
            FormulaImpl(this.operator, this.subFormulas.map(x))
        }

fun Expression.treeMap(x: (Expression) -> Expression) =
        if (this is ExpressionImpl) {
            ExpressionImpl(x(left), this.operator, x(right))
        } else this