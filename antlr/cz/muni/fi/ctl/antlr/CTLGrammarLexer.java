// Generated from /Users/daemontus/Synced/Java Development/Parametrized CTL Model Checker/CTL Parser/grammar/CTLGrammar.g4 by ANTLR 4.1
package cz.muni.fi.ctl.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CTLGrammarLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TRUE=1, FALSE=2, NEG=3, AND=4, OR=5, IMPL=6, EQIV=7, ALL=8, EXISTS=9, 
		NEXT=10, FUTURE=11, GLOBAL=12, UNTIL=13, EQ=14, N_EQ=15, GT=16, LT=17, 
		GT_EQ=18, LT_EQ=19, B_OPEN=20, B_CLOSE=21, CB_OPEN=22, CB_CLOSE=23, SB_OPEN=24, 
		SB_CLOSE=25, VAR_NAME=26, FLOAT_VAL=27, WS=28;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'True'", "'False'", "'!'", "'&&'", "'||'", "'=>'", "'<=>'", "'A'", "'E'", 
		"'X'", "'F'", "'G'", "'U'", "'=='", "'!='", "'>'", "'<'", "'>='", "'<='", 
		"'('", "')'", "'{'", "'}'", "'['", "']'", "VAR_NAME", "FLOAT_VAL", "WS"
	};
	public static final String[] ruleNames = {
		"TRUE", "FALSE", "NEG", "AND", "OR", "IMPL", "EQIV", "ALL", "EXISTS", 
		"NEXT", "FUTURE", "GLOBAL", "UNTIL", "EQ", "N_EQ", "GT", "LT", "GT_EQ", 
		"LT_EQ", "B_OPEN", "B_CLOSE", "CB_OPEN", "CB_CLOSE", "SB_OPEN", "SB_CLOSE", 
		"VAR_NAME", "FLOAT_VAL", "WS"
	};


	public CTLGrammarLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CTLGrammar.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 27: WS_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0: skip();  break;
		}
	}

	public static final String _serializedATN =
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\36\u0095\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\3\2\3\2\3\2\3\2\3\2\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3"+
		"\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3"+
		"\17\3\17\3\20\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\23\3\24\3\24\3"+
		"\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3"+
		"\33\6\33\u0080\n\33\r\33\16\33\u0081\3\34\6\34\u0085\n\34\r\34\16\34\u0086"+
		"\3\34\3\34\6\34\u008b\n\34\r\34\16\34\u008c\3\35\6\35\u0090\n\35\r\35"+
		"\16\35\u0091\3\35\3\35\2\36\3\3\1\5\4\1\7\5\1\t\6\1\13\7\1\r\b\1\17\t"+
		"\1\21\n\1\23\13\1\25\f\1\27\r\1\31\16\1\33\17\1\35\20\1\37\21\1!\22\1"+
		"#\23\1%\24\1\'\25\1)\26\1+\27\1-\30\1/\31\1\61\32\1\63\33\1\65\34\1\67"+
		"\35\19\36\2\3\2\6\3\2c|\4\2C\\c|\3\2\62;\4\2\13\13\"\"\u0098\2\3\3\2\2"+
		"\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3"+
		"\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2"+
		"\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2"+
		"\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2"+
		"\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\3;\3\2\2\2\5@\3"+
		"\2\2\2\7F\3\2\2\2\tH\3\2\2\2\13K\3\2\2\2\rN\3\2\2\2\17Q\3\2\2\2\21U\3"+
		"\2\2\2\23W\3\2\2\2\25Y\3\2\2\2\27[\3\2\2\2\31]\3\2\2\2\33_\3\2\2\2\35"+
		"a\3\2\2\2\37d\3\2\2\2!g\3\2\2\2#i\3\2\2\2%k\3\2\2\2\'n\3\2\2\2)q\3\2\2"+
		"\2+s\3\2\2\2-u\3\2\2\2/w\3\2\2\2\61y\3\2\2\2\63{\3\2\2\2\65}\3\2\2\2\67"+
		"\u0084\3\2\2\29\u008f\3\2\2\2;<\7V\2\2<=\7t\2\2=>\7w\2\2>?\7g\2\2?\4\3"+
		"\2\2\2@A\7H\2\2AB\7c\2\2BC\7n\2\2CD\7u\2\2DE\7g\2\2E\6\3\2\2\2FG\7#\2"+
		"\2G\b\3\2\2\2HI\7(\2\2IJ\7(\2\2J\n\3\2\2\2KL\7~\2\2LM\7~\2\2M\f\3\2\2"+
		"\2NO\7?\2\2OP\7@\2\2P\16\3\2\2\2QR\7>\2\2RS\7?\2\2ST\7@\2\2T\20\3\2\2"+
		"\2UV\7C\2\2V\22\3\2\2\2WX\7G\2\2X\24\3\2\2\2YZ\7Z\2\2Z\26\3\2\2\2[\\\7"+
		"H\2\2\\\30\3\2\2\2]^\7I\2\2^\32\3\2\2\2_`\7W\2\2`\34\3\2\2\2ab\7?\2\2"+
		"bc\7?\2\2c\36\3\2\2\2de\7#\2\2ef\7?\2\2f \3\2\2\2gh\7@\2\2h\"\3\2\2\2"+
		"ij\7>\2\2j$\3\2\2\2kl\7@\2\2lm\7?\2\2m&\3\2\2\2no\7>\2\2op\7?\2\2p(\3"+
		"\2\2\2qr\7*\2\2r*\3\2\2\2st\7+\2\2t,\3\2\2\2uv\7}\2\2v.\3\2\2\2wx\7\177"+
		"\2\2x\60\3\2\2\2yz\7]\2\2z\62\3\2\2\2{|\7_\2\2|\64\3\2\2\2}\177\t\2\2"+
		"\2~\u0080\t\3\2\2\177~\3\2\2\2\u0080\u0081\3\2\2\2\u0081\177\3\2\2\2\u0081"+
		"\u0082\3\2\2\2\u0082\66\3\2\2\2\u0083\u0085\t\4\2\2\u0084\u0083\3\2\2"+
		"\2\u0085\u0086\3\2\2\2\u0086\u0084\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0088"+
		"\3\2\2\2\u0088\u008a\13\2\2\2\u0089\u008b\t\4\2\2\u008a\u0089\3\2\2\2"+
		"\u008b\u008c\3\2\2\2\u008c\u008a\3\2\2\2\u008c\u008d\3\2\2\2\u008d8\3"+
		"\2\2\2\u008e\u0090\t\5\2\2\u008f\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091"+
		"\u008f\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0093\3\2\2\2\u0093\u0094\b\35"+
		"\2\2\u0094:\3\2\2\2\7\2\u0081\u0086\u008c\u0091";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}