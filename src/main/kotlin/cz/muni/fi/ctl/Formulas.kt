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
        if (subFormulas.size() != operator.cardinality) {
            throw IllegalArgumentException(
                    "Operator cardinality(${operator.cardinality}) and " +
                    "number of sub formulas(${subFormulas.size()}) do not match")
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
public val True: Formula = object : Atom {
    final override val operator = Op.ATOM
    final override val subFormulas = listOf<Formula>()
    override fun toString():String = "True"
}

public val False: Formula = object : Atom {
    final override val operator = Op.ATOM
    final override val subFormulas = listOf<Formula>()
    override fun toString():String = "False"
}

//Float atoms
public data class FloatProposition (
        val variable: String,
        val floatOp: FloatOp,
        val value: Double
) : Atom {
    final override val operator = Op.ATOM
    final override val subFormulas = listOf<Formula>()
    override fun toString(): String = "$variable $floatOp $value"
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


//Simplified builders
public fun not(f: Formula): Formula = FormulaImpl(Op.NEGATION, f)
public fun EX(f: Formula): Formula = FormulaImpl(Op.EXISTS_NEXT, f)
public fun AX(f: Formula): Formula = FormulaImpl(Op.ALL_NEXT, f)
public fun EF(f: Formula): Formula = FormulaImpl(Op.EXISTS_FUTURE, f)
public fun AF(f: Formula): Formula = FormulaImpl(Op.ALL_FUTURE, f)
public fun EG(f: Formula): Formula = FormulaImpl(Op.EXISTS_GLOBAL, f)
public fun AG(f: Formula): Formula = FormulaImpl(Op.ALL_GLOBAL, f)
public fun Formula.or(f2: Formula): Formula = FormulaImpl(Op.OR, this, f2)
public fun Formula.and(f2: Formula): Formula = FormulaImpl(Op.AND, this, f2)
public fun Formula.implies(f2: Formula): Formula = FormulaImpl(Op.IMPLICATION, this, f2)
public fun Formula.equal(f2: Formula): Formula = FormulaImpl(Op.EQUIVALENCE, this, f2)
public fun Formula.EU(f2: Formula): Formula = FormulaImpl(Op.EXISTS_UNTIL, this, f2)
public fun Formula.AU(f2: Formula): Formula = FormulaImpl(Op.ALL_UNTIL, this, f2)

//this is not typical map semantics -> don't make it public
fun Formula.treeMap(x: (Formula) -> Formula) =
        if (this.operator.cardinality == 0) {
            this
        } else {
            FormulaImpl(this.operator, this.subFormulas.map(x))
        }
