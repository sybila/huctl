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
		BOOLEAN=1, OPERATOR=2, NEG=3, EX=4, AF=5, E=6, U=7, AND=8, B_OPEN=9, B_CLOSE=10, 
		CB_OPEN=11, CB_CLOSE=12, EQ=13, N_EQ=14, GT=15, LT=16, GT_EQ=17, LT_EQ=18, 
		VARIABLE=19, FLOAT=20;
	public static final String[] tokenNames = {
		"<INVALID>", "BOOLEAN", "OPERATOR", "'!'", "'EX'", "'AF'", "'E'", "'U'", 
		"'&&'", "'('", "')'", "'{'", "'}'", "'=='", "'!='", "'>'", "'<'", "'>='", 
		"'<='", "VARIABLE", "FLOAT"
	};
	public static final int
		RULE_formula = 0, RULE_prop = 1, RULE_expression = 2;
	public static final String[] ruleNames = {
		"formula", "prop", "expression"
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
	public static class NegationContext extends FormulaContext {
		public TerminalNode NEG() { return getToken(CTLGrammarParser.NEG, 0); }
		public FormulaContext formula() {
			return getRuleContext(FormulaContext.class,0);
		}
		public NegationContext(FormulaContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterNegation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitNegation(this);
		}
	}
	public static class AndContext extends FormulaContext {
		public List<FormulaContext> formula() {
			return getRuleContexts(FormulaContext.class);
		}
		public TerminalNode B_CLOSE() { return getToken(CTLGrammarParser.B_CLOSE, 0); }
		public TerminalNode AND() { return getToken(CTLGrammarParser.AND, 0); }
		public FormulaContext formula(int i) {
			return getRuleContext(FormulaContext.class,i);
		}
		public TerminalNode B_OPEN() { return getToken(CTLGrammarParser.B_OPEN, 0); }
		public AndContext(FormulaContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterAnd(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitAnd(this);
		}
	}
	public static class ExistsUntilContext extends FormulaContext {
		public List<FormulaContext> formula() {
			return getRuleContexts(FormulaContext.class);
		}
		public TerminalNode B_CLOSE() { return getToken(CTLGrammarParser.B_CLOSE, 0); }
		public TerminalNode E() { return getToken(CTLGrammarParser.E, 0); }
		public TerminalNode U() { return getToken(CTLGrammarParser.U, 0); }
		public FormulaContext formula(int i) {
			return getRuleContext(FormulaContext.class,i);
		}
		public TerminalNode B_OPEN() { return getToken(CTLGrammarParser.B_OPEN, 0); }
		public ExistsUntilContext(FormulaContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterExistsUntil(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitExistsUntil(this);
		}
	}
	public static class PropositionContext extends FormulaContext {
		public TerminalNode CB_OPEN() { return getToken(CTLGrammarParser.CB_OPEN, 0); }
		public TerminalNode CB_CLOSE() { return getToken(CTLGrammarParser.CB_CLOSE, 0); }
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
	public static class ExistsFutureContext extends FormulaContext {
		public FormulaContext formula() {
			return getRuleContext(FormulaContext.class,0);
		}
		public TerminalNode EX() { return getToken(CTLGrammarParser.EX, 0); }
		public ExistsFutureContext(FormulaContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterExistsFuture(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitExistsFuture(this);
		}
	}
	public static class AllFutureContext extends FormulaContext {
		public FormulaContext formula() {
			return getRuleContext(FormulaContext.class,0);
		}
		public TerminalNode AF() { return getToken(CTLGrammarParser.AF, 0); }
		public AllFutureContext(FormulaContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).enterAllFuture(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CTLGrammarListener ) ((CTLGrammarListener)listener).exitAllFuture(this);
		}
	}

	public final FormulaContext formula() throws RecognitionException {
		FormulaContext _localctx = new FormulaContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_formula);
		try {
			setState(29);
			switch (_input.LA(1)) {
			case NEG:
				_localctx = new NegationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(6); match(NEG);
				setState(7); formula();
				}
				break;
			case EX:
				_localctx = new ExistsFutureContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(8); match(EX);
				setState(9); formula();
				}
				break;
			case AF:
				_localctx = new AllFutureContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(10); match(AF);
				setState(11); formula();
				}
				break;
			case B_OPEN:
				_localctx = new AndContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(12); match(B_OPEN);
				setState(13); formula();
				setState(14); match(AND);
				setState(15); formula();
				setState(16); match(B_CLOSE);
				}
				break;
			case E:
				_localctx = new ExistsUntilContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(18); match(E);
				setState(19); match(B_OPEN);
				setState(20); formula();
				setState(21); match(U);
				setState(22); formula();
				setState(23); match(B_CLOSE);
				}
				break;
			case CB_OPEN:
				_localctx = new PropositionContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(25); match(CB_OPEN);
				setState(26); prop();
				setState(27); match(CB_CLOSE);
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

	public static class PropContext extends ParserRuleContext {
		public TerminalNode BOOLEAN() { return getToken(CTLGrammarParser.BOOLEAN, 0); }
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
		enterRule(_localctx, 2, RULE_prop);
		try {
			setState(33);
			switch (_input.LA(1)) {
			case VARIABLE:
				enterOuterAlt(_localctx, 1);
				{
				setState(31); expression();
				}
				break;
			case BOOLEAN:
				enterOuterAlt(_localctx, 2);
				{
				setState(32); match(BOOLEAN);
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

	public static class ExpressionContext extends ParserRuleContext {
		public TerminalNode VARIABLE() { return getToken(CTLGrammarParser.VARIABLE, 0); }
		public TerminalNode OPERATOR() { return getToken(CTLGrammarParser.OPERATOR, 0); }
		public TerminalNode FLOAT() { return getToken(CTLGrammarParser.FLOAT, 0); }
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
		enterRule(_localctx, 4, RULE_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(35); match(VARIABLE);
			setState(36); match(OPERATOR);
			setState(37); match(FLOAT);
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
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\3\26*\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2 \n\2\3\3\3\3\5\3$\n\3\3\4\3\4\3\4"+
		"\3\4\3\4\2\5\2\4\6\2\2,\2\37\3\2\2\2\4#\3\2\2\2\6%\3\2\2\2\b\t\7\5\2\2"+
		"\t \5\2\2\2\n\13\7\6\2\2\13 \5\2\2\2\f\r\7\7\2\2\r \5\2\2\2\16\17\7\13"+
		"\2\2\17\20\5\2\2\2\20\21\7\n\2\2\21\22\5\2\2\2\22\23\7\f\2\2\23 \3\2\2"+
		"\2\24\25\7\b\2\2\25\26\7\13\2\2\26\27\5\2\2\2\27\30\7\t\2\2\30\31\5\2"+
		"\2\2\31\32\7\f\2\2\32 \3\2\2\2\33\34\7\r\2\2\34\35\5\4\3\2\35\36\7\16"+
		"\2\2\36 \3\2\2\2\37\b\3\2\2\2\37\n\3\2\2\2\37\f\3\2\2\2\37\16\3\2\2\2"+
		"\37\24\3\2\2\2\37\33\3\2\2\2 \3\3\2\2\2!$\5\6\4\2\"$\7\3\2\2#!\3\2\2\2"+
		"#\"\3\2\2\2$\5\3\2\2\2%&\7\25\2\2&\'\7\4\2\2\'(\7\26\2\2(\7\3\2\2\2\4"+
		"\37#";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}