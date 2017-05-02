package com.github.sybila.huctl

import com.github.sybila.huctl.antlr.HUCTLBaseListener
import com.github.sybila.huctl.antlr.HUCTLLexer
import com.github.sybila.huctl.antlr.HUCTLParser
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

class HUCTLParser {

    fun formula(input: String): Formula = parse("val = $input")["val"]!!

    fun dirFormula(input: String): DirFormula
            = (formula("{$input}EX True") as? Formula.Simple<*>)?.direction
            ?: throw IllegalArgumentException("$input is not a direction formula")

    fun atom(input: String): Formula.Atom
            = formula(input) as? Formula.Atom
            ?: throw IllegalArgumentException("$input is not an atom")

    fun dirAtom(input: String): DirFormula.Atom
            = dirFormula(input) as? DirFormula.Atom
            ?: throw IllegalArgumentException("$input is not a direction atom")

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

        fun <R> String.resolved(action: () -> R): R {
            replaced.push(this)
            val result = action()
            replaced.pop()
            return result
        }

        //resolve a string alias to a specific type (Formula|DirFormula|Expression)
        fun resolveAlias(name: String): Any =
            if (name in replaced) {
                throw IllegalStateException("Cyclic reference: $name")
            } else name.resolved {
                references[name]?.run {
                    if (this.item is String) resolveAlias(this.item)
                    else this.item
                } ?: Expression.Variable(name)  //a = k and k is not defined, it defaults to variable name
            }

        //resolve references in an expression
        fun resolveExpression(e: Expression): Expression = e.mapLeafs({it}) { e ->
            val name = e.name
            if (name in replaced) {
                throw IllegalStateException("Cyclic reference: $name")
            } else name.resolved {
                references[name]?.run {
                    if (this.item is Expression) resolveExpression(this.item)
                    else throw IllegalStateException(
                            "Expected type of $name is an Expression."
                    )
                } ?: e  //e is just a model variable
            }
        }

        //resolve references in a direction formula
        fun resolveDirectionFormula(f: DirFormula): DirFormula = f.mapLeafs {
            if (it is DirFormula.Reference) {
                val name = it.name
                if (name in replaced) {
                    throw IllegalStateException("Cyclic reference: $name")
                } else name.resolved {
                    references[name]?.run {
                        if (this.item is DirFormula) resolveDirectionFormula(this.item)
                        else if (this.item is Formula)  //try up-casting
                            this.item.asDirFormula()?.let(::resolveDirectionFormula)
                            ?: throw IllegalStateException("$name cannot be cast to direction formula.")
                        else throw IllegalStateException(
                                "Expected type of $name is a direction formula."
                        )
                    } ?: throw IllegalStateException("Undefined reference $name")
                }
            } else it //True, False, Proposition
        }

        //Resolve references in a formula.
        fun resolveFormula(f: Formula): Formula = f.fold({
            when (this) {
                is Formula.Reference -> {
                    val name = this.name
                    if (name in replaced) {
                        throw IllegalStateException("Cyclic reference: $name")
                    } else name.resolved {
                        references[name]?.run {
                            if (this.item is Formula) resolveFormula(this.item)
                            else throw IllegalStateException("Expected type of $name is a formula.")
                        } ?: this   //this is a bound name
                    }
                }
                //dive into the expressions
                is Formula.Numeric -> this.copy(
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

        fun unboundNames(f: Formula): List<String> = f.fold({
            if (this is Formula.Reference) listOf(this.name) else listOf()
        }, { inner ->
            if (this is Formula.At) inner + listOf(this.name)
            else if (this is Formula.Bind) inner - listOf(this.name)
            else inner
        }, { l, r ->
            if (this is Formula.FirstOrder) l + (r - listOf(this.name))
            else l + r
        })


        //aliases need to go first, because all other resolve procedures need can depend on them
        for ((name, assignment) in references) {
            if (assignment.item is String) {
                references[name] = assignment.copy(item = resolveAlias(assignment.item))
            }
        }

        //the rest can run "in parallel"
        for ((name, assignment) in references) {
            references[name] = when (assignment.item) {
                is Expression -> assignment.copy(item = resolveExpression(assignment.item))
                is DirFormula -> assignment.copy(item = resolveDirectionFormula(assignment.item))
                is Formula -> assignment.copy(item = resolveFormula(assignment.item))
                else -> throw IllegalStateException("WTF?!")
            }
        }

        //check name bounds
        for ((name, assignment) in references) {
            if (assignment.item is Formula) {
                val unboundNames = unboundNames(assignment.item)
                if (unboundNames.isNotEmpty()) {
                    throw IllegalStateException("Unbound occurrence of $unboundNames in ${assignment.item}")
                }
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
            processStream(ANTLRInputStream(input.toCharArray(), input.length), "input_string")

    private fun processFile(input: File): FileContext =
            input.inputStream().use { processStream(ANTLRInputStream(it), input.absolutePath) }

    private fun processStream(input: ANTLRInputStream, location: String): FileContext {
        val lexer = HUCTLLexer(input)
        val parser = HUCTLParser(CommonTokenStream(lexer))
        lexer.removeErrorListeners()
        lexer.addErrorListener(errorListener)
        parser.removeErrorListeners()
        parser.addErrorListener(errorListener)
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
                    throw IllegalArgumentException(
                            "Duplicate assignment for ${it[0].name} defined in ${it[0].location} and ${it[1].location}"
                    )
                }
    }

    operator fun plus(ctx: ParserContext): ParserContext {
        return ParserContext(assignments + ctx.assignments)
    }

}

operator fun <T> ParseTreeProperty<T>.set(k: ParseTree, v: T) = this.put(k, v)
//operator fun <T> ParseTreeProperty<T>.get(k: ParseTree): T = this.get(k)
