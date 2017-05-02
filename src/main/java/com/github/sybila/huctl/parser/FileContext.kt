package com.github.sybila.huctl.parser

import com.github.sybila.huctl.*
import com.github.sybila.huctl.antlr.HUCTLBaseListener
import com.github.sybila.huctl.antlr.HUCTLParser
import com.github.sybila.huctl.dsl.*
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeProperty
import java.io.File
import java.util.*

/**
 * A class responsible for parsing a single HUCTLp file.
 *
 * WARNING: This class does not resolve any include statements, aliases or references.
 */
internal class FileContext(val location: String) : HUCTLBaseListener() {

    val includes = ArrayList<File>()
    val formulas = ArrayList<Assignment<Formula>>()
    val dirFormulas = ArrayList<Assignment<DirFormula>>()
    val expressions = ArrayList<Assignment<Expression>>()
    val aliases = ArrayList<Assignment<String>>()

    private val formulaTree = ParseTreeProperty<Formula>()
    private val expressionTree = ParseTreeProperty<Expression>()
    private val dirFormulaTree = ParseTreeProperty<DirFormula>()

    fun exportAssignments(): List<Assignment<*>> = formulas + expressions + aliases + dirFormulas

    private val unaryTemporalConstructors = mapOf<String, (PathQuantifier, Formula, DirFormula) -> Formula>(
            "X" to { a,b,c -> Formula.Next(a, b, c) },
            "wX" to { a,b,c -> Formula.WeakNext(a, b, c) },
            "F" to { a,b,c -> Formula.Future(a, b, c) },
            "wF" to { a,b,c -> Formula.WeakFuture(a, b, c) },
            "G" to { a,b,c -> Formula.Globally(a, b, c) }
    )

    /* ----- Basic control flow ------ */

    override fun exitIncludeStatement(ctx: HUCTLParser.IncludeStatementContext) {
        val string = ctx.STRING().text
        includes.add(File(string.substring(1, string.length - 1)))    //remove quotes
    }

    override fun exitAssignStatement(ctx: HUCTLParser.AssignStatementContext) {
        fun <T: Any> put(data: MutableList<Assignment<T>>, item: T) {
            data.add(Assignment(ctx.VAR_NAME(0).text, item, "$location:${ctx.start.line}", ctx.FLAG() != null))
        }
        when {
            ctx.formula() != null -> put(formulas, formulaTree[ctx.formula()])
            ctx.dirFormula() != null -> put(dirFormulas, dirFormulaTree[ctx.dirFormula()])
            ctx.expression() != null -> put(expressions, expressionTree[ctx.expression()])
            else -> put(aliases, ctx.VAR_NAME(1).text)
        }
    }

    override fun visitErrorNode(node: ErrorNode) {
        throw IllegalArgumentException("Syntax error at '${node.text}' in $location:${node.symbol.line}")
    }

    /* ------ Formula Parsing ------ */

    override fun exitId(ctx: HUCTLParser.IdContext) {
        formulaTree[ctx] = Formula.Reference(ctx.text)
    }

    override fun exitBool(ctx: HUCTLParser.BoolContext) {
        formulaTree[ctx] = when {
            ctx.TRUE() != null -> Formula.True
            else -> Formula.False
        }
    }

    override fun exitTransition(ctx: HUCTLParser.TransitionContext) {
        formulaTree[ctx] = Formula.Transition(
                name = ctx.VAR_NAME().text,
                flow = if (ctx.IN() != null) Flow.IN else Flow.OUT,
                direction = if (ctx.PLUS() != null) Direction.POSITIVE else Direction.NEGATIVE
        )
    }

    override fun exitProposition(ctx: HUCTLParser.PropositionContext) {
        val left = expressionTree[ctx.expression(0)]
        val right = expressionTree[ctx.expression(1)]
        val cmp = ctx.compare()
        formulaTree[ctx] = when {
            cmp.EQ() != null -> left eq right
            cmp.NEQ() != null -> left neq right
            cmp.LT() != null -> left lt right
            cmp.LTEQ() != null -> left le right
            cmp.GT() != null -> left gt right
            else -> left ge right
        }
    }

    override fun exitText(ctx: HUCTLParser.TextContext) {
        val string = ctx.STRING().text
        formulaTree[ctx] = Formula.Text(string.substring(1, string.length - 1)) //remove quotes
    }

    override fun exitParenthesis(ctx: HUCTLParser.ParenthesisContext) {
        formulaTree[ctx] = formulaTree[ctx.formula()]
    }

    override fun exitNegation(ctx: HUCTLParser.NegationContext) {
        formulaTree[ctx] = Not(formulaTree[ctx.formula()])
    }

    private fun splitOperator(operator: String): Pair<PathQuantifier, String> {
        return if (operator.startsWith("p")) {
            PathQuantifier.valueOf(operator.take(2)) to operator.drop(2)
        } else (PathQuantifier.valueOf(operator.take(1)) to operator.drop(1))
    }

    override fun exitUnaryTemporal(ctx: HUCTLParser.UnaryTemporalContext) {
        val (path, state) = splitOperator(ctx.TEMPORAL_UNARY().text)
        val dir = ctx.dirModifier()?.let { dirFormulaTree[it.dirFormula()] } ?: DirFormula.True
        fun put(constructor: (PathQuantifier, Formula, DirFormula) -> Formula) {
            formulaTree[ctx] = constructor(path, formulaTree[ctx.formula()], dir)
        }
        put(unaryTemporalConstructors[state]!!)
    }

    override fun exitOr(ctx: HUCTLParser.OrContext) {
        formulaTree[ctx] = formulaTree[ctx.formula(0)] or formulaTree[ctx.formula(1)]
    }

    override fun exitAnd(ctx: HUCTLParser.AndContext) {
        formulaTree[ctx] = formulaTree[ctx.formula(0)] and formulaTree[ctx.formula(1)]
    }

    override fun exitImplies(ctx: HUCTLParser.ImpliesContext) {
        formulaTree[ctx] = formulaTree[ctx.formula(0)] implies formulaTree[ctx.formula(1)]
    }

    override fun exitEqual(ctx: HUCTLParser.EqualContext) {
        formulaTree[ctx] = formulaTree[ctx.formula(0)] equal formulaTree[ctx.formula(1)]
    }

    override fun exitAllUntil(ctx: HUCTLParser.AllUntilContext) {
        ctx.makeUntil(ctx.A_U().text, ctx.formula(0), ctx.formula(1),
                ctx.dirModifierL()?.dirModifier()?.dirFormula(),
                ctx.dirModifierR()?.dirModifier()?.dirFormula()
        )
    }

    override fun exitExistUntil(ctx: HUCTLParser.ExistUntilContext) {
        ctx.makeUntil(ctx.E_U().text, ctx.formula(0), ctx.formula(1),
                ctx.dirModifierL()?.dirModifier()?.dirFormula(),
                ctx.dirModifierR()?.dirModifier()?.dirFormula()
        )
    }

    private fun HUCTLParser.FormulaContext.makeUntil(op: String,
                                                     pathC: HUCTLParser.FormulaContext,
                                                     reachC: HUCTLParser.FormulaContext,
                                                     dirLC: HUCTLParser.DirFormulaContext?,
                                                     dirRC: HUCTLParser.DirFormulaContext?
    ) {
        val (path, state) = splitOperator(op)
        assert(state == "U")
        val left = formulaTree[pathC]
        val right = formulaTree[reachC]
        val dirL = dirFormulaTree[dirLC] ?: DirFormula.True
        val dirR = dirFormulaTree[dirRC] ?: DirFormula.True
        if (dirR != DirFormula.True) {
            //rewrite using U X
            val next = Formula.Next(path, right, dirR)
            formulaTree[this] = Formula.Until(path, left, next, dirL)
        } else {
            formulaTree[this] = Formula.Until(path, left, right, dirL)
        }
    }

    override fun exitFirstOrder(ctx: HUCTLParser.FirstOrderContext) {
        val bound = ctx.setBound()?.formula()?.let { formulaTree[it] } ?: Formula.True
        val name = ctx.VAR_NAME().text
        val inner = formulaTree[ctx.formula()]
        formulaTree.put(ctx, if (ctx.FORALL() != null) {
            Formula.ForAll(name, bound, inner)
        } else {
            Formula.Exists(name, bound, inner)
        })

    }

    override fun exitHybrid(ctx: HUCTLParser.HybridContext) {
        val name = ctx.VAR_NAME().text
        val inner = formulaTree[ctx.formula()]
        formulaTree.put(ctx, if (ctx.BIND() != null) {
            Formula.Bind(name, inner)
        } else {
            Formula.At(name, inner)
        })
    }

    /* ------ Direction formula parsing ------ */

    override fun exitDirId(ctx: HUCTLParser.DirIdContext) {
        dirFormulaTree[ctx] = DirFormula.Reference(ctx.text)
    }

    override fun exitDirAtom(ctx: HUCTLParser.DirAtomContext) {
        dirFormulaTree.put(ctx, when {
            ctx.TRUE() != null -> DirFormula.True
            ctx.LOOP() != null -> DirFormula.Loop
            else -> DirFormula.False
        })
    }

    override fun exitDirProposition(ctx: HUCTLParser.DirPropositionContext) {
        dirFormulaTree[ctx] = DirFormula.Proposition(
                ctx.VAR_NAME().text, if (ctx.PLUS() != null) Direction.POSITIVE else Direction.NEGATIVE
        )
    }

    override fun exitDirText(ctx: HUCTLParser.DirTextContext) {
        val string = ctx.STRING().text
        dirFormulaTree[ctx] = DirFormula.Text(string.substring(1, string.length - 1)) //remove quotes
    }

    override fun exitDirParenthesis(ctx: HUCTLParser.DirParenthesisContext) {
        dirFormulaTree[ctx] = dirFormulaTree[ctx.dirFormula()]
    }

    override fun exitDirNegation(ctx: HUCTLParser.DirNegationContext) {
        dirFormulaTree[ctx] = Not(dirFormulaTree[ctx.dirFormula()])
    }

    override fun exitDirAnd(ctx: HUCTLParser.DirAndContext) {
        dirFormulaTree[ctx] = dirFormulaTree[ctx.dirFormula(0)] and dirFormulaTree[ctx.dirFormula(1)]
    }

    override fun exitDirOr(ctx: HUCTLParser.DirOrContext) {
        dirFormulaTree[ctx] = dirFormulaTree[ctx.dirFormula(0)] or dirFormulaTree[ctx.dirFormula(1)]
    }

    override fun exitDirImplies(ctx: HUCTLParser.DirImpliesContext) {
        dirFormulaTree[ctx] = dirFormulaTree[ctx.dirFormula(0)] implies dirFormulaTree[ctx.dirFormula(1)]
    }

    override fun exitDirEqual(ctx: HUCTLParser.DirEqualContext) {
        dirFormulaTree[ctx] = dirFormulaTree[ctx.dirFormula(0)] equal dirFormulaTree[ctx.dirFormula(1)]
    }


    /* ------ Expression Parsing ----- */

    override fun exitExpId(ctx: HUCTLParser.ExpIdContext) {
        expressionTree[ctx] = ctx.text.toVar()
    }

    override fun exitExpValue(ctx: HUCTLParser.ExpValueContext) {
        expressionTree[ctx] = Expression.Constant(ctx.FLOAT_VAL().text.toDouble())
    }

    override fun exitExpParenthesis(ctx: HUCTLParser.ExpParenthesisContext) {
        expressionTree[ctx] = expressionTree[ctx.expression()]
    }

    override fun exitExpMultiply(ctx: HUCTLParser.ExpMultiplyContext) {
        expressionTree[ctx] = expressionTree[ctx.expression(0)] times expressionTree[ctx.expression(1)]
    }

    override fun exitExpDivide(ctx: HUCTLParser.ExpDivideContext) {
        expressionTree[ctx] = expressionTree[ctx.expression(0)] div expressionTree[ctx.expression(1)]
    }

    override fun exitExpAdd(ctx: HUCTLParser.ExpAddContext) {
        expressionTree[ctx] = expressionTree[ctx.expression(0)] plus expressionTree[ctx.expression(1)]
    }

    override fun exitExpSubtract(ctx: HUCTLParser.ExpSubtractContext) {
        expressionTree[ctx] = expressionTree[ctx.expression(0)] minus expressionTree[ctx.expression(1)]
    }


    // Small utility function
    private operator fun <T> ParseTreeProperty<T>.set(k: ParseTree, v: T) = this.put(k, v)

}
