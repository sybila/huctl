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

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CTLLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__5=1, T__4=2, T__3=3, T__2=4, T__1=5, T__0=6, DEF=7, PROP=8, NEWLINE=9, 
		TRUE=10, FALSE=11, NEG=12, AND=13, OR=14, IMPL=15, EQIV=16, ALL=17, EXISTS=18, 
		UNTIL=19, EQ=20, N_EQ=21, GT=22, LT=23, GT_EQ=24, LT_EQ=25, B_OPEN=26, 
		B_CLOSE=27, VAR_NAME=28, FLOAT_VAL=29, WS=30;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"'EF'", "'AF'", "'AG'", "'EG'", "'AX'", "'EX'", "'#define'", "'#property'", 
		"NEWLINE", "'True'", "'False'", "'!'", "'&&'", "'||'", "'=>'", "'<=>'", 
		"'A'", "'E'", "'U'", "'=='", "'!='", "'>'", "'<'", "'>='", "'<='", "'('", 
		"')'", "VAR_NAME", "FLOAT_VAL", "WS"
	};
	public static final String[] ruleNames = {
		"T__5", "T__4", "T__3", "T__2", "T__1", "T__0", "DEF", "PROP", "NEWLINE", 
		"TRUE", "FALSE", "NEG", "AND", "OR", "IMPL", "EQIV", "ALL", "EXISTS", 
		"UNTIL", "EQ", "N_EQ", "GT", "LT", "GT_EQ", "LT_EQ", "B_OPEN", "B_CLOSE", 
		"VAR_NAME", "FLOAT_VAL", "WS"
	};


	public CTLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CTL.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2 \u00b3\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\3\2\3\2\3"+
		"\2\3\3\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3"+
		"\n\3\n\5\ng\n\n\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3"+
		"\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3"+
		"\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\25\3\26\3\26\3\26\3\27\3\27\3"+
		"\30\3\30\3\31\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\34\3\34\3\35\6\35\u009e"+
		"\n\35\r\35\16\35\u009f\3\36\6\36\u00a3\n\36\r\36\16\36\u00a4\3\36\3\36"+
		"\6\36\u00a9\n\36\r\36\16\36\u00aa\3\37\6\37\u00ae\n\37\r\37\16\37\u00af"+
		"\3\37\3\37\2\2 \3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31"+
		"\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65"+
		"\34\67\359\36;\37= \3\2\6\4\2\f\f\17\17\4\2C\\c|\3\2\62;\4\2\13\f\"\""+
		"\u00b7\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2"+
		"\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3"+
		"\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2"+
		"\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2"+
		"/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2"+
		"\2\2;\3\2\2\2\2=\3\2\2\2\3?\3\2\2\2\5B\3\2\2\2\7E\3\2\2\2\tH\3\2\2\2\13"+
		"K\3\2\2\2\rN\3\2\2\2\17Q\3\2\2\2\21Y\3\2\2\2\23f\3\2\2\2\25h\3\2\2\2\27"+
		"m\3\2\2\2\31s\3\2\2\2\33u\3\2\2\2\35x\3\2\2\2\37{\3\2\2\2!~\3\2\2\2#\u0082"+
		"\3\2\2\2%\u0084\3\2\2\2\'\u0086\3\2\2\2)\u0088\3\2\2\2+\u008b\3\2\2\2"+
		"-\u008e\3\2\2\2/\u0090\3\2\2\2\61\u0092\3\2\2\2\63\u0095\3\2\2\2\65\u0098"+
		"\3\2\2\2\67\u009a\3\2\2\29\u009d\3\2\2\2;\u00a2\3\2\2\2=\u00ad\3\2\2\2"+
		"?@\7G\2\2@A\7H\2\2A\4\3\2\2\2BC\7C\2\2CD\7H\2\2D\6\3\2\2\2EF\7C\2\2FG"+
		"\7I\2\2G\b\3\2\2\2HI\7G\2\2IJ\7I\2\2J\n\3\2\2\2KL\7C\2\2LM\7Z\2\2M\f\3"+
		"\2\2\2NO\7G\2\2OP\7Z\2\2P\16\3\2\2\2QR\7%\2\2RS\7f\2\2ST\7g\2\2TU\7h\2"+
		"\2UV\7k\2\2VW\7p\2\2WX\7g\2\2X\20\3\2\2\2YZ\7%\2\2Z[\7r\2\2[\\\7t\2\2"+
		"\\]\7q\2\2]^\7r\2\2^_\7g\2\2_`\7t\2\2`a\7v\2\2ab\7{\2\2b\22\3\2\2\2cd"+
		"\7\17\2\2dg\7\f\2\2eg\t\2\2\2fc\3\2\2\2fe\3\2\2\2g\24\3\2\2\2hi\7V\2\2"+
		"ij\7t\2\2jk\7w\2\2kl\7g\2\2l\26\3\2\2\2mn\7H\2\2no\7c\2\2op\7n\2\2pq\7"+
		"u\2\2qr\7g\2\2r\30\3\2\2\2st\7#\2\2t\32\3\2\2\2uv\7(\2\2vw\7(\2\2w\34"+
		"\3\2\2\2xy\7~\2\2yz\7~\2\2z\36\3\2\2\2{|\7?\2\2|}\7@\2\2} \3\2\2\2~\177"+
		"\7>\2\2\177\u0080\7?\2\2\u0080\u0081\7@\2\2\u0081\"\3\2\2\2\u0082\u0083"+
		"\7C\2\2\u0083$\3\2\2\2\u0084\u0085\7G\2\2\u0085&\3\2\2\2\u0086\u0087\7"+
		"W\2\2\u0087(\3\2\2\2\u0088\u0089\7?\2\2\u0089\u008a\7?\2\2\u008a*\3\2"+
		"\2\2\u008b\u008c\7#\2\2\u008c\u008d\7?\2\2\u008d,\3\2\2\2\u008e\u008f"+
		"\7@\2\2\u008f.\3\2\2\2\u0090\u0091\7>\2\2\u0091\60\3\2\2\2\u0092\u0093"+
		"\7@\2\2\u0093\u0094\7?\2\2\u0094\62\3\2\2\2\u0095\u0096\7>\2\2\u0096\u0097"+
		"\7?\2\2\u0097\64\3\2\2\2\u0098\u0099\7*\2\2\u0099\66\3\2\2\2\u009a\u009b"+
		"\7+\2\2\u009b8\3\2\2\2\u009c\u009e\t\3\2\2\u009d\u009c\3\2\2\2\u009e\u009f"+
		"\3\2\2\2\u009f\u009d\3\2\2\2\u009f\u00a0\3\2\2\2\u00a0:\3\2\2\2\u00a1"+
		"\u00a3\t\4\2\2\u00a2\u00a1\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00a2\3\2"+
		"\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a8\13\2\2\2\u00a7"+
		"\u00a9\t\4\2\2\u00a8\u00a7\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa\u00a8\3\2"+
		"\2\2\u00aa\u00ab\3\2\2\2\u00ab<\3\2\2\2\u00ac\u00ae\t\5\2\2\u00ad\u00ac"+
		"\3\2\2\2\u00ae\u00af\3\2\2\2\u00af\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0"+
		"\u00b1\3\2\2\2\u00b1\u00b2\b\37\2\2\u00b2>\3\2\2\2\b\2f\u009f\u00a4\u00aa"+
		"\u00af\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}