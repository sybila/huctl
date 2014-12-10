// Generated from /Users/daemontus/Synced/Java Development/Colored Model Checker/CTL Parser/src/main/antlr4/CTL.g4 by ANTLR 4.2.2
package cz.muni.fi.ctl.antlr;
 
import cz.muni.fi.ctl.formula.Formula;
import cz.muni.fi.ctl.formula.FormulaImpl;
import cz.muni.fi.ctl.formula.operator.Operator;
import cz.muni.fi.ctl.formula.operator.BinaryOperator;
import cz.muni.fi.ctl.formula.operator.UnaryOperator;
import cz.muni.fi.ctl.formula.proposition.Contradiction;
import cz.muni.fi.ctl.formula.proposition.FloatProposition;
import cz.muni.fi.ctl.formula.proposition.Proposition;
import cz.muni.fi.ctl.formula.proposition.Tautology;

import java.util.HashMap; 
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CTLParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__5=1, T__4=2, T__3=3, T__2=4, T__1=5, T__0=6, DEF=7, PROP=8, NEWLINE=9, 
		TRUE=10, FALSE=11, NEG=12, AND=13, OR=14, IMPL=15, EQIV=16, ALL=17, EXISTS=18, 
		UNTIL=19, EQ=20, N_EQ=21, GT=22, LT=23, GT_EQ=24, LT_EQ=25, B_OPEN=26, 
		B_CLOSE=27, VAR_NAME=28, FLOAT_VAL=29, WS=30;
	public static final String[] tokenNames = {
		"<INVALID>", "'EF'", "'AF'", "'AG'", "'EG'", "'AX'", "'EX'", "'#define'", 
		"'#property'", "NEWLINE", "'True'", "'False'", "'!'", "'&&'", "'||'", 
		"'=>'", "'<=>'", "'A'", "'E'", "'U'", "'=='", "'!='", "'>'", "'<'", "'>='", 
		"'<='", "'('", "')'", "VAR_NAME", "FLOAT_VAL", "WS"
	};
	public static final int
		RULE_root = 0, RULE_ctl = 1, RULE_define = 2, RULE_property = 3, RULE_formula = 4, 
		RULE_temporal = 5, RULE_op_bool = 6, RULE_op_float = 7, RULE_bool = 8;
	public static final String[] ruleNames = {
		"root", "ctl", "define", "property", "formula", "temporal", "op_bool", 
		"op_float", "bool"
	};

	@Override
	public String getGrammarFileName() { return "CTL.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CTLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class RootContext extends ParserRuleContext {
		public Map<String, Proposition> propositions = new HashMap<>();
		public Formula result;
		public CtlContext ctl() {
			return getRuleContext(CtlContext.class,0);
		}
		public RootContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_root; }
	}

	public final RootContext root() throws RecognitionException {
		RootContext _localctx = new RootContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_root);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18); ctl();
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

	public static class CtlContext extends ParserRuleContext {
		public DefineContext define() {
			return getRuleContext(DefineContext.class,0);
		}
		public CtlContext ctl() {
			return getRuleContext(CtlContext.class,0);
		}
		public PropertyContext property() {
			return getRuleContext(PropertyContext.class,0);
		}
		public CtlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ctl; }
	}

	public final CtlContext ctl() throws RecognitionException {
		CtlContext _localctx = new CtlContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_ctl);
		try {
			setState(24);
			switch (_input.LA(1)) {
			case DEF:
				enterOuterAlt(_localctx, 1);
				{
				setState(20); define();
				setState(21); ctl();
				}
				break;
			case PROP:
				enterOuterAlt(_localctx, 2);
				{
				setState(23); property();
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

	public static class DefineContext extends ParserRuleContext {
		public Token VAR_NAME;
		public Op_floatContext op_float;
		public Token FLOAT_VAL;
		public TerminalNode DEF() { return getToken(CTLParser.DEF, 0); }
		public Op_floatContext op_float() {
			return getRuleContext(Op_floatContext.class,0);
		}
		public TerminalNode FLOAT_VAL() { return getToken(CTLParser.FLOAT_VAL, 0); }
		public List<TerminalNode> VAR_NAME() { return getTokens(CTLParser.VAR_NAME); }
		public TerminalNode VAR_NAME(int i) {
			return getToken(CTLParser.VAR_NAME, i);
		}
		public DefineContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_define; }
	}

	public final DefineContext define() throws RecognitionException {
		DefineContext _localctx = new DefineContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_define);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26); match(DEF);
			setState(27); ((DefineContext)_localctx).VAR_NAME = match(VAR_NAME);
			 String name = (((DefineContext)_localctx).VAR_NAME!=null?((DefineContext)_localctx).VAR_NAME.getText():null); 
			setState(29); ((DefineContext)_localctx).VAR_NAME = match(VAR_NAME);
			 String prop = (((DefineContext)_localctx).VAR_NAME!=null?((DefineContext)_localctx).VAR_NAME.getText():null); 
			setState(31); ((DefineContext)_localctx).op_float = op_float();
			setState(32); ((DefineContext)_localctx).FLOAT_VAL = match(FLOAT_VAL);
			 ((RootContext)getInvokingContext(0)).propositions.put( name,
			        new FloatProposition(
			            Float.parseFloat((((DefineContext)_localctx).FLOAT_VAL!=null?((DefineContext)_localctx).FLOAT_VAL.getText():null)),
			            prop,
			            ((DefineContext)_localctx).op_float.op
			            )
			      ); 
			    
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

	public static class PropertyContext extends ParserRuleContext {
		public FormulaContext formula() {
			return getRuleContext(FormulaContext.class,0);
		}
		public TerminalNode PROP() { return getToken(CTLParser.PROP, 0); }
		public PropertyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_property; }
	}

	public final PropertyContext property() throws RecognitionException {
		PropertyContext _localctx = new PropertyContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_property);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(35); match(PROP);
			setState(36); formula();
			 ((RootContext)getInvokingContext(0)).result =  ((PropertyContext)getInvokingContext(3)).formula().processed; 
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

	public static class FormulaContext extends ParserRuleContext {
		public Formula processed;
		public Token VAR_NAME;
		public FormulaContext formula;
		public Op_boolContext op_bool;
		public TemporalContext temporal;
		public List<FormulaContext> formula() {
			return getRuleContexts(FormulaContext.class);
		}
		public TerminalNode ALL() { return getToken(CTLParser.ALL, 0); }
		public TerminalNode EXISTS() { return getToken(CTLParser.EXISTS, 0); }
		public TerminalNode TRUE() { return getToken(CTLParser.TRUE, 0); }
		public TemporalContext temporal() {
			return getRuleContext(TemporalContext.class,0);
		}
		public FormulaContext formula(int i) {
			return getRuleContext(FormulaContext.class,i);
		}
		public TerminalNode B_CLOSE() { return getToken(CTLParser.B_CLOSE, 0); }
		public TerminalNode VAR_NAME() { return getToken(CTLParser.VAR_NAME, 0); }
		public Op_boolContext op_bool() {
			return getRuleContext(Op_boolContext.class,0);
		}
		public TerminalNode UNTIL() { return getToken(CTLParser.UNTIL, 0); }
		public TerminalNode NEG() { return getToken(CTLParser.NEG, 0); }
		public TerminalNode FALSE() { return getToken(CTLParser.FALSE, 0); }
		public TerminalNode B_OPEN() { return getToken(CTLParser.B_OPEN, 0); }
		public FormulaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formula; }
	}

	public final FormulaContext formula() throws RecognitionException {
		FormulaContext _localctx = new FormulaContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_formula);
		try {
			setState(76);
			switch (_input.LA(1)) {
			case TRUE:
				enterOuterAlt(_localctx, 1);
				{
				setState(39); match(TRUE);
				 ((FormulaContext)_localctx).processed =  Tautology.INSTANCE; 
				}
				break;
			case FALSE:
				enterOuterAlt(_localctx, 2);
				{
				setState(41); match(FALSE);
				 ((FormulaContext)_localctx).processed =  Contradiction.INSTANCE; 
				}
				break;
			case VAR_NAME:
				enterOuterAlt(_localctx, 3);
				{
				setState(43); ((FormulaContext)_localctx).VAR_NAME = match(VAR_NAME);
				 ((FormulaContext)_localctx).processed =  ((RootContext)getInvokingContext(0)).propositions.get((((FormulaContext)_localctx).VAR_NAME!=null?((FormulaContext)_localctx).VAR_NAME.getText():null));
				            if (_localctx.processed == null) throw new IllegalArgumentException("Unknown proposition: "+(((FormulaContext)_localctx).VAR_NAME!=null?((FormulaContext)_localctx).VAR_NAME.getText():null));
				          
				}
				break;
			case NEG:
				enterOuterAlt(_localctx, 4);
				{
				setState(45); match(NEG);
				setState(46); ((FormulaContext)_localctx).formula = formula();
				 ((FormulaContext)_localctx).processed =  new FormulaImpl(UnaryOperator.NEGATION, _localctx.formula.processed ); 
				}
				break;
			case B_OPEN:
				enterOuterAlt(_localctx, 5);
				{
				setState(49); match(B_OPEN);
				setState(50); ((FormulaContext)_localctx).formula = formula();
				setState(51); ((FormulaContext)_localctx).op_bool = op_bool();
				setState(52); ((FormulaContext)_localctx).formula = formula();
				setState(53); match(B_CLOSE);
				 ((FormulaContext)_localctx).processed =  new FormulaImpl(((FormulaContext)_localctx).op_bool.op, _localctx.formula(0).processed, _localctx.formula(1).processed ); 
				}
				break;
			case EXISTS:
				enterOuterAlt(_localctx, 6);
				{
				setState(56); match(EXISTS);
				setState(57); match(B_OPEN);
				setState(58); ((FormulaContext)_localctx).formula = formula();
				setState(59); match(UNTIL);
				setState(60); ((FormulaContext)_localctx).formula = formula();
				setState(61); match(B_CLOSE);
				 ((FormulaContext)_localctx).processed =  new FormulaImpl(BinaryOperator.EXISTS_UNTIL, _localctx.formula(0).processed, _localctx.formula(1).processed ); 
				}
				break;
			case ALL:
				enterOuterAlt(_localctx, 7);
				{
				setState(64); match(ALL);
				setState(65); match(B_OPEN);
				setState(66); ((FormulaContext)_localctx).formula = formula();
				setState(67); match(UNTIL);
				setState(68); ((FormulaContext)_localctx).formula = formula();
				setState(69); match(B_CLOSE);
				 ((FormulaContext)_localctx).processed =  new FormulaImpl(BinaryOperator.ALL_UNTIL, _localctx.formula(0).processed, _localctx.formula(1).processed ); 
				}
				break;
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
				enterOuterAlt(_localctx, 8);
				{
				setState(72); ((FormulaContext)_localctx).temporal = temporal();
				setState(73); ((FormulaContext)_localctx).formula = formula();
				 ((FormulaContext)_localctx).processed =  new FormulaImpl(((FormulaContext)_localctx).temporal.op, _localctx.formula(0).processed ); 
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

	public static class TemporalContext extends ParserRuleContext {
		public Operator op;
		public TemporalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_temporal; }
	}

	public final TemporalContext temporal() throws RecognitionException {
		TemporalContext _localctx = new TemporalContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_temporal);
		try {
			setState(90);
			switch (_input.LA(1)) {
			case 6:
				enterOuterAlt(_localctx, 1);
				{
				setState(78); match(6);
				 ((TemporalContext)_localctx).op =  UnaryOperator.EXISTS_NEXT; 
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 2);
				{
				setState(80); match(5);
				 ((TemporalContext)_localctx).op =  UnaryOperator.ALL_NEXT; 
				}
				break;
			case 1:
				enterOuterAlt(_localctx, 3);
				{
				setState(82); match(1);
				 ((TemporalContext)_localctx).op =  UnaryOperator.EXISTS_FUTURE; 
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 4);
				{
				setState(84); match(2);
				 ((TemporalContext)_localctx).op =  UnaryOperator.ALL_FUTURE; 
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 5);
				{
				setState(86); match(4);
				 ((TemporalContext)_localctx).op =  UnaryOperator.EXISTS_GLOBAL; 
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 6);
				{
				setState(88); match(3);
				 ((TemporalContext)_localctx).op =  UnaryOperator.ALL_GLOBAL; 
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

	public static class Op_boolContext extends ParserRuleContext {
		public Operator op;
		public TerminalNode AND() { return getToken(CTLParser.AND, 0); }
		public TerminalNode OR() { return getToken(CTLParser.OR, 0); }
		public TerminalNode EQIV() { return getToken(CTLParser.EQIV, 0); }
		public TerminalNode IMPL() { return getToken(CTLParser.IMPL, 0); }
		public Op_boolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op_bool; }
	}

	public final Op_boolContext op_bool() throws RecognitionException {
		Op_boolContext _localctx = new Op_boolContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_op_bool);
		try {
			setState(100);
			switch (_input.LA(1)) {
			case AND:
				enterOuterAlt(_localctx, 1);
				{
				setState(92); match(AND);
				 ((Op_boolContext)_localctx).op =  BinaryOperator.AND; 
				}
				break;
			case OR:
				enterOuterAlt(_localctx, 2);
				{
				setState(94); match(OR);
				 ((Op_boolContext)_localctx).op =  BinaryOperator.OR; 
				}
				break;
			case IMPL:
				enterOuterAlt(_localctx, 3);
				{
				setState(96); match(IMPL);
				 ((Op_boolContext)_localctx).op =  BinaryOperator.IMPLICATION; 
				}
				break;
			case EQIV:
				enterOuterAlt(_localctx, 4);
				{
				setState(98); match(EQIV);
				 ((Op_boolContext)_localctx).op =  BinaryOperator.EQUIVALENCE; 
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

	public static class Op_floatContext extends ParserRuleContext {
		public FloatProposition.Operator op;
		public TerminalNode LT() { return getToken(CTLParser.LT, 0); }
		public TerminalNode LT_EQ() { return getToken(CTLParser.LT_EQ, 0); }
		public TerminalNode GT() { return getToken(CTLParser.GT, 0); }
		public TerminalNode N_EQ() { return getToken(CTLParser.N_EQ, 0); }
		public TerminalNode GT_EQ() { return getToken(CTLParser.GT_EQ, 0); }
		public TerminalNode EQ() { return getToken(CTLParser.EQ, 0); }
		public Op_floatContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_op_float; }
	}

	public final Op_floatContext op_float() throws RecognitionException {
		Op_floatContext _localctx = new Op_floatContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_op_float);
		try {
			setState(114);
			switch (_input.LA(1)) {
			case EQ:
				enterOuterAlt(_localctx, 1);
				{
				setState(102); match(EQ);
				 ((Op_floatContext)_localctx).op =  FloatProposition.Operator.EQ; 
				}
				break;
			case N_EQ:
				enterOuterAlt(_localctx, 2);
				{
				setState(104); match(N_EQ);
				 ((Op_floatContext)_localctx).op =  FloatProposition.Operator.NEQ; 
				}
				break;
			case GT:
				enterOuterAlt(_localctx, 3);
				{
				setState(106); match(GT);
				 ((Op_floatContext)_localctx).op =  FloatProposition.Operator.GT; 
				}
				break;
			case LT:
				enterOuterAlt(_localctx, 4);
				{
				setState(108); match(LT);
				 ((Op_floatContext)_localctx).op =  FloatProposition.Operator.LT; 
				}
				break;
			case GT_EQ:
				enterOuterAlt(_localctx, 5);
				{
				setState(110); match(GT_EQ);
				 ((Op_floatContext)_localctx).op =  FloatProposition.Operator.GT_EQ; 
				}
				break;
			case LT_EQ:
				enterOuterAlt(_localctx, 6);
				{
				setState(112); match(LT_EQ);
				 ((Op_floatContext)_localctx).op =  FloatProposition.Operator.LT_EQ; 
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

	public static class BoolContext extends ParserRuleContext {
		public TerminalNode TRUE() { return getToken(CTLParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(CTLParser.FALSE, 0); }
		public BoolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bool; }
	}

	public final BoolContext bool() throws RecognitionException {
		BoolContext _localctx = new BoolContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_bool);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
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

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3 y\4\2\t\2\4\3\t\3"+
		"\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\3\2\3\2\3\3\3"+
		"\3\3\3\3\3\5\3\33\n\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5"+
		"\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\3\6\3\6\3\6\5\6O\n\6\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\5\7]\n\7\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\bg\n\b\3\t\3\t\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\t\3\t\3\t\3\t\5\tu\n\t\3\n\3\n\3\n\2\2\13\2\4\6\b\n\f\16"+
		"\20\22\2\3\3\2\f\r\u0084\2\24\3\2\2\2\4\32\3\2\2\2\6\34\3\2\2\2\b%\3\2"+
		"\2\2\nN\3\2\2\2\f\\\3\2\2\2\16f\3\2\2\2\20t\3\2\2\2\22v\3\2\2\2\24\25"+
		"\5\4\3\2\25\3\3\2\2\2\26\27\5\6\4\2\27\30\5\4\3\2\30\33\3\2\2\2\31\33"+
		"\5\b\5\2\32\26\3\2\2\2\32\31\3\2\2\2\33\5\3\2\2\2\34\35\7\t\2\2\35\36"+
		"\7\36\2\2\36\37\b\4\1\2\37 \7\36\2\2 !\b\4\1\2!\"\5\20\t\2\"#\7\37\2\2"+
		"#$\b\4\1\2$\7\3\2\2\2%&\7\n\2\2&\'\5\n\6\2\'(\b\5\1\2(\t\3\2\2\2)*\7\f"+
		"\2\2*O\b\6\1\2+,\7\r\2\2,O\b\6\1\2-.\7\36\2\2.O\b\6\1\2/\60\7\16\2\2\60"+
		"\61\5\n\6\2\61\62\b\6\1\2\62O\3\2\2\2\63\64\7\34\2\2\64\65\5\n\6\2\65"+
		"\66\5\16\b\2\66\67\5\n\6\2\678\7\35\2\289\b\6\1\29O\3\2\2\2:;\7\24\2\2"+
		";<\7\34\2\2<=\5\n\6\2=>\7\25\2\2>?\5\n\6\2?@\7\35\2\2@A\b\6\1\2AO\3\2"+
		"\2\2BC\7\23\2\2CD\7\34\2\2DE\5\n\6\2EF\7\25\2\2FG\5\n\6\2GH\7\35\2\2H"+
		"I\b\6\1\2IO\3\2\2\2JK\5\f\7\2KL\5\n\6\2LM\b\6\1\2MO\3\2\2\2N)\3\2\2\2"+
		"N+\3\2\2\2N-\3\2\2\2N/\3\2\2\2N\63\3\2\2\2N:\3\2\2\2NB\3\2\2\2NJ\3\2\2"+
		"\2O\13\3\2\2\2PQ\7\b\2\2Q]\b\7\1\2RS\7\7\2\2S]\b\7\1\2TU\7\3\2\2U]\b\7"+
		"\1\2VW\7\4\2\2W]\b\7\1\2XY\7\6\2\2Y]\b\7\1\2Z[\7\5\2\2[]\b\7\1\2\\P\3"+
		"\2\2\2\\R\3\2\2\2\\T\3\2\2\2\\V\3\2\2\2\\X\3\2\2\2\\Z\3\2\2\2]\r\3\2\2"+
		"\2^_\7\17\2\2_g\b\b\1\2`a\7\20\2\2ag\b\b\1\2bc\7\21\2\2cg\b\b\1\2de\7"+
		"\22\2\2eg\b\b\1\2f^\3\2\2\2f`\3\2\2\2fb\3\2\2\2fd\3\2\2\2g\17\3\2\2\2"+
		"hi\7\26\2\2iu\b\t\1\2jk\7\27\2\2ku\b\t\1\2lm\7\30\2\2mu\b\t\1\2no\7\31"+
		"\2\2ou\b\t\1\2pq\7\32\2\2qu\b\t\1\2rs\7\33\2\2su\b\t\1\2th\3\2\2\2tj\3"+
		"\2\2\2tl\3\2\2\2tn\3\2\2\2tp\3\2\2\2tr\3\2\2\2u\21\3\2\2\2vw\t\2\2\2w"+
		"\23\3\2\2\2\7\32N\\ft";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}