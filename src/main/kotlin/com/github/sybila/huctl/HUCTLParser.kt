package com.github.sybila.huctl

import com.github.sybila.huctl.antlr.HUCTLBaseListener
import com.github.sybila.huctl.antlr.HUCTLLexer
import com.github.sybila.huctl.antlr.HUCTLParser
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
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

class HUCTLParser() {

    fun formula(input: String): Formula = parse("val = $input")["val"]!!

    fun parse(input: String, onlyFlagged: Boolean = false): Map<String, Formula>
            = process(FileParser().process(input), onlyFlagged)

    fun parse(input: File, onlyFlagged: Boolean = false): Map<String, Formula>
            = process(FileParser().process(input), onlyFlagged)

    private fun process(ctx: ParserContext, onlyFlagged: Boolean): Map<String, Formula> {

        //Resolve references and finalize type checking.
        //However, some formulas are also valid as direction formulas. (Bool only)
        //In such cases, default to normal formulas and if needed, perform typecast.
        //
        //First resolve all aliases - this will assign some definite types to them.
        //(with Formulas being possibly later up-casted to DirectionFormulas)

        val references = HashMap(ctx.toMap())  //mutable copy
        val replaced = Stack<String>()  //processing stack for cycle detection

        fun <R> String.stacked(action: () -> R): R {
            replaced.push(this)
            val result = action()
            replaced.pop()
            return result
        }

        //resolve a string alias to a specific type (Formula|DirectionFormula|Expression)
        fun resolveAlias(name: String): Any =
            if (name in replaced) {
                throw IllegalStateException("Cyclic reference: $name")
            } else {
                references[name]?.run {
                    if (this.item is String) resolveAlias(this.name)
                    else this.item
                } ?: Expression.Variable(name)  //a = k and k is not defined, it defaults to variable name
            }

        //resolve references in an expression
        fun resolveExpression(e: Expression): Expression = e.mapLeafs({it}) { e ->
            val name = e.name
            if (name in replaced) {
                throw IllegalStateException("Cyclic reference: $name")
            } else name.stacked {
                references[name]?.run {
                    if (this.item is Expression) resolveExpression(this.item)
                    else throw IllegalStateException(
                            "Expected type of $name is an Expression, not ${this.item.javaClass.simpleName}."
                    )
                } ?: e  //e is just a model variable
            }
        }

        //resolve references in a direction formula
        fun resolveDirectionFormula(f: DirectionFormula): DirectionFormula = f.mapLeafs {
            if (it is DirectionFormula.Atom.Reference) {
                val name = it.name
                if (name in replaced) {
                    throw IllegalStateException("Cyclic reference: $name")
                } else name.stacked {
                    references[name]?.run {
                        if (this.item is DirectionFormula) resolveDirectionFormula(this.item)
                        else if (this.item is Formula)  //try up-casting
                            this.item.asDirectionFormula()?.let(::resolveDirectionFormula)
                            ?: throw IllegalStateException("$name cannot be cast to direction formula.")
                        else throw IllegalStateException(
                                "Expected type of $name is a direction formula, not ${this.item.javaClass.simpleName}."
                        )
                    } ?: throw IllegalStateException("Undefined reference $name")
                }
            } else it //True, False, Proposition
        }

        //Resolve references in a formula.
        fun resolveFormula(f: Formula): Formula = f.fold({
            when (this) {
                is Formula.Atom.Reference -> {
                    val name = this.name
                    if (name in replaced) {
                        throw IllegalStateException("Cyclic reference: $name")
                    } else name.stacked {
                        references[name]?.run {
                            if (this.item is Formula) resolveFormula(this.item)
                            else throw IllegalStateException("Expected type of $name is a formula, not ${this.item.javaClass.simpleName}.")
                        } ?: throw IllegalStateException("Undefined reference $name")
                    }
                }
                //dive into the expressions
                is Formula.Atom.Float -> this.copy(
                        left = resolveExpression(this.left),
                        right = resolveExpression(this.right)
                )
                //True, False, Transition
                else -> this
            }
        }, { inner ->
            when (this) {   //resolve direction
                is Formula.Simple -> this.copy(
                        inner = inner, direction = resolveDirectionFormula(this.direction)
                )
                else -> this.copy(inner)
            }
        }, { left, right ->
            when (this) {   //resolve direction
                is Formula.Until -> this.copy(
                        path = left, reach = right, direction = resolveDirectionFormula(this.direction)
                )
                else -> this.copy(left, right)
            }
        })

        //aliases need to go first, because all other resolve procedures need can depend on them
        for ((name, assignment) in references) {
            if (assignment.item is String) {
                references[name] = assignment.copy(item = resolveAlias(assignment.item))
            } else if (assignment.item is Formula.Atom.Reference && assignment.item.name !in references) {
                references[name] = assignment.copy(item = assignment.item.name.asVariable())
            } else if (assignment.item is DirectionFormula.Atom.Reference && assignment.item.name !in references) {
                references[name] = assignment.copy(item = assignment.item.name.asVariable())
            }
        }

        //the rest can run "in parallel"
        for ((name, assignment) in references) {
            references[name] = when (assignment.item) {
                is Expression -> assignment.copy(item = resolveExpression(assignment.item))
                is DirectionFormula -> assignment.copy(item = resolveDirectionFormula(assignment.item))
                is Formula -> assignment.copy(item = resolveFormula(assignment.item))
                else -> throw IllegalStateException("WTF?!")
            }
        }

        val results = HashMap<String, Formula>()
        for ((name, assignment) in references) {
            if (assignment.item is Formula && (!onlyFlagged || assignment.flagged))
                results[name] = resolveFormula(assignment.item)
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
        val lexer = HUCTLLexer(input)
        val parser = HUCTLParser(CommonTokenStream(lexer))
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
        private val assignments: List<Assignment<*>>
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

private class FileContext(val location: String) : HUCTLBaseListener() {

    val includes = ArrayList<File>()
    val formulas = ArrayList<Assignment<Formula>>()
    val dirFormulas = ArrayList<Assignment<DirectionFormula>>()
    val expressions = ArrayList<Assignment<Expression>>()
    val aliases = ArrayList<Assignment<String>>()

    private val formulaTree = ParseTreeProperty<Formula>()
    private val expressionTree = ParseTreeProperty<Expression>()
    private val dirFormulaTree = ParseTreeProperty<DirectionFormula>()

    fun toParseContext() = ParserContext(formulas + expressions + aliases)

    /* ----- Basic control flow ------ */

    override fun exitIncludeStatement(ctx: HUCTLParser.IncludeStatementContext) {
        val string = ctx.STRING().text!!
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

    /* ------ Formula Parsing ------ */

    override fun exitId(ctx: HUCTLParser.IdContext) {
        formulaTree[ctx] = Formula.Atom.Reference(ctx.text)
    }

    override fun exitBool(ctx: HUCTLParser.BoolContext) {
        formulaTree[ctx] = if (ctx.TRUE() != null) True else False
    }

    override fun exitTransition(ctx: HUCTLParser.TransitionContext) {
        formulaTree[ctx] = Formula.Atom.Transition(
                name = ctx.VAR_NAME().text,
                direction = if (ctx.IN() != null) Direction.IN else Direction.OUT,
                facet = if (ctx.PLUS() != null) Facet.POSITIVE else Facet.NEGATIVE
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

    override fun exitParenthesis(ctx: HUCTLParser.ParenthesisContext) {
        formulaTree[ctx] = formulaTree[ctx.formula()]
    }

    override fun exitNegation(ctx: HUCTLParser.NegationContext) {
        formulaTree[ctx] = not(formulaTree[ctx.formula()])
    }

    private fun splitOperator(operator: String): Pair<PathQuantifier, String> {
        return if (operator.startsWith("p")) {
            PathQuantifier.valueOf(operator.take(2)) to operator.drop(2)
        } else (PathQuantifier.valueOf(operator.take(1)) to operator.drop(1))
    }

    override fun exitUnaryTemporal(ctx: HUCTLParser.UnaryTemporalContext) {
        val (path, state) = splitOperator(ctx.TEMPORAL_UNARY().text)
        val dir = ctx.dirModifier()?.let { dirFormulaTree[it.dirFormula()] } ?: DirectionFormula.Atom.True
        fun put(constructor: (PathQuantifier, Formula, DirectionFormula) -> Formula) {
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

    override fun exitExistUntil(ctx: HUCTLParser.ExistUntilContext) {
        val dirL = ctx.dirModifierL()?.let { dirFormulaTree[it.dirModifier().dirFormula()] } ?: DirectionFormula.Atom.True
        val dirR = ctx.dirModifierR()?.let { dirFormulaTree[it.dirModifier().dirFormula()] } ?: DirectionFormula.Atom.True
        val left = formulaTree[ctx.formula(0)]
        val right = formulaTree[ctx.formula(1)]
        val (path, state) = splitOperator(ctx.E_U().text)
        assert(state == "U")
        assert(path == PathQuantifier.E || path == PathQuantifier.pE)
        if (dirR != DirectionFormula.Atom.True) {
            //rewrite using U X
            val next = Formula.Simple.Next(path, right, dirR)
            formulaTree[ctx] = Formula.Until(path, left, next, dirL)
        } else {
            formulaTree[ctx] = Formula.Until(path, left, right, dirL)
        }
    }

    override fun exitAllUntil(ctx: HUCTLParser.AllUntilContext) {
        val dirL = ctx.dirModifierL()?.let { dirFormulaTree[it.dirModifier().dirFormula()] } ?: DirectionFormula.Atom.True
        val dirR = ctx.dirModifierR()?.let { dirFormulaTree[it.dirModifier().dirFormula()] } ?: DirectionFormula.Atom.True
        val left = formulaTree[ctx.formula(0)]
        val right = formulaTree[ctx.formula(1)]
        val (path, state) = splitOperator(ctx.A_U().text)
        assert(state == "U")
        assert(path == PathQuantifier.A || path == PathQuantifier.pA)
        if (dirR != DirectionFormula.Atom.True) {
            //rewrite using U X
            val next = Formula.Simple.Next(path, right, dirR)
            formulaTree[ctx] = Formula.Until(path, left, next, dirL)
        } else {
            formulaTree[ctx] = Formula.Until(path, left, right, dirL)
        }
    }

    override fun exitFirstOrder(ctx: HUCTLParser.FirstOrderContext) {
        val bound = ctx.setBound().formula()?.let { formulaTree[it] } ?: True
        val name = ctx.VAR_NAME().text
        val inner = formulaTree[ctx.formula()]
        formulaTree[ctx] =
                if (ctx.FORALL() != null) Formula.FirstOrder.ForAll(name, bound, inner)
                else Formula.FirstOrder.Exists(name, bound, inner)
    }

    override fun exitHybrid(ctx: HUCTLParser.HybridContext) {
        val name = ctx.VAR_NAME().text
        val inner = formulaTree[ctx.formula()]
        formulaTree[ctx] =
                if (ctx.BIND() != null) Formula.Hybrid.Bind(name, inner)
                else Formula.Hybrid.At(name, inner)
    }

    /* ------ Direction formula parsing ------ */

    override fun exitDirId(ctx: HUCTLParser.DirIdContext) {
        dirFormulaTree[ctx] = DirectionFormula.Atom.Reference(ctx.text)
    }

    override fun exitDirBool(ctx: HUCTLParser.DirBoolContext) {
        dirFormulaTree[ctx] = if (ctx.TRUE() != null) DirectionFormula.Atom.True else DirectionFormula.Atom.False
    }

    override fun exitDirProposition(ctx: HUCTLParser.DirPropositionContext) {
        dirFormulaTree[ctx] = DirectionFormula.Atom.Proposition(
                ctx.VAR_NAME().text, if (ctx.PLUS() != null) Facet.POSITIVE else Facet.NEGATIVE
        )
    }

    override fun exitDirParenthesis(ctx: HUCTLParser.DirParenthesisContext) {
        dirFormulaTree[ctx] = dirFormulaTree[ctx.dirFormula()]
    }

    override fun exitDirNegation(ctx: HUCTLParser.DirNegationContext) {
        dirFormulaTree[ctx] = not(dirFormulaTree[ctx.dirFormula()])
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
        expressionTree[ctx] = ctx.text.asVariable()
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

}

private val unaryTemporalConstructors = mapOf<String, (PathQuantifier, Formula, DirectionFormula) -> Formula>(
        "X" to { a,b,c -> Formula.Simple.Next(a, b, c) },
        "wX" to { a,b,c -> Formula.Simple.WeakNext(a, b, c) },
        "F" to { a,b,c -> Formula.Simple.Future(a, b, c) },
        "wF" to { a,b,c -> Formula.Simple.WeakFuture(a, b, c) },
        "G" to { a,b,c -> Formula.Simple.Globally(a, b, c) }
)

private data class Assignment<out T: Any>(
        val name: String,
        val item: T,
        val location: String,
        val flagged: Boolean
)

operator fun <T> ParseTreeProperty<T>.set(k: ParseTree, v: T) = this.put(k, v)
operator fun <T> ParseTreeProperty<T>.get(k: ParseTree): T = this.get(k)
