package cz.muni.fi.ctl.parser;

import cz.muni.fi.ctl.formula.*;
import cz.muni.fi.ctl.formula.operator.BinaryOperator;
import cz.muni.fi.ctl.formula.operator.Operator;
import cz.muni.fi.ctl.formula.operator.UnaryOperator;
import cz.muni.fi.ctl.formula.proposition.Contradiction;
import cz.muni.fi.ctl.formula.proposition.Tautology;
import cz.muni.fi.ctl.grammar.CTLGrammarLexer;
import cz.muni.fi.ctl.grammar.CTLGrammarParser;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.TokenStream;

import java.util.*;
import java.util.regex.Matcher;

public class CTLParser {

    public static Formula parse(String input) throws IllegalStateException {
        input = input.replace(" ", "");
        TokenStream tokenStream = new BufferedTokenStream(new CTLGrammarLexer(new ANTLRInputStream(input.toCharArray(), input.length())));
        CTLGrammarParser parser = new CTLGrammarParser(tokenStream);
        CTLGrammarParser.FormulaContext context = parser.formula();
        return processContext(context);
    }

    private static Formula processContext(CTLGrammarParser.FormulaContext input) {
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
            if (context.BOOLEAN().getSymbol().getText().equals("True")) {
                return new Tautology();
            } else {
                return new Contradiction();
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
