package cz.muni.fi.ctl

//Formulas
public interface Formula {
    val operator: Op
    val subFormulas: List<Formula>

    public fun get(i: Int): Formula = subFormulas[i]
}

data class FormulaImpl (
        override val operator: Op = Op.ATOM,
        override val subFormulas: List<Formula> = listOf()
) : Formula {

    init {
        if (subFormulas.size() != operator.cardinality) {
            throw IllegalArgumentException(
                    "Operator cardinality(${operator.cardinality}) and " +
                    "number of sub formulas(${subFormulas.size()}) do not match")
        }
    }

    constructor(operator: Op, vararg formulas: Formula) : this(operator, formulas.toList())

    override fun toString(): String = when (operator.cardinality) {
        1 -> "$operator(${subFormulas[0]})"
        2 -> "${subFormulas[0]} $operator ${subFormulas[1]}"
        else -> "$operator($subFormulas)"
    }

}

open class Atom : Formula {
    final override val operator = Op.ATOM
    final override val subFormulas = listOf<Formula>()
}

//Boolean Atoms
val True = object : Atom() {
    override fun toString():String = "True"
}

val False = object : Atom() {
    override fun toString():String = "False"
}

//Float atoms
public data class FloatProposition (
        val variable: String,
        val floatOp: FloatOp,
        val value: Double
) : Atom() {
    override fun toString(): String = "$variable $floatOp $value"
}

//Direction atoms
public data class DirectionProposition (
        val variable: String,
        val direction: DirectionProposition.Direction,
        val facet: DirectionProposition.Facet
) : Atom() {

    override fun toString(): String = "$variable:$direction$facet"

    enum class Direction(val str: String) {
        IN("in"), OUT("out");
        override fun toString(): String = str
    }

    enum class Facet(val str: String) {
        POSITIVE("+"), NEGATIVE("-");
        override fun toString(): String = str
    }

}


//Simplified builders
fun not(f: Formula) = FormulaImpl(Op.NEGATION, f)
fun EX(f: Formula) = FormulaImpl(Op.EXISTS_NEXT, f)
fun AX(f: Formula) = FormulaImpl(Op.ALL_NEXT, f)
fun EF(f: Formula) = FormulaImpl(Op.EXISTS_FUTURE, f)
fun AF(f: Formula) = FormulaImpl(Op.ALL_FUTURE, f)
fun EG(f: Formula) = FormulaImpl(Op.EXISTS_GLOBAL, f)
fun AG(f: Formula) = FormulaImpl(Op.ALL_GLOBAL, f)
fun Formula.or(f2: Formula): Formula = FormulaImpl(Op.OR, this, f2)
fun Formula.and(f2: Formula): Formula = FormulaImpl(Op.AND, this, f2)
fun Formula.implies(f2: Formula): Formula = FormulaImpl(Op.IMPLICATION, this, f2)
fun Formula.equals(f2: Formula): Formula = FormulaImpl(Op.EQUIVALENCE, this, f2)
fun Formula.EU(f2: Formula): Formula = FormulaImpl(Op.EXISTS_UNTIL, this, f2)
fun Formula.AU(f2: Formula): Formula = FormulaImpl(Op.ALL_UNTIL, this, f2)
fun Formula.map(x: (Formula) -> Formula) = FormulaImpl(this.operator, this.subFormulas.map(x))
