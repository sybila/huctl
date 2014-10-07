// Generated from /Users/daemontus/CTLGrammar.g4 by ANTLR 4.1
package cz.muni.fi.ctl.grammar;
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
		BOOLEAN=1, NEG=2, EX=3, AF=4, E=5, U=6, AND=7, B_OPEN=8, B_CLOSE=9, CB_OPEN=10, 
		CB_CLOSE=11;
	public static final String[] tokenNames = {
		"<INVALID>", "BOOLEAN", "'!'", "'EX'", "'AF'", "'E'", "'U'", "'&&'", "'('", 
		"')'", "'{'", "'}'"
	};
	public static final int
		RULE_formula = 0;
	public static final String[] ruleNames = {
		"formula"
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
	}
	public static class PropositionContext extends FormulaContext {
		public TerminalNode CB_OPEN() { return getToken(CTLGrammarParser.CB_OPEN, 0); }
		public TerminalNode CB_CLOSE() { return getToken(CTLGrammarParser.CB_CLOSE, 0); }
		public TerminalNode BOOLEAN() { return getToken(CTLGrammarParser.BOOLEAN, 0); }
		public PropositionContext(FormulaContext ctx) { copyFrom(ctx); }
	}
	public static class ExistsFutureContext extends FormulaContext {
		public FormulaContext formula() {
			return getRuleContext(FormulaContext.class,0);
		}
		public TerminalNode EX() { return getToken(CTLGrammarParser.EX, 0); }
		public ExistsFutureContext(FormulaContext ctx) { copyFrom(ctx); }
	}
	public static class AllFutureContext extends FormulaContext {
		public FormulaContext formula() {
			return getRuleContext(FormulaContext.class,0);
		}
		public TerminalNode AF() { return getToken(CTLGrammarParser.AF, 0); }
		public AllFutureContext(FormulaContext ctx) { copyFrom(ctx); }
	}

	public final FormulaContext formula() throws RecognitionException {
		FormulaContext _localctx = new FormulaContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_formula);
		try {
			setState(24);
			switch (_input.LA(1)) {
			case NEG:
				_localctx = new NegationContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(2); match(NEG);
				setState(3); formula();
				}
				break;
			case EX:
				_localctx = new ExistsFutureContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(4); match(EX);
				setState(5); formula();
				}
				break;
			case AF:
				_localctx = new AllFutureContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(6); match(AF);
				setState(7); formula();
				}
				break;
			case B_OPEN:
				_localctx = new AndContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(8); match(B_OPEN);
				setState(9); formula();
				setState(10); match(AND);
				setState(11); formula();
				setState(12); match(B_CLOSE);
				}
				break;
			case E:
				_localctx = new ExistsUntilContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(14); match(E);
				setState(15); match(B_OPEN);
				setState(16); formula();
				setState(17); match(U);
				setState(18); formula();
				setState(19); match(B_CLOSE);
				}
				break;
			case CB_OPEN:
				_localctx = new PropositionContext(_localctx);
				enterOuterAlt(_localctx, 6);
				{
				setState(21); match(CB_OPEN);
				setState(22); match(BOOLEAN);
				setState(23); match(CB_CLOSE);
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

	public static final String _serializedATN =
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\3\r\35\4\2\t\2\3\2"+
		"\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\5\2\33\n\2\3\2\2\3\2\2\2 \2\32\3\2\2\2\4\5\7\4\2\2\5\33"+
		"\5\2\2\2\6\7\7\5\2\2\7\33\5\2\2\2\b\t\7\6\2\2\t\33\5\2\2\2\n\13\7\n\2"+
		"\2\13\f\5\2\2\2\f\r\7\t\2\2\r\16\5\2\2\2\16\17\7\13\2\2\17\33\3\2\2\2"+
		"\20\21\7\7\2\2\21\22\7\n\2\2\22\23\5\2\2\2\23\24\7\b\2\2\24\25\5\2\2\2"+
		"\25\26\7\13\2\2\26\33\3\2\2\2\27\30\7\f\2\2\30\31\7\3\2\2\31\33\7\r\2"+
		"\2\32\4\3\2\2\2\32\6\3\2\2\2\32\b\3\2\2\2\32\n\3\2\2\2\32\20\3\2\2\2\32"+
		"\27\3\2\2\2\33\3\3\2\2\2\3\32";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}