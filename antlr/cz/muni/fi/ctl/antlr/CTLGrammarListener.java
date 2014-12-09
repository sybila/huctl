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
	 * Enter a parse tree produced by {@link CTLGrammarParser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterComparison(@NotNull CTLGrammarParser.ComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitComparison(@NotNull CTLGrammarParser.ComparisonContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#BinBool}.
	 * @param ctx the parse tree
	 */
	void enterBinBool(@NotNull CTLGrammarParser.BinBoolContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#BinBool}.
	 * @param ctx the parse tree
	 */
	void exitBinBool(@NotNull CTLGrammarParser.BinBoolContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#bool}.
	 * @param ctx the parse tree
	 */
	void enterBool(@NotNull CTLGrammarParser.BoolContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#bool}.
	 * @param ctx the parse tree
	 */
	void exitBool(@NotNull CTLGrammarParser.BoolContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#BinTemp}.
	 * @param ctx the parse tree
	 */
	void enterBinTemp(@NotNull CTLGrammarParser.BinTempContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#BinTemp}.
	 * @param ctx the parse tree
	 */
	void exitBinTemp(@NotNull CTLGrammarParser.BinTempContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#UnTemp}.
	 * @param ctx the parse tree
	 */
	void enterUnTemp(@NotNull CTLGrammarParser.UnTempContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#UnTemp}.
	 * @param ctx the parse tree
	 */
	void exitUnTemp(@NotNull CTLGrammarParser.UnTempContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#quantifier}.
	 * @param ctx the parse tree
	 */
	void enterQuantifier(@NotNull CTLGrammarParser.QuantifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#quantifier}.
	 * @param ctx the parse tree
	 */
	void exitQuantifier(@NotNull CTLGrammarParser.QuantifierContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#Unary}.
	 * @param ctx the parse tree
	 */
	void enterUnary(@NotNull CTLGrammarParser.UnaryContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#Unary}.
	 * @param ctx the parse tree
	 */
	void exitUnary(@NotNull CTLGrammarParser.UnaryContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#path}.
	 * @param ctx the parse tree
	 */
	void enterPath(@NotNull CTLGrammarParser.PathContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#path}.
	 * @param ctx the parse tree
	 */
	void exitPath(@NotNull CTLGrammarParser.PathContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#op_binary_bool}.
	 * @param ctx the parse tree
	 */
	void enterOp_binary_bool(@NotNull CTLGrammarParser.Op_binary_boolContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#op_binary_bool}.
	 * @param ctx the parse tree
	 */
	void exitOp_binary_bool(@NotNull CTLGrammarParser.Op_binary_boolContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#op_bool}.
	 * @param ctx the parse tree
	 */
	void enterOp_bool(@NotNull CTLGrammarParser.Op_boolContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#op_bool}.
	 * @param ctx the parse tree
	 */
	void exitOp_bool(@NotNull CTLGrammarParser.Op_boolContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#op_binary_temp}.
	 * @param ctx the parse tree
	 */
	void enterOp_binary_temp(@NotNull CTLGrammarParser.Op_binary_tempContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#op_binary_temp}.
	 * @param ctx the parse tree
	 */
	void exitOp_binary_temp(@NotNull CTLGrammarParser.Op_binary_tempContext ctx);

	/**
	 * Enter a parse tree produced by {@link CTLGrammarParser#op_float}.
	 * @param ctx the parse tree
	 */
	void enterOp_float(@NotNull CTLGrammarParser.Op_floatContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#op_float}.
	 * @param ctx the parse tree
	 */
	void exitOp_float(@NotNull CTLGrammarParser.Op_floatContext ctx);

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
	 * Enter a parse tree produced by {@link CTLGrammarParser#UnBool}.
	 * @param ctx the parse tree
	 */
	void enterUnBool(@NotNull CTLGrammarParser.UnBoolContext ctx);
	/**
	 * Exit a parse tree produced by {@link CTLGrammarParser#UnBool}.
	 * @param ctx the parse tree
	 */
	void exitUnBool(@NotNull CTLGrammarParser.UnBoolContext ctx);

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
}