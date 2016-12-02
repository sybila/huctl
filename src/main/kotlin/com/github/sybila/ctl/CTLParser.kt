package com.github.sybila.ctl

import com.github.sybila.ctl.antlr.CTLBaseListener
import com.github.sybila.ctl.antlr.CTLLexer
import com.github.sybila.ctl.antlr.CTLParser
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import org.antlr.v4.runtime.tree.ErrorNode
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeProperty
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.File
import java.util.*

/*
 * Workflow:
 * Antlr constructs a parse tree that is transformed into FileContext.
 * File context is a direct representation of a file.
 * Using FileParser, includes in the file context are resolved and merged into a ParserContext.
 * Duplicate assignments are checked at this stage.
 * Lastly, Parser resolves references in ParserContext (checking for undefined, cyclic and invalid type references)
 * and returns a final map of valid formula assignments
 */

class CTLParser() {

    fun formula(input: String): Formula = parse("val = $input")["val"]!!

    fun parse(input: String): Map<String, Formula> = process(FileParser().process(input))

    fun parse(input: File): Map<String, Formula> = process(FileParser().process(input))

    private fun process(ctx: ParserContext): Map<String, Formula> {

        //Resolve references
        //First resolve all aliases - we need to find out whether they reference formulas or expressions
        //Only formulas are returned.

        val references = HashMap(ctx.toMap())  //mutable copy
        val replaced = Stack<String>()  //processing stack for cycle detection

        fun <R> stacked(name: String, action: () -> R): R {
            replaced.push(name)
            val result = action()
            replaced.pop()
            return result
        }

        fun resolveAlias(a: Assignment): Assignment =
            when {
                a !is AliasAssignment -> a
                a.name in replaced -> throw IllegalStateException("Cyclic reference ${a.name} in ${a.location}")
                a.alias !in references -> ExpressionAssignment(a.name, a.alias.asVariable(), a.location)
                else -> stacked(a.name) { resolveAlias(references[a.alias]!!) }
            }

        fun resolveExpression(e: Expression): Expression = e.mapLeafs({it}) { e ->
            val name = e.name
            when (name) {
                in replaced -> throw IllegalStateException("Cyclic reference: $name")
                in references -> stacked(name) {
                    val assignment = references[name]!!
                    if (assignment is ExpressionAssignment) resolveExpression(assignment.expression)
                    else throw IllegalStateException("$name is a formula. Expression needed.")
                }
                else -> e   //This is a valid variable, not a reference
            }
        }

        fun resolveFormula(f: Formula): Formula = f.mapLeafs {
            when (it.proposition) {
                is Proposition.Reference -> {
                    val name = it.proposition.value
                    when (name) {
                        in replaced -> throw IllegalStateException("Cyclic reference: $name")
                        !in references -> throw IllegalStateException("Undefined reference: $name")
                        else -> stacked(name) {
                            val assignment = references[name]!!
                            if (assignment is FormulaAssignment) resolveFormula(assignment.formula)
                            else throw IllegalStateException("$name is an expression. Formula needed.")
                        }
                    }
                }
                //dive into the expressions
                is Proposition.Comparison<*> -> it.proposition.copy(
                        resolveExpression(it.proposition.left),
                        resolveExpression(it.proposition.right)
                ).asAtom()
                //True, False, Direction
                else -> it
            }
        }

        for ((name, assignment) in references) {    //resolve aliases
            references[name] = resolveAlias(assignment)
        }

        for ((name, assignment) in references) {    //resolve expressions - this way we catch errors also for unused expressions
            if (assignment is ExpressionAssignment) references[name] = ExpressionAssignment(name, resolveExpression(assignment.expression), assignment.location)
        }

        val results = HashMap<String, Formula>()
        for ((name, assignment) in references) {
            if (assignment is FormulaAssignment) results[name] = resolveFormula(assignment.formula)
        }

        return results
    }

}

private class FileParser {

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
        val lexer = CTLLexer(input)
        val parser = CTLParser(CommonTokenStream(lexer))
        lexer.removeErrorListeners()
        lexer.addErrorListener(errorListener)
        parser.removeErrorListeners()
        lexer.addErrorListener(errorListener)
        val root = parser.root()
        val context = FileContext(location)
        ParseTreeWalker().walk(context, root)
        return context
    }

    private val errorListener = object : ANTLRErrorListener {
        override fun reportAttemptingFullContext(p0: Parser?, p1: DFA?, p2: Int, p3: Int, p4: BitSet?, p5: ATNConfigSet?) { }
        override fun syntaxError(p0: Recognizer<*, *>?, p1: Any?, line: Int, char: Int, msg: String?, p5: RecognitionException?) {
            throw IllegalArgumentException("Syntax error at $line:$char: $msg")
        }
        override fun reportAmbiguity(p0: Parser?, p1: DFA?, p2: Int, p3: Int, p4: Boolean, p5: BitSet?, p6: ATNConfigSet?) { }
        override fun reportContextSensitivity(p0: Parser?, p1: DFA?, p2: Int, p3: Int, p4: Int, p5: ATNConfigSet?) { }
    }

}

private data class ParserContext(
        private val assignments: List<Assignment>
) {

    fun toMap() = assignments.associateBy({ it.name }, { it })

    /*
     * Checks for duplicate assignments received from parser
     */
    init {
        assignments
                .map { one -> assignments.filter { two -> one.name == two.name }.toSet().toList() }
                .filter { it.size > 1 }
                .any {
                    throw IllegalStateException(
                            "Duplicate assignment for ${it[0].name} defined in ${it[0].location} and ${it[1].location}"
                    )
                }
    }

    operator fun plus(ctx: ParserContext): ParserContext {
        return ParserContext(assignments + ctx.assignments)
    }

}

private class FileContext(val location: String) : CTLBaseListener() {

    val includes = ArrayList<File>()
    val formulas = ArrayList<FormulaAssignment>()
    val expressions = ArrayList<ExpressionAssignment>()
    val aliases = ArrayList<AliasAssignment>()

    private val formulaTree = ParseTreeProperty<Formula>()
    private val expressionTree = ParseTreeProperty<Expression>()

    fun toParseContext() = ParserContext(formulas + expressions + aliases)

    override fun exitIncludeStatement(ctx: CTLParser.IncludeStatementContext) {
        val string = ctx.STRING().text!!
        includes.add(File(string.substring(1, string.length - 1)))    //remove quotes
    }

    override fun exitAssignFormula(ctx: CTLParser.AssignFormulaContext) {
        formulas.add(FormulaAssignment(
                ctx.VAR_NAME().text,
                formulaTree[ctx.formula()],
                location + ":" + ctx.start.line
        ))
    }

    override fun exitAssignExpression(ctx: CTLParser.AssignExpressionContext) {
        expressions.add(ExpressionAssignment(
                ctx.VAR_NAME().text,
                expressionTree[ctx.expression()],
                location + ":" + ctx.start.line
        ))
    }

    override fun exitAssignAlias(ctx: CTLParser.AssignAliasContext) {
        aliases.add(AliasAssignment(
                ctx.VAR_NAME(0).text!!,
                ctx.VAR_NAME(1).text!!,
                location + ":" + ctx.start.line
        ))
    }

    override fun visitErrorNode(node: ErrorNode) {
        throw IllegalArgumentException("Parse error: $node")
    }

    /* ------ Expression Parsing ----- */

    override fun exitIdExpression(ctx: CTLParser.IdExpressionContext) {
        expressionTree[ctx] = Expression.Variable(ctx.text)
    }

    override fun exitValue(ctx: CTLParser.ValueContext) {
        expressionTree[ctx] = Expression.Constant(ctx.FLOAT_VAL().text.toDouble())
    }

    override fun exitParenthesisExpression(ctx: CTLParser.ParenthesisExpressionContext) {
        expressionTree[ctx] = expressionTree[ctx.expression()]
    }

    override fun exitMultiplication(ctx: CTLParser.MultiplicationContext) {
        expressionTree[ctx] = expressionTree[ctx.expression(0)] times expressionTree[ctx.expression(1)]
    }

    override fun exitDivision(ctx: CTLParser.DivisionContext) {
        expressionTree[ctx] = expressionTree[ctx.expression(0)] div expressionTree[ctx.expression(1)]
    }

    override fun exitAddition(ctx: CTLParser.AdditionContext) {
        expressionTree[ctx] = expressionTree[ctx.expression(0)] plus expressionTree[ctx.expression(1)]
    }

    override fun exitSubtraction(ctx: CTLParser.SubtractionContext) {
        expressionTree[ctx] = expressionTree[ctx.expression(0)] minus expressionTree[ctx.expression(1)]
    }

    /* ------ Formula Parsing ------ */

    override fun exitId(ctx: CTLParser.IdContext) {
        formulaTree[ctx] = Proposition.Reference(ctx.text!!).asAtom()
    }

    override fun exitBool(ctx: CTLParser.BoolContext) {
        formulaTree[ctx] = (if (ctx.TRUE() != null) tt() else ff()).asAtom()
    }

    override fun exitDirection(ctx: CTLParser.DirectionContext) {
        formulaTree[ctx] = Proposition.DirectionProposition(
                variable = ctx.VAR_NAME().text!!,
                direction = if (ctx.IN() != null) Direction.IN else Direction.OUT,
                facet = if (ctx.PLUS() != null) Facet.POSITIVE else Facet.NEGATIVE
        ).asAtom()
    }

    override fun exitProposition(ctx: CTLParser.PropositionContext) {
        val constructor = compareConstructors.first {
            it.first.invoke(ctx.compare()) != null
        }.second
        formulaTree[ctx] = constructor(expressionTree[ctx.expression(0)], expressionTree[ctx.expression(1)]).asAtom()

    }

    override fun exitParenthesis(ctx: CTLParser.ParenthesisContext) {
        formulaTree[ctx] = formulaTree[ctx.formula()]
    }

    override fun exitUnary(ctx: CTLParser.UnaryContext) {
        val constructor = unaryOpConstructors.first {
            it.first.invoke(ctx.unaryOp()) != null
        }.second
        formulaTree[ctx] = constructor(formulaTree[ctx.formula()])
    }

    override fun exitOr(ctx: CTLParser.OrContext) {
        formulaTree[ctx] = formulaTree[ctx.formula(0)] or formulaTree[ctx.formula(1)]
    }

    override fun exitAnd(ctx: CTLParser.AndContext) {
        formulaTree[ctx] = formulaTree[ctx.formula(0)] and formulaTree[ctx.formula(1)]
    }

    override fun exitImplies(ctx: CTLParser.ImpliesContext) {
        formulaTree[ctx] = formulaTree[ctx.formula(0)] implies formulaTree[ctx.formula(1)]
    }

    override fun exitEqual(ctx: CTLParser.EqualContext) {
        formulaTree[ctx] = formulaTree[ctx.formula(0)] equal formulaTree[ctx.formula(1)]
    }

    override fun exitEU(ctx: CTLParser.EUContext) {
        formulaTree[ctx] = formulaTree[ctx.formula(0)] EU formulaTree[ctx.formula(1)]
    }

    override fun exitAU(ctx: CTLParser.AUContext) {
        formulaTree[ctx] = formulaTree[ctx.formula(0)] AU formulaTree[ctx.formula(1)]
    }
}

private interface Assignment {
    val name: String
    val location: String
}

private data class FormulaAssignment(
        override val name: String, val formula: Formula, override val location: String
) : Assignment
private data class ExpressionAssignment(
        override val name: String, val expression: Expression, override val location: String
) : Assignment
private data class AliasAssignment(
        override val name: String, val alias: String, override val location: String
) : Assignment

//convenience methods

private val unaryOpConstructors = listOf(
        (CTLParser.UnaryOpContext::EX to ::EX),
        (CTLParser.UnaryOpContext::AX to ::AX),
        (CTLParser.UnaryOpContext::EG to ::EG),
        (CTLParser.UnaryOpContext::AG to ::AG),
        (CTLParser.UnaryOpContext::EF to ::EF),
        (CTLParser.UnaryOpContext::AF to ::AF),
        (CTLParser.UnaryOpContext::NEG to ::not)
)

private val compareConstructors = listOf(
        (CTLParser.CompareContext::EQ to Expression::eq),
        (CTLParser.CompareContext::NEQ to Expression::neq),
        (CTLParser.CompareContext::LT to Expression::lt),
        (CTLParser.CompareContext::LTEQ to Expression::le),
        (CTLParser.CompareContext::GT to Expression::gt),
        (CTLParser.CompareContext::GTEQ to Expression::ge)
)

operator fun <T> ParseTreeProperty<T>.set(k: ParseTree, v: T) = this.put(k, v)
operator fun <T> ParseTreeProperty<T>.get(k: ParseTree): T = this.get(k)
