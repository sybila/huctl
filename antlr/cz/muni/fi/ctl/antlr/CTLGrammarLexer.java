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
		BOOLEAN=1, OPERATOR=2, NEG=3, EX=4, AF=5, E=6, U=7, AND=8, B_OPEN=9, B_CLOSE=10, 
		CB_OPEN=11, CB_CLOSE=12, EQ=13, N_EQ=14, GT=15, LT=16, GT_EQ=17, LT_EQ=18, 
		VARIABLE=19, FLOAT=20;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"BOOLEAN", "OPERATOR", "'!'", "'EX'", "'AF'", "'E'", "'U'", "'&&'", "'('", 
		"')'", "'{'", "'}'", "'=='", "'!='", "'>'", "'<'", "'>='", "'<='", "VARIABLE", 
		"FLOAT"
	};
	public static final String[] ruleNames = {
		"BOOLEAN", "OPERATOR", "NEG", "EX", "AF", "E", "U", "AND", "B_OPEN", "B_CLOSE", 
		"CB_OPEN", "CB_CLOSE", "EQ", "N_EQ", "GT", "LT", "GT_EQ", "LT_EQ", "VARIABLE", 
		"FLOAT"
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

	public static final String _serializedATN =
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\26u\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2"+
		"\65\n\2\3\3\3\3\3\3\3\3\3\3\3\3\5\3=\n\3\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3"+
		"\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16"+
		"\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\23\3\23"+
		"\3\23\3\24\6\24g\n\24\r\24\16\24h\3\25\6\25l\n\25\r\25\16\25m\3\25\3\25"+
		"\6\25r\n\25\r\25\16\25s\2\26\3\3\1\5\4\1\7\5\1\t\6\1\13\7\1\r\b\1\17\t"+
		"\1\21\n\1\23\13\1\25\f\1\27\r\1\31\16\1\33\17\1\35\20\1\37\21\1!\22\1"+
		"#\23\1%\24\1\'\25\1)\26\1\3\2\4\3\2c|\3\2\62;}\2\3\3\2\2\2\2\5\3\2\2\2"+
		"\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3"+
		"\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2"+
		"\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2"+
		"\2\2\2)\3\2\2\2\3\64\3\2\2\2\5<\3\2\2\2\7>\3\2\2\2\t@\3\2\2\2\13C\3\2"+
		"\2\2\rF\3\2\2\2\17H\3\2\2\2\21J\3\2\2\2\23M\3\2\2\2\25O\3\2\2\2\27Q\3"+
		"\2\2\2\31S\3\2\2\2\33U\3\2\2\2\35X\3\2\2\2\37[\3\2\2\2!]\3\2\2\2#_\3\2"+
		"\2\2%b\3\2\2\2\'f\3\2\2\2)k\3\2\2\2+,\7V\2\2,-\7t\2\2-.\7w\2\2.\65\7g"+
		"\2\2/\60\7H\2\2\60\61\7c\2\2\61\62\7n\2\2\62\63\7u\2\2\63\65\7g\2\2\64"+
		"+\3\2\2\2\64/\3\2\2\2\65\4\3\2\2\2\66=\5\33\16\2\67=\5\35\17\28=\5\37"+
		"\20\29=\5!\21\2:=\5#\22\2;=\5%\23\2<\66\3\2\2\2<\67\3\2\2\2<8\3\2\2\2"+
		"<9\3\2\2\2<:\3\2\2\2<;\3\2\2\2=\6\3\2\2\2>?\7#\2\2?\b\3\2\2\2@A\7G\2\2"+
		"AB\7Z\2\2B\n\3\2\2\2CD\7C\2\2DE\7H\2\2E\f\3\2\2\2FG\7G\2\2G\16\3\2\2\2"+
		"HI\7W\2\2I\20\3\2\2\2JK\7(\2\2KL\7(\2\2L\22\3\2\2\2MN\7*\2\2N\24\3\2\2"+
		"\2OP\7+\2\2P\26\3\2\2\2QR\7}\2\2R\30\3\2\2\2ST\7\177\2\2T\32\3\2\2\2U"+
		"V\7?\2\2VW\7?\2\2W\34\3\2\2\2XY\7#\2\2YZ\7?\2\2Z\36\3\2\2\2[\\\7@\2\2"+
		"\\ \3\2\2\2]^\7>\2\2^\"\3\2\2\2_`\7@\2\2`a\7?\2\2a$\3\2\2\2bc\7>\2\2c"+
		"d\7?\2\2d&\3\2\2\2eg\t\2\2\2fe\3\2\2\2gh\3\2\2\2hf\3\2\2\2hi\3\2\2\2i"+
		"(\3\2\2\2jl\t\3\2\2kj\3\2\2\2lm\3\2\2\2mk\3\2\2\2mn\3\2\2\2no\3\2\2\2"+
		"oq\13\2\2\2pr\t\3\2\2qp\3\2\2\2rs\3\2\2\2sq\3\2\2\2st\3\2\2\2t*\3\2\2"+
		"\2\b\2\64<hms";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}