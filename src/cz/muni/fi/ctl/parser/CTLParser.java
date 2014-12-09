package cz.muni.fi.ctl.parser;

import cz.muni.fi.ctl.antlr.CTLGrammarLexer;
import cz.muni.fi.ctl.antlr.CTLGrammarParser;
import cz.muni.fi.ctl.formula.Formula;
import cz.muni.fi.ctl.formula.FormulaImpl;
import cz.muni.fi.ctl.formula.operator.BinaryOperator;
import cz.muni.fi.ctl.formula.operator.UnaryOperator;
import cz.muni.fi.ctl.formula.proposition.Contradiction;
import cz.muni.fi.ctl.formula.proposition.FloatProposition;
import cz.muni.fi.ctl.formula.proposition.Proposition;
import cz.muni.fi.ctl.formula.proposition.Tautology;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CTLParser {

    @NotNull
    public static Formula parse(@NotNull File input) throws IOException {
        @Nullable InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(input);
            @NotNull CTLGrammarParser parser = new CTLGrammarParser(
                                        new BufferedTokenStream(
                                        new CTLGrammarLexer(
                                        new ANTLRInputStream(
                                            inputStream))));
            return processContext(parser.formula());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    @NotNull
    public static Formula parse(@NotNull String input) throws IllegalStateException {
        @NotNull CTLGrammarParser parser = new CTLGrammarParser(
                                    new BufferedTokenStream(
                                    new CTLGrammarLexer(
                                    new ANTLRInputStream(
                                        input.toCharArray(), input.length()))));
        return processContext(parser.formula());
    }

    /** Main processing function - should handle any formula context. */
    @NotNull
    private static Formula processContext(CTLGrammarParser.FormulaContext input) {
        if (input instanceof CTLGrammarParser.UnaryContext) {
            return processUnaryOperator(((CTLGrammarParser.UnaryContext) input).op_unary());
        } else if (input instanceof CTLGrammarParser.BinTempContext) {
            return processBinaryTemporalOperator(((CTLGrammarParser.BinTempContext) input).op_binary_temp());
        } else if (input instanceof CTLGrammarParser.BinBoolContext) {
            return processBinaryBooleanOperator(((CTLGrammarParser.BinBoolContext) input).op_binary_bool());
        } else if (input instanceof CTLGrammarParser.PropositionContext) {
            return processProposition(((CTLGrammarParser.PropositionContext) input).prop().expression());
        } else {
            throw new IllegalStateException("Unsupported operation");
        }
    }

    /** Read and parse any proposition (Bool/Float). */
    @NotNull
    private static Proposition processProposition(@NotNull CTLGrammarParser.ExpressionContext expression) {
        if (expression.bool() != null) {     //Parse boolean literals
            if (expression.bool().TRUE() != null) {
                return new Tautology();
            } else {
                return new Contradiction();
            }
        } else {    //Parse float proposition
            CTLGrammarParser.ComparisonContext comparison = expression.comparison();
            FloatProposition.Operator operator;
            if (comparison.op_float().EQ() != null) {
                operator = FloatProposition.Operator.EQ;
            } else if (comparison.op_float().N_EQ() != null) {
                operator = FloatProposition.Operator.NEQ;
            } else if (comparison.op_float().GT() != null) {
                operator = FloatProposition.Operator.GT;
            } else if (comparison.op_float().LT() != null) {
                operator = FloatProposition.Operator.LT;
            } else if (comparison.op_float().GT_EQ() != null) {
                operator = FloatProposition.Operator.GT_EQ;
            } else {
                operator = FloatProposition.Operator.LT_EQ;
            }
            return new FloatProposition(Float.parseFloat(comparison.FLOAT_VAL().getText()), comparison.VAR_NAME().getText(), operator);
        }
    }

    /** Read and parse binary boolean operators. &&, ||, =>, <=> */
    @NotNull
    private static Formula processBinaryBooleanOperator(@NotNull CTLGrammarParser.Op_binary_boolContext context) {
        if (context.op_bool().AND() != null) {
            return new FormulaImpl(BinaryOperator.AND, processSubFormulas(context.formula()));
        } else if (context.op_bool().OR() != null) {
            return new FormulaImpl(BinaryOperator.OR, processSubFormulas(context.formula()));
        } else if (context.op_bool().IMPL() != null) {
            return new FormulaImpl(BinaryOperator.OR, Arrays.asList(
                    new FormulaImpl(UnaryOperator.NEGATION, processContext(context.formula(0))),
                    processContext(context.formula(1))
            ));
        } else if (context.op_bool().EQIV() != null) {
            @NotNull List<Formula> formulas = processSubFormulas(context.formula());
            return new FormulaImpl(BinaryOperator.OR, Arrays.asList(
                    new FormulaImpl(BinaryOperator.AND, formulas),
                    new FormulaImpl(BinaryOperator.AND, Arrays.asList(
                            new FormulaImpl(UnaryOperator.NEGATION, formulas.get(0)),
                            new FormulaImpl(UnaryOperator.NEGATION, formulas.get(1))
                    ))
            ));
        } else {
            throw new IllegalArgumentException("Unknown boolean operator");
        }
    }

    /** Read and parse binary temporal operators. EU, AU */
    @NotNull
    private static Formula processBinaryTemporalOperator(@NotNull CTLGrammarParser.Op_binary_tempContext context) {
        if (context.UNTIL() != null) {
            @NotNull BinaryOperator op = BinaryOperator.EXISTS_UNTIL;
            if (context.quantifier().ALL() != null) {
                op = BinaryOperator.ALL_UNTIL;
            }
            return new FormulaImpl(op, processSubFormulas(context.formula()));
        } else {
            throw new IllegalArgumentException("No until in formula");
        }
    }

    /** Read and parse all unary operators. */
    @NotNull
    private static Formula processUnaryOperator(CTLGrammarParser.Op_unaryContext context) {
        //Process negation
        if (context instanceof CTLGrammarParser.UnBoolContext) {
            @NotNull CTLGrammarParser.UnBoolContext expression = (CTLGrammarParser.UnBoolContext) context;
            if (expression.NEG() != null) {
                return new FormulaImpl(UnaryOperator.NEGATION, processContext(expression.formula()));
            } else {
                throw new IllegalArgumentException("Unknown boolean unary operator");
            }
        } else if (context instanceof CTLGrammarParser.UnTempContext) { //Process unary temporal operators
            @NotNull CTLGrammarParser.UnTempContext expression = (CTLGrammarParser.UnTempContext) context;
            if (expression.quantifier().ALL() != null) {
                return normalizeQuantifierAll(expression);
            } else if (expression.quantifier().EXISTS() != null) {
                return normalizeQuantifierExists(expression);
            } else {
                throw new IllegalArgumentException("Unknown quantifier operator.");
            }
        } else {
            throw new IllegalArgumentException("Unknown unary operator");
        }
    }

    /** Turn and temporal operator with All path qualifier into usable normal form. */
    @NotNull
    private static Formula normalizeQuantifierAll(@NotNull CTLGrammarParser.UnTempContext expression) {
        if (expression.path().NEXT() != null) {
            // AX p = !EX !p
            return
                    new FormulaImpl(UnaryOperator.NEGATION,
                            new FormulaImpl(UnaryOperator.EXISTS_NEXT,
                                    new FormulaImpl(UnaryOperator.NEGATION,
                                            processContext(expression.formula())
                                    )));
        } else if (expression.path().FUTURE() != null) {
            // AF p = A [ true U p ]
            return
                    new FormulaImpl(BinaryOperator.ALL_UNTIL, Arrays.asList(
                            new Tautology(),
                            processContext(expression.formula())
                    ));
        } else if (expression.path().GLOBAL() != null) {
            // AG p = ! E [ true U ! p ]
            return
                    new FormulaImpl(UnaryOperator.NEGATION,
                            new FormulaImpl(BinaryOperator.EXISTS_UNTIL, Arrays.asList(
                                    new Tautology(),
                                    new FormulaImpl(UnaryOperator.NEGATION,
                                            processContext(expression.formula())
                                    ))));
        } else {
            throw new IllegalArgumentException("Unknown path operator.");
        }
    }

    /** Turn and temporal operator with Exists path qualifier into usable normal form. */
    @NotNull
    private static Formula normalizeQuantifierExists(@NotNull CTLGrammarParser.UnTempContext expression) {
        if (expression.path().NEXT() != null) {
            // OK
            return new FormulaImpl(UnaryOperator.EXISTS_NEXT, processContext(expression.formula()));
        } else if (expression.path().FUTURE() != null) {
            // EF p = E [ true U p ]
            return new FormulaImpl(BinaryOperator.EXISTS_UNTIL, Arrays.asList(
                    new Tautology(),
                    new FormulaImpl(UnaryOperator.NEGATION,
                            processContext(expression.formula())
                    )));
        } else if (expression.path().GLOBAL() != null) {
            // EG p = ! A [ true U ! p ]
            return new FormulaImpl(UnaryOperator.NEGATION,
                    new FormulaImpl(BinaryOperator.ALL_UNTIL, Arrays.asList(
                            new Tautology(),
                            new FormulaImpl(UnaryOperator.NEGATION,
                                    processContext(expression.formula())
                            ))));
        } else {
            throw new IllegalArgumentException("Unknown path operator.");
        }
    }

    @NotNull
    private static List<Formula> processSubFormulas(@NotNull List<CTLGrammarParser.FormulaContext> formulaContextList) {
        return formulaContextList.stream().map(CTLParser::processContext).collect(Collectors.toList());
    }

}
