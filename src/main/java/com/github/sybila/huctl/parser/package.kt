package com.github.sybila.huctl.parser

import com.github.sybila.fold
import com.github.sybila.huctl.*
import com.github.sybila.huctl.antlr.HUCTLLexer
import com.github.sybila.huctl.antlr.HUCTLParser
import com.github.sybila.huctl.dsl.*
import com.github.sybila.map
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.File
import java.util.*

/**
 *
 * The parser logic is based on pure functions and uses Assignment and FileContext as data objects.
 *
 * Workflow:
 *  - Antlr constructs a parse tree that is transformed into a FileContext.
 *  - File context is a direct representation of a file.
 *  - Includes in the FileContext are resolved and merged into one Assignment list.
 *  - Duplicate assignments are checked and an alias map is constructed (String -> Assignment).
 *  - Lastly, Parser resolves references (checking for undefined, cyclic and invalid type references)
 * and returns a final map of valid formula assignments
 *
 */

// Standard parsing functions

/**
 * Read [string] as a HUCTLp formula.
 *
 * Note that this does not support the full HUCTLp file format (references, includes, etc.).
 * Only one formula can be present in the string.
 */
fun readFormula(string: String): Formula = string.toFormula()

/**
 * Read [string] as a HUCTLp direction restriction.
 *
 * Note that this does not support the full HUCTLp file format (references, includes, etc.).
 * Only one direction restriction can be present in the string.
 */
fun readDirFormula(string: String): DirFormula = string.toDirFormula()

/**
 * Read [string] as a HUCTLp expression.
 *
 * Note that this does not support the full HUCTLp file format (references, includes, etc.).
 * Only one expression can be present in the string.
 */
fun readExpression(string: String): Expression = string.toExpression()

/**
 * Read [string] as a HUCTLp specification file.
 *
 * This assumes the string adheres to the HUCTLp file format (including references, includes, etc.).
 *
 * Use [onlyFlagged] to filter out formulas not marked with ":?".
 */
fun readHUCTLp(string: String, onlyFlagged: Boolean = false) = string.toHUCTLp(onlyFlagged)

/**
 * Read [file] as a HUCTLp specification file.
 *
 * This assumes the string adheres to the HUCTLp file format (including references, includes, etc.).
 *
 * Use [onlyFlagged] to filter out formulas not marked with ":?".
 */
fun readHUCTLp(file: File, onlyFlagged: Boolean = false) = file.toHUCTLp(onlyFlagged)

// And extension alternatives

/**
 * @see [readFormula]
 */
fun String.toFormula(): Formula = "val = $this".toHUCTLp(onlyFlagged = false)["val"]!!

/**
 * @see [readDirFormula]
 */
fun String.toDirFormula(): DirFormula = ("{$this}EX true".toFormula() as Formula.Next).direction

/**
 * @see [readExpression]
 */
fun String.toExpression(): Expression = ("$this == 0".toFormula() as Formula.Numeric).left

/**
 * @see [parseHUCTLp]
 */
fun String.toHUCTLp(onlyFlagged: Boolean = false): Map<String, Formula>
        = this.parse().resolveReferences(onlyFlagged).checkUnboundedNames()

/**
 * @see [parseHUCTLp]
 */
fun File.toHUCTLp(onlyFlagged: Boolean = false): Map<String, Formula>
        = this.parse().resolveReferences(onlyFlagged).checkUnboundedNames()

// Throw error when some formula contains unbounded names.
private fun Map<String, Formula>.checkUnboundedNames(): Map<String, Formula> {
    //check unbounded names
    this.values.forEach {
                it.unboundedNames().takeIf { it.isNotEmpty() }?.let { unbounded ->
                    error("Unbounded occurrence of $unbounded in $it")
                }
            }

    return this
}

// Resolve all references
private fun Map<String, Assignment<*>>.resolveReferences(onlyFlagged: Boolean): Map<String, Formula> {

    //Resolve references and finalize type checking.
    //However, some formulas are also valid as direction formulas. (Bool only)
    //In such cases, default to normal formulas and if needed, perform typecast.
    //
    //First resolve all aliases - this will assign some definite types to them.
    //(with Formulas being possibly later up-casted to DirectionFormulas)

    val references = HashMap(this) //mutable copy

    // resolve a string alias to a specific type (Formula|DirFormula|Expression)
    fun resolveAlias(name: String, stack: List<String>): Any =
        if (name in stack) {
            throw IllegalStateException("Cyclic reference for $name")
        } else {
            references[name]?.let { value ->
                if (value.item is String) resolveAlias(value.item, stack + name)
                else value.item
            } ?: name.toVar()   // When a = b and b is not defined, b is inferred as variable expression.
        }

    // resolve references in expressions
    fun resolveExpression(expression: Expression, stack: List<String>): Expression = expression.map(atom = {
        when {
            this !is Expression.Variable -> this    // Constants
            name in stack -> throw IllegalStateException("Cyclic reference: $name")
            name in references -> references[name]!!.let { substitute ->
                if (substitute.item !is Expression) throw IllegalStateException(
                        "Expected type of $name is Expression but " +
                        "inferred type is ${substitute.javaClass.simpleName}."
                ) else {
                    resolveExpression(substitute.item, stack + name)
                }
            }
            else -> this    // this is just a model variable
        }
    })

    // resolve references in direction formulas (with possible cast)
    fun resolveDirFormula(formula: DirFormula, stack: List<String>): DirFormula = formula.map(atom = {
        when {
            this !is DirFormula.Reference -> this   // True, False, Proposition
            name in stack -> throw IllegalStateException("Cyclic reference: $name")
            name !in references -> throw IllegalStateException("Undefined reference: $name")
            else -> {
                val substitute = references[name]!!
                if (substitute.item is DirFormula) {
                    resolveDirFormula(substitute.item, stack + name)
                } else if (substitute.item is Formula) {
                    substitute.item.toDirFormula()?.let { cast ->
                        resolveDirFormula(cast, stack + name)
                    } ?: throw IllegalStateException("$name cannot be cast to direction formula.")
                } else throw IllegalStateException(
                        "Expected type of $name is DirFormula but " +
                        "inferred type is ${substitute.javaClass.simpleName}."
                )

            }
        }
    })

    // Resolve references in HUCTLp formulas. Note that the references can be in the formula
    // or in some of it's parts (directions, propositions)
    fun resolveFormula(formula: Formula, stack: List<String>): Formula = formula.map(atom = {
        when {
            this is Formula.Numeric ->
                copy(left = resolveExpression(left, stack), right = resolveExpression(right, stack))
            this is Formula.Reference -> when (name) {
                in stack -> throw IllegalStateException("Cyclic reference: $name")
                !in references -> this // this is a bound name
                else -> references[name]!!.let { substitute ->
                    if (substitute.item !is Formula) throw IllegalStateException(
                            "Expected type of $name is Formula but " +
                                    "inferred type is ${substitute.javaClass.simpleName}."
                    ) else {
                        resolveFormula(substitute.item, stack + name)
                    }
                }
            }
            else -> this    // True, False, Transition
        }
    }, unary = { child ->
        when {
            this is Temporal -> this.copy(direction = resolveDirFormula(direction, stack), inner = child)
            else -> this.copy(inner = child)
        }
    }, binary = { l, r ->
        when {
            this is Formula.Until -> this.copy(direction = resolveDirFormula(direction, stack), path = l, reach = r)
            else -> this.copy(left = l, right = r)
        }
    })

    //aliases need to go first, because all other resolve procedures depend on them
    for ((name, assignment) in references) {
        if (assignment.item is String) {
            references[name] = assignment.copy(item = resolveAlias(assignment.item, emptyList()))
        }
    }

    //the rest can run "in parallel"
    for ((name, assignment) in references) {
        references[name] = when (assignment.item) {
            is Expression -> assignment.copy(item = resolveExpression(assignment.item, emptyList()))
            is DirFormula -> assignment.copy(item = resolveDirFormula(assignment.item, emptyList()))
            is Formula -> assignment.copy(item = resolveFormula(assignment.item, emptyList()))
            else -> error("unreachable")
        }
    }

    val result = HashMap<String, Formula>()
    for ((name, assignment) in references) {
        if (assignment.item is Formula && (!onlyFlagged || assignment.flagged)) {
            result[name] = assignment.item
        }
    }

    return result
}

// Read and resolve content of a file. Throw error on duplicate assignments.
private fun File.parse(): Map<String, Assignment<*>> = this.process().resolve(HashSet()).validate()

// Read and resolve content of a string. Throw error on duplicate assignments.
private fun String.parse(): Map<String, Assignment<*>> = this.process().resolve(HashSet()).validate()

// If the list contains duplicate assignments, throw error.
private fun List<Assignment<*>>.validate(): Map<String, Assignment<*>> {
    this    .map { (nameA) -> this.filter { (nameB) -> nameA == nameB }.toSet().toList() }
            .filter { it.size > 1 }
            .any {
                throw IllegalArgumentException("Duplicate assignment for ${it[0].name} " +
                        "defined in ${it[0].location} and ${it[1].location}")
            }
    return this.associateBy({ it.name }, { it })
}

// Resolve include statements in the FileContext. Skip locations included in the done set.
private fun FileContext.resolve(done: MutableSet<String>): List<Assignment<*>> {
    done.add(this.location)
    return this.includes
            .filter { it.absolutePath !in done }
            .fold(this.exportAssignments()) { a, i -> a + i.process().resolve(done) }
}

// Read content of a String into the FileContext.
private fun String.process(): FileContext = CharStreams.fromString(this).process("input_string")

// Read content of a File into the FileContext.
private fun File.process(): FileContext = this.inputStream().use {
    CharStreams.fromStream(it).process(this.absolutePath)
}

// Create a new parser and lexer and walk the given stream from root.
private fun CharStream.process(location: String): FileContext {
    val lexer = HUCTLLexer(this)
    val parser = HUCTLParser(CommonTokenStream(lexer))
    lexer.addErrorListener(errorListener)
    parser.addErrorListener(errorListener)
    val root = parser.root()
    val context = FileContext(location)
    ParseTreeWalker().walk(context, root)
    return context
}

// Throw an exception when parser/lexer error is first encountered.
private val errorListener = object : ANTLRErrorListener {
    override fun reportAttemptingFullContext(p0: Parser?, p1: DFA?, p2: Int, p3: Int, p4: BitSet?, p5: ATNConfigSet?) { }
    override fun syntaxError(p0: Recognizer<*, *>?, p1: Any?, line: Int, char: Int, msg: String?, p5: RecognitionException?) {
        throw IllegalArgumentException("Syntax error at $line:$char: $msg")
    }
    override fun reportAmbiguity(p0: Parser?, p1: DFA?, p2: Int, p3: Int, p4: Boolean, p5: BitSet?, p6: ATNConfigSet?) { }
    override fun reportContextSensitivity(p0: Parser?, p1: DFA?, p2: Int, p3: Int, p4: Int, p5: ATNConfigSet?) { }
}


/**
 * Safely up-cast a Formula to equivalent DirFormula.
 *
 * If no equivalent DirFormula exists, return null.
 */
private fun Formula.toDirFormula(): DirFormula?
        = this.fold(
        atom = { when (this) {
            is Formula.True -> DirFormula.True
            is Formula.False -> DirFormula.False
            is Formula.Text -> DirFormula.Text(this.value)
            is Formula.Reference -> DirFormula.Reference(this.name)
            else -> null
        } },
        unary = { it?.let { inner ->
            when (this) {
                is Formula.Not -> DirFormula.Not(inner)
                else -> null
            }
        } },
        binary = { a, b ->
            if (a == null || b == null) {
                null
            } else when (this) {
                is Formula.And -> a and b
                is Formula.Or -> a or b
                is Formula.Implies -> a implies b
                is Formula.Equals -> a equal b
                else -> null
            }
        }
)

// Compute all unbounded names in the given formula
private fun Formula.unboundedNames(): Set<String> = this.fold(
    atom = { if (this is Formula.Reference) setOf(name) else emptySet() },
    unary = { when {
        this is Formula.At -> it + name
        this is Formula.Bind -> it - name
        else -> it
    } },
    binary = { l, r -> when {
        this is Formula.ForAll -> l + (r - name)    // the parentheses are important!
        this is Formula.Exists -> l + (r - name)
        else -> l + r
    } }
)