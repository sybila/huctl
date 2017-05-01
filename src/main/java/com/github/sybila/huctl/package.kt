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

enum class Direction(private val str: String) {
    IN("in"), OUT("out");
    override fun toString(): String = str
}

enum class Facet(private val str: String) {
    POSITIVE("+"), NEGATIVE("-");
    override fun toString(): String = str
}