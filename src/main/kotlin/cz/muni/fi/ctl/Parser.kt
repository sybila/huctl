package cz.muni.fi.ctl

import cz.muni.fi.ctl.antlr.CTLBaseListener
import cz.muni.fi.ctl.antlr.CTLLexer
import cz.muni.fi.ctl.antlr.CTLParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeProperty
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.File
import java.util.*

/**
 * Workflow:
 * Antlr constructs a parse tree that is transformed into FileContext.
 * File context is a direct representation of a file.
 * Using FileParser, includes in the file context are resolved and merged into a ParserContext.
 * Duplicate assignments are checked at this stage.
 * Lastly, Parser resolves references in ParserContext (checking for undefined or cyclic references)
 * and returns a final map of valid assignments
 */

public class Parser {

    public fun formula(input: String): Formula = parse("val = $input").get("val")!!

    public fun parse(input: String): Map<String, Formula> = process(FileParser().process(input))

    public fun parse(input: File): Map<String, Formula> = process(FileParser().process(input))

    fun process(ctx: ParserContext): Map<String, Formula> {

        val formulas = ctx.formulas

        // Resolve references

        val references = HashMap(formulas)  //mutable copy
        val replaced = Stack<String>()  //processing stack for cycle detection

        fun replace(f: Formula): Formula = when (f) {
            is Reference -> when {
                f.name !in references ->
                    throw IllegalStateException("Undefined reference: ${f.name}")
                f.name in replaced ->
                    throw IllegalStateException("Cyclic reference: ${f.name}")
                else -> {
                    replaced.push(f.name)
                    val result = references[f.name]!!.treeMap(::replace)
                    replaced.pop()
                    result
                }
            }
            else -> f.treeMap(::replace)
        }

        for ((name, formula) in formulas) {
            references[name] = replace(formula)
        }

        return references
    }

}

class FileParser {

    private val processed = HashSet<File>()

    fun process(input: String): ParserContext {
        val ctx = processString(input)
        return ctx.includes.map { process(it) }.fold(ctx.toParseContext(), ParserContext::plus)
    }

    fun process(input: File): ParserContext {
        val ctx = processFile(input)
        processed.add(input)
        return ctx.includes.
                filter { it !in processed }.
                map { process(it) }.
                fold (ctx.toParseContext(), ParserContext::plus)
    }

    private fun processString(input: String): FileContext =
            processStream(ANTLRInputStream(input.toCharArray(), input.length), "input string")

    private fun processFile(input: File): FileContext =
            input.inputStream().use { processStream(ANTLRInputStream(it), input.absolutePath) }

    private fun processStream(input: ANTLRInputStream, location: String): FileContext {
        val root = CTLParser(CommonTokenStream(CTLLexer(input))).root()
        val context = FileContext(location)
        ParseTreeWalker().walk(context, root)
        return context
    }

}

data class ParserContext(
        val assignments: List<Assignment>
) {

    val formulas: Map<String, Formula>
        get() = assignments.toMap({ it.name }, { it.formula} )

    /**
     * Checks for duplicate assignments received from parser
     */
    init {
        for (one in assignments) {
            for (two in assignments) {
                if (one.name == two.name && (one.location != two.location || one.formula != two.formula)) {
                    throw IllegalStateException(
                            "Duplicate assignment for ${one.name} defined in ${one.location} and ${two.location}"
                    )
                }
            }
        }
    }

    operator fun plus(ctx: ParserContext): ParserContext {
        return ParserContext(assignments + ctx.assignments)
    }

}

class FileContext(val location: String) : CTLBaseListener() {

    val includes = ArrayList<File>()
    val assignments = ArrayList<Assignment>()

    private val formulas = ParseTreeProperty<Formula>()

    fun toParseContext() = ParserContext(assignments)

    override fun exitInclude(ctx: CTLParser.IncludeContext) {
        val string = ctx.STRING().text!!
        includes.add(File(string.substring(1, string.length - 1)))    //remove quotes
    }

    override fun exitAssign(ctx: CTLParser.AssignContext) {
        assignments.add(Assignment(
                ctx.VAR_NAME().text!!,
                formulas[ctx.formula()],
                location+":"+ctx.start.line
        ))
    }

    /** ------ Formula Parsing ------ **/

    override fun exitId(ctx: CTLParser.IdContext) {
        formulas[ctx] = Reference(ctx.text!!)
    }

    override fun exitBool(ctx: CTLParser.BoolContext) {
        formulas[ctx] = if (ctx.TRUE() != null) True else False
    }

    override fun exitDirection(ctx: CTLParser.DirectionContext) {
        formulas[ctx] = DirectionProposition(
                variable = ctx.VAR_NAME().text!!,
                direction = if (ctx.IN() != null) Direction.IN else Direction.OUT,
                facet = if (ctx.PLUS() != null) Facet.POSITIVE else Facet.NEGATIVE
        )
    }

    override fun exitProposition(ctx: CTLParser.PropositionContext) {
        formulas[ctx] = FloatProposition(
                variable = ctx.VAR_NAME().text!!,
                floatOp = ctx.floatOp().toOperator(),
                value = ctx.FLOAT_VAL().text.toDouble()
        )
    }

    override fun exitParenthesis(ctx: CTLParser.ParenthesisContext) {
        formulas[ctx] = formulas[ctx.formula()]
    }

    override fun exitUnary(ctx: CTLParser.UnaryContext) {
        formulas[ctx] = FormulaImpl(ctx.unaryOp().toOperator(), formulas[ctx.formula()])
    }

    override fun exitOr(ctx: CTLParser.OrContext) {
        formulas[ctx] = formulas[ctx.formula(0)] or formulas[ctx.formula(1)]
    }

    override fun exitAnd(ctx: CTLParser.AndContext) {
        formulas[ctx] = formulas[ctx.formula(0)] and formulas[ctx.formula(1)]
    }

    override fun exitImplies(ctx: CTLParser.ImpliesContext) {
        formulas[ctx] = formulas[ctx.formula(0)] implies formulas[ctx.formula(1)]
    }

    override fun exitEqual(ctx: CTLParser.EqualContext) {
        formulas[ctx] = formulas[ctx.formula(0)] equal  formulas[ctx.formula(1)]
    }

    override fun exitEU(ctx: CTLParser.EUContext) {
        formulas[ctx] = formulas[ctx.formula(0)] EU formulas[ctx.formula(1)]
    }

    override fun exitAU(ctx: CTLParser.AUContext) {
        formulas[ctx] = formulas[ctx.formula(0)] AU formulas[ctx.formula(1)]
    }
}

data class Assignment(val name: String, val formula: Formula, val location: String)

data class Reference(val name: String) : Atom {
    final override val operator = Op.ATOM
    final override val subFormulas = listOf<Formula>()
}

//convenience methods

fun CTLParser.UnaryOpContext.toOperator(): Op = when {
    EX() != null -> Op.EXISTS_NEXT
    AX() != null -> Op.ALL_NEXT
    EF() != null -> Op.EXISTS_FUTURE
    AF() != null -> Op.ALL_FUTURE
    EG() != null -> Op.EXISTS_GLOBAL
    AG() != null -> Op.ALL_GLOBAL
    else -> Op.NEGATION
}

fun CTLParser.FloatOpContext.toOperator(): FloatOp = when {
    NEQ() != null -> FloatOp.NEQ
    LT() != null -> FloatOp.LT
    LTEQ() != null -> FloatOp.LT_EQ
    GT() != null -> FloatOp.GT
    GTEQ() != null -> FloatOp.GT_EQ
    else -> FloatOp.EQ
}

operator fun <T> ParseTreeProperty<T>.set(k: ParseTree, v: T) = this.put(k, v)
operator fun <T> ParseTreeProperty<T>.get(k: ParseTree): T = this.get(k)
