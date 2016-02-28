package com.github.sybila.ctl

enum class Op(val cardinality: Int, private val str: String) {
    //nullary
    ATOM(0,"null"),
    //unary
    NEGATION(1, "!"),
    EXISTS_NEXT(1, "EX"),
    ALL_NEXT(1, "AX"),
    EXISTS_FUTURE(1, "EF"),
    ALL_FUTURE(1, "AF"),
    EXISTS_GLOBAL(1, "EG"),
    ALL_GLOBAL(1, "AG"),
    //binary
    AND(2, "&&"),
    OR(2, "||"),
    IMPLICATION(2, "=>"),
    EQUIVALENCE(2, "<=>"),
    EXISTS_UNTIL(2, "EU"),
    ALL_UNTIL(2, "AU");

    override fun toString(): String = str
}

enum class CompareOp(private val str: String) {
    EQ("=="), NEQ("!="), GT(">"), GT_EQ(">="), LT("<"), LT_EQ("<=");

    val neg: CompareOp
        get() = when (this) {
            EQ -> NEQ
            NEQ -> EQ
            GT -> LT_EQ
            GT_EQ -> LT
            LT -> GT_EQ
            LT_EQ -> GT
        }

    override fun toString(): String = str

}

enum class FloatOp(private val str: String) {
    ADD("+"), SUBTRACT("-"), MULTIPLY("*"), DIVIDE("/");

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