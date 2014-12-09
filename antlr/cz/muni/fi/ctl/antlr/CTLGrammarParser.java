// Generated from /Users/daemontus/Synced/Java Development/Parametrized CTL Model Checker/CTL Parser/grammar/CTLGrammar.g4 by ANTLR 4.1
package cz.muni.fi.ctl.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CTLGrammarParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TRUE=1, FALSE=2, NEG=3, AND=4, OR=5, IMPL=6, EQIV=7, ALL=8, EXISTS=9, 
		NEXT=10, FUTURE=11, GLOBAL=12, UNTIL=13, EQ=14, N_EQ=15, GT=16, LT=17, 
		GT_EQ=18, LT_EQ=19, B_OPEN=20, B_CLOSE=21, CB_OPEN=22, CB_CLOSE=23, SB_OPEN=24, 
		SB_CLOSE=25, VAR_NAME=26, FLOAT_VAL=27, WS=28;
	public static final String[] tokenNames = {
		"<INVALID>", "'True'", "'False'", "'!'", "'&&'", "'||'", "'=>'", "'<=>'", 
		"'A'", "'E'", "'X'", "'F'", "'G'", "'U'", "'=='", "'!='", "'>'", "'<'", 
		"'>='", "'<='", "'('", "')'", "'{'", "'}'", "'['", "']'", "VAR_NAME", 
		"FLOAT_VAL", "WS"
	};
	public static final int
		RULE_formula = 0, RULE_op_unary = 1, RULE_op_binary_temp = 2, RULE_op_binary_bool = 3, 
		RULE_prop = 4, RULE_expression = 5, RULE_comparison = 6, RULE_bool = 7, 
		RULE_op_bool = 8, RULE_quantifier = 9, RULE_path = 10, RULE_op_float = 11;
	public static final String[] ruleNames = {
		"formula", "op_unary", "op_binary_temp", "op_binary_bool", "prop", "expression", 
		"comparison", "bool", "op_bool", "quantifier", "path", "op_float"
	};

	@Override
	public String getGrammarFileName() { return "CTLGrammar.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public CTLGrammarParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class FormulaContext extends ParserRuleContext {
		public FormulaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formula; }
	 
		public FormulaContext() { }
		public void copyFrom(FormulaContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BinBoolContext extends FormulaContext {
		public Op_binary_boolContext op_binary_bool() {
			return getRuleContext(Op_binary_boolContext.class,0);
		}
		public BinBoolContext(FormulaContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterBinBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitBinBool(this);
		}
	}
	public static class BinTempContext extends FormulaContext {
		public Op_binary_tempContext op_binary_temp() {
			return getRuleContext(Op_binary_tempContext.class,0);
		}
		public BinTempContext(FormulaContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterBinTemp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitBinTemp(this);
		}
	}
	public static class PropositionContext extends FormulaContext {
		public PropContext prop() {
			return getRuleContext(PropContext.class,0);
		}
		public PropositionContext(FormulaContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterProposition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitProposition(this);
		}
	}
	public static class UnaryContext extends FormulaContext {
		public Op_unaryContext op_unary() {
			return getRuleContext(Op_unaryContext.class,0);
		}
		public UnaryContext(FormulaContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterUnary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitUnary(this);
		}
	}

	public final FormulaContext formula() throws RecognitionException {
		FormulaContext _localctx = new FormulaContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_formula);
		try {
			setState(28);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				_localctx = new UnaryContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(24); op_unary();
				}
				break;

			case 2:
				_localctx = new BinTempContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(25); op_binary_temp();
				}
				break;

			case 3:
				_localctx = new BinBoolContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(26); op_binary_bool();
				}
				break;

			case 4:
				_localctx = new PropositionContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(27); prop();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Op_unaryContext extends ParserRuleContext {
		public Op_unaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op_unary; }
	 
		public Op_unaryContext() { }
		public void copyFrom(Op_unaryContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class UnBoolContext extends Op_unaryContext {
		public TerminalNode NEG() { return getToken(CTLGrammarParser.NEG, 0); }
		public FormulaContext formula() {
			return getRuleContext(FormulaContext.class,0);
		}
		public UnBoolContext(Op_unaryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterUnBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitUnBool(this);
		}
	}
	public static class UnTempContext extends Op_unaryContext {
		public FormulaContext formula() {
			return getRuleContext(FormulaContext.class,0);
		}
		public QuantifierContext quantifier() {
			return getRuleContext(QuantifierContext.class,0);
		}
		public PathContext path() {
			return getRuleContext(PathContext.class,0);
		}
		public UnTempContext(Op_unaryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterUnTemp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitUnTemp(this);
		}
	}

	public final Op_unaryContext op_unary() throws RecognitionException {
		Op_unaryContext _localctx = new Op_unaryContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_op_unary);
		try {
			setState(36);
			switch (_input.LA(1)) {
			case NEG:
				_localctx = new UnBoolContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(30); match(NEG);
				setState(31); formula();
				}
				break;
			case ALL:
			case EXISTS:
				_localctx = new UnTempContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(32); quantifier();
				setState(33); path();
				setState(34); formula();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Op_binary_tempContext extends ParserRuleContext {
		public List<FormulaContext> formula() {
			return getRuleContexts(FormulaContext.class);
		}
		public TerminalNode SB_OPEN() { return getToken(CTLGrammarParser.SB_OPEN, 0); }
		public QuantifierContext quantifier() {
			return getRuleContext(QuantifierContext.class,0);
		}
		public TerminalNode UNTIL() { return getToken(CTLGrammarParser.UNTIL, 0); }
		public FormulaContext formula(int i) {
			return getRuleContext(FormulaContext.class,i);
		}
		public TerminalNode SB_CLOSE() { return getToken(CTLGrammarParser.SB_CLOSE, 0); }
		public Op_binary_tempContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op_binary_temp; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterOp_binary_temp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitOp_binary_temp(this);
		}
	}

	public final Op_binary_tempContext op_binary_temp() throws RecognitionException {
		Op_binary_tempContext _localctx = new Op_binary_tempContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_op_binary_temp);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38); quantifier();
			setState(39); match(SB_OPEN);
			setState(40); formula();
			setState(41); match(UNTIL);
			setState(42); formula();
			setState(43); match(SB_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Op_binary_boolContext extends ParserRuleContext {
		public List<FormulaContext> formula() {
			return getRuleContexts(FormulaContext.class);
		}
		public TerminalNode B_CLOSE() { return getToken(CTLGrammarParser.B_CLOSE, 0); }
		public Op_boolContext op_bool() {
			return getRuleContext(Op_boolContext.class,0);
		}
		public FormulaContext formula(int i) {
			return getRuleContext(FormulaContext.class,i);
		}
		public TerminalNode B_OPEN() { return getToken(CTLGrammarParser.B_OPEN, 0); }
		public Op_binary_boolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op_binary_bool; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterOp_binary_bool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitOp_binary_bool(this);
		}
	}

	public final Op_binary_boolContext op_binary_bool() throws RecognitionException {
		Op_binary_boolContext _localctx = new Op_binary_boolContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_op_binary_bool);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(45); match(B_OPEN);
			setState(46); formula();
			setState(47); op_bool();
			setState(48); formula();
			setState(49); match(B_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PropContext extends ParserRuleContext {
		public TerminalNode CB_OPEN() { return getToken(CTLGrammarParser.CB_OPEN, 0); }
		public TerminalNode CB_CLOSE() { return getToken(CTLGrammarParser.CB_CLOSE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public PropContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prop; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterProp(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitProp(this);
		}
	}

	public final PropContext prop() throws RecognitionException {
		PropContext _localctx = new PropContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_prop);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(51); match(CB_OPEN);
			setState(52); expression();
			setState(53); match(CB_CLOSE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ComparisonContext comparison() {
			return getRuleContext(ComparisonContext.class,0);
		}
		public BoolContext bool() {
			return getRuleContext(BoolContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitExpression(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_expression);
		try {
			setState(57);
			switch (_input.LA(1)) {
			case VAR_NAME:
				enterOuterAlt(_localctx, 1);
				{
				setState(55); comparison();
				}
				break;
			case TRUE:
			case FALSE:
				enterOuterAlt(_localctx, 2);
				{
				setState(56); bool();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ComparisonContext extends ParserRuleContext {
		public Op_floatContext op_float() {
			return getRuleContext(Op_floatContext.class,0);
		}
		public TerminalNode FLOAT_VAL() { return getToken(CTLGrammarParser.FLOAT_VAL, 0); }
		public TerminalNode VAR_NAME() { return getToken(CTLGrammarParser.VAR_NAME, 0); }
		public ComparisonContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_comparison; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterComparison(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitComparison(this);
		}
	}

	public final ComparisonContext comparison() throws RecognitionException {
		ComparisonContext _localctx = new ComparisonContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_comparison);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59); match(VAR_NAME);
			setState(60); op_float();
			setState(61); match(FLOAT_VAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BoolContext extends ParserRuleContext {
		public TerminalNode FALSE() { return getToken(CTLGrammarParser.FALSE, 0); }
		public TerminalNode TRUE() { return getToken(CTLGrammarParser.TRUE, 0); }
		public BoolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bool; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterBool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitBool(this);
		}
	}

	public final BoolContext bool() throws RecognitionException {
		BoolContext _localctx = new BoolContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_bool);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
			_la = _input.LA(1);
			if ( !(_la==TRUE || _la==FALSE) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Op_boolContext extends ParserRuleContext {
		public TerminalNode IMPL() { return getToken(CTLGrammarParser.IMPL, 0); }
		public TerminalNode EQIV() { return getToken(CTLGrammarParser.EQIV, 0); }
		public TerminalNode AND() { return getToken(CTLGrammarParser.AND, 0); }
		public TerminalNode OR() { return getToken(CTLGrammarParser.OR, 0); }
		public Op_boolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op_bool; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterOp_bool(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitOp_bool(this);
		}
	}

	public final Op_boolContext op_bool() throws RecognitionException {
		Op_boolContext _localctx = new Op_boolContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_op_bool);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << AND) | (1L << OR) | (1L << IMPL) | (1L << EQIV))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QuantifierContext extends ParserRuleContext {
		public TerminalNode EXISTS() { return getToken(CTLGrammarParser.EXISTS, 0); }
		public TerminalNode ALL() { return getToken(CTLGrammarParser.ALL, 0); }
		public QuantifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_quantifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterQuantifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitQuantifier(this);
		}
	}

	public final QuantifierContext quantifier() throws RecognitionException {
		QuantifierContext _localctx = new QuantifierContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_quantifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67);
			_la = _input.LA(1);
			if ( !(_la==ALL || _la==EXISTS) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PathContext extends ParserRuleContext {
		public TerminalNode NEXT() { return getToken(CTLGrammarParser.NEXT, 0); }
		public TerminalNode FUTURE() { return getToken(CTLGrammarParser.FUTURE, 0); }
		public TerminalNode GLOBAL() { return getToken(CTLGrammarParser.GLOBAL, 0); }
		public PathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_path; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitPath(this);
		}
	}

	public final PathContext path() throws RecognitionException {
		PathContext _localctx = new PathContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_path);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(69);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << NEXT) | (1L << FUTURE) | (1L << GLOBAL))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Op_floatContext extends ParserRuleContext {
		public TerminalNode LT_EQ() { return getToken(CTLGrammarParser.LT_EQ, 0); }
		public TerminalNode LT() { return getToken(CTLGrammarParser.LT, 0); }
		public TerminalNode GT() { return getToken(CTLGrammarParser.GT, 0); }
		public TerminalNode N_EQ() { return getToken(CTLGrammarParser.N_EQ, 0); }
		public TerminalNode EQ() { return getToken(CTLGrammarParser.EQ, 0); }
		public TerminalNode GT_EQ() { return getToken(CTLGrammarParser.GT_EQ, 0); }
		public Op_floatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op_float; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterOp_float(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitOp_float(this);
		}
	}

	public final Op_floatContext op_float() throws RecognitionException {
		Op_floatContext _localctx = new Op_floatContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_op_float);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(71);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << EQ) | (1L << N_EQ) | (1L << GT) | (1L << LT) | (1L << GT_EQ) | (1L << LT_EQ))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\3\36L\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\4\r\t\r\3\2\3\2\3\2\3\2\5\2\37\n\2\3\3\3\3\3\3\3\3\3\3\3\3\5\3"+
		"\'\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6"+
		"\3\6\3\7\3\7\5\7<\n\7\3\b\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3"+
		"\f\3\r\3\r\3\r\2\16\2\4\6\b\n\f\16\20\22\24\26\30\2\7\3\2\3\4\3\2\6\t"+
		"\3\2\n\13\3\2\f\16\3\2\20\25D\2\36\3\2\2\2\4&\3\2\2\2\6(\3\2\2\2\b/\3"+
		"\2\2\2\n\65\3\2\2\2\f;\3\2\2\2\16=\3\2\2\2\20A\3\2\2\2\22C\3\2\2\2\24"+
		"E\3\2\2\2\26G\3\2\2\2\30I\3\2\2\2\32\37\5\4\3\2\33\37\5\6\4\2\34\37\5"+
		"\b\5\2\35\37\5\n\6\2\36\32\3\2\2\2\36\33\3\2\2\2\36\34\3\2\2\2\36\35\3"+
		"\2\2\2\37\3\3\2\2\2 !\7\5\2\2!\'\5\2\2\2\"#\5\24\13\2#$\5\26\f\2$%\5\2"+
		"\2\2%\'\3\2\2\2& \3\2\2\2&\"\3\2\2\2\'\5\3\2\2\2()\5\24\13\2)*\7\32\2"+
		"\2*+\5\2\2\2+,\7\17\2\2,-\5\2\2\2-.\7\33\2\2.\7\3\2\2\2/\60\7\26\2\2\60"+
		"\61\5\2\2\2\61\62\5\22\n\2\62\63\5\2\2\2\63\64\7\27\2\2\64\t\3\2\2\2\65"+
		"\66\7\30\2\2\66\67\5\f\7\2\678\7\31\2\28\13\3\2\2\29<\5\16\b\2:<\5\20"+
		"\t\2;9\3\2\2\2;:\3\2\2\2<\r\3\2\2\2=>\7\34\2\2>?\5\30\r\2?@\7\35\2\2@"+
		"\17\3\2\2\2AB\t\2\2\2B\21\3\2\2\2CD\t\3\2\2D\23\3\2\2\2EF\t\4\2\2F\25"+
		"\3\2\2\2GH\t\5\2\2H\27\3\2\2\2IJ\t\6\2\2J\31\3\2\2\2\5\36&;";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}