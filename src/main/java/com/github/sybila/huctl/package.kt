package com.github.sybila.huctl

/**
 * Specifies the type of temporal path quantifier.
 *  - [A]: All paths
 *  - [E]: Exists path
 *  - [pA]: All past paths
 *  - [pE]: Exists past path
 */
enum class PathQuantifier {
    A, E, pA, pE;
}

enum class CompareOp(private val str: String) {
    GT(">"), GE(">="), EQ("=="), NEQ("!="), LE("<="), LT("<");

    override fun toString(): String = str
}

enum class Flow(private val str: String) {
    IN("in"), OUT("out");
    override fun toString(): String = str
}

enum class Direction(private val str: String) {
    POSITIVE("+"), NEGATIVE("-");
    override fun toString(): String = str
}

interface Unary<This, Tree> where This : Unary<This, Tree> {
    val inner: Tree
    fun copy(inner: Tree = this.inner): This
}

interface Binary<This, Tree> where This : Binary<This, Tree> {
    val left: Tree
    val right: Tree
    fun copy(left: Tree = this.left, right: Tree = this.right): This
}