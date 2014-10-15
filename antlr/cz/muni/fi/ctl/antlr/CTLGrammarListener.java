// Generated from /Users/daemontus/Synced/Java Development/Parametrized CTL Model Checker/CTL Parser/grammar/CTLGrammar.g4 by ANTLR 4.1
package cz.muni.fi.ctl.antlr;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CTLGrammarParser}.
 */
public interface CTLGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(@NotNull CTLGrammarParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(@NotNull CTLGrammarParser.ExpressionContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#Negation}.
	 * @param ctx the parse tree
	 */
	void enterNegation(@NotNull CTLGrammarParser.NegationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#Negation}.
	 * @param ctx the parse tree
	 */
	void exitNegation(@NotNull CTLGrammarParser.NegationContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#And}.
	 * @param ctx the parse tree
	 */
	void enterAnd(@NotNull CTLGrammarParser.AndContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#And}.
	 * @param ctx the parse tree
	 */
	void exitAnd(@NotNull CTLGrammarParser.AndContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#prop}.
	 * @param ctx the parse tree
	 */
	void enterProp(@NotNull CTLGrammarParser.PropContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#prop}.
	 * @param ctx the parse tree
	 */
	void exitProp(@NotNull CTLGrammarParser.PropContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#ExistsUntil}.
	 * @param ctx the parse tree
	 */
	void enterExistsUntil(@NotNull CTLGrammarParser.ExistsUntilContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#ExistsUntil}.
	 * @param ctx the parse tree
	 */
	void exitExistsUntil(@NotNull CTLGrammarParser.ExistsUntilContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#Proposition}.
	 * @param ctx the parse tree
	 */
	void enterProposition(@NotNull CTLGrammarParser.PropositionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#Proposition}.
	 * @param ctx the parse tree
	 */
	void exitProposition(@NotNull CTLGrammarParser.PropositionContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#ExistsFuture}.
	 * @param ctx the parse tree
	 */
	void enterExistsFuture(@NotNull CTLGrammarParser.ExistsFutureContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#ExistsFuture}.
	 * @param ctx the parse tree
	 */
	void exitExistsFuture(@NotNull CTLGrammarParser.ExistsFutureContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#AllFuture}.
	 * @param ctx the parse tree
	 */
	void enterAllFuture(@NotNull CTLGrammarParser.AllFutureContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#AllFuture}.
	 * @param ctx the parse tree
	 */
	void exitAllFuture(@NotNull CTLGrammarParser.AllFutureContext ctx);
}