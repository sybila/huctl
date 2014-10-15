package cz.muni.fi.ctl.parser;

import cz.muni.fi.ctl.antlr.CTLGrammarLexer;
import cz.muni.fi.ctl.antlr.CTLGrammarParser;
import cz.muni.fi.ctl.formula.Formula;
import cz.muni.fi.ctl.formula.FormulaImpl;
import cz.muni.fi.ctl.formula.operator.BinaryOperator;
import cz.muni.fi.ctl.formula.operator.UnaryOperator;
import cz.muni.fi.ctl.formula.proposition.Contradiction;
import cz.muni.fi.ctl.formula.proposition.FloatProposition;
import cz.muni.fi.ctl.formula.proposition.Tautology;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.TokenStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CTLParser {

    public static Formula parse(File input) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(input);
            CTLGrammarParser parser =   new CTLGrammarParser(
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

    public static Formula parse(String input) throws IllegalStateException {
        input = input.replace(" ", ""); //remove all spaces (they drive parser crazy!)
        CTLGrammarParser parser =   new CTLGrammarParser(
                                    new BufferedTokenStream(
                                    new CTLGrammarLexer(
                                    new ANTLRInputStream(
                                        input.toCharArray(), input.length()))));
        return processContext(parser.formula());
    }

    private static Formula processContext(CTLGrammarParser.FormulaContext input) {
        //TODO update for new parser after grammar recompiled
        if (input instanceof CTLGrammarParser.ExistsUntilContext) {
            CTLGrammarParser.ExistsUntilContext context = (CTLGrammarParser.ExistsUntilContext) input;
            return new FormulaImpl(BinaryOperator.EXISTS_UNTIL, processSubFormulas(context.formula()));
        } else if (input instanceof CTLGrammarParser.NegationContext) {
            CTLGrammarParser.NegationContext context = (CTLGrammarParser.NegationContext) input;
            return new FormulaImpl(UnaryOperator.NEGATION, processContext(context.formula()));
        } else if (input instanceof CTLGrammarParser.ExistsFutureContext) {
            CTLGrammarParser.ExistsFutureContext context = (CTLGrammarParser.ExistsFutureContext) input;
            return new FormulaImpl(UnaryOperator.EXISTS_NEXT, processContext(context.formula()));
        } else if (input instanceof CTLGrammarParser.AllFutureContext) {
            CTLGrammarParser.AllFutureContext context = (CTLGrammarParser.AllFutureContext) input;
            return new FormulaImpl(UnaryOperator.ALL_FUTURE, processContext(context.formula()));
        } else if (input instanceof CTLGrammarParser.AndContext) {
            CTLGrammarParser.AndContext context = (CTLGrammarParser.AndContext) input;
            return new FormulaImpl(BinaryOperator.AND, processSubFormulas(context.formula()));
        } else if (input instanceof CTLGrammarParser.PropositionContext) {
            CTLGrammarParser.PropositionContext context = (CTLGrammarParser.PropositionContext) input;
            if (context.prop().BOOLEAN() != null) {
                if (context.prop().BOOLEAN().getSymbol().getText().equals("True")) {
                    return new Tautology();
                } else {
                    return new Contradiction();
                }
            } else {
                CTLGrammarParser.ExpressionContext expressionContext = context.prop().expression();
                FloatProposition.Operator operator;
                switch (expressionContext.OPERATOR().getText()) {
                    case "==":
                        operator = FloatProposition.Operator.EQ;
                        break;
                    case "!=":
                        operator = FloatProposition.Operator.NEQ;
                        break;
                    case ">":
                        operator = FloatProposition.Operator.GT;
                        break;
                    case "<":
                        operator = FloatProposition.Operator.LT;
                        break;
                    case ">=":
                        operator = FloatProposition.Operator.GT_EQ;
                        break;
                    case "<=":
                        operator = FloatProposition.Operator.LT_EQ;
                        break;
                    default:
                        operator = FloatProposition.Operator.EQ;
                }
                return new FloatProposition(Float.parseFloat(expressionContext.FLOAT().getText()), expressionContext.VARIABLE().getText(), operator);
            }
        } else {
            throw new IllegalStateException("Unsupported operation");
        }
    }

    private static List<Formula> processSubFormulas(List<CTLGrammarParser.FormulaContext> formulaContextList) {
        List<Formula> subFormulas = new ArrayList<>();
        for (CTLGrammarParser.FormulaContext context : formulaContextList) {
            subFormulas.add(processContext(context));
        }
        return subFormulas;
    }

}
