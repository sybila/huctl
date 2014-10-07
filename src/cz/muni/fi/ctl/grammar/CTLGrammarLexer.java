// Generated from /Users/daemontus/CTLGrammar.g4 by ANTLR 4.1
package cz.muni.fi.ctl.grammar;
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
		BOOLEAN=1, NEG=2, EX=3, AF=4, E=5, U=6, AND=7, B_OPEN=8, B_CLOSE=9, CB_OPEN=10, 
		CB_CLOSE=11;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"BOOLEAN", "'!'", "'EX'", "'AF'", "'E'", "'U'", "'&&'", "'('", "')'", 
		"'{'", "'}'"
	};
	public static final String[] ruleNames = {
		"BOOLEAN", "NEG", "EX", "AF", "E", "U", "AND", "B_OPEN", "B_CLOSE", "CB_OPEN", 
		"CB_CLOSE"
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
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\r;\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2#\n\2\3\3\3\3\3\4\3"+
		"\4\3\4\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3"+
		"\13\3\f\3\f\2\r\3\3\1\5\4\1\7\5\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23"+
		"\13\1\25\f\1\27\r\1\3\2\2;\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2"+
		"\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2"+
		"\25\3\2\2\2\2\27\3\2\2\2\3\"\3\2\2\2\5$\3\2\2\2\7&\3\2\2\2\t)\3\2\2\2"+
		"\13,\3\2\2\2\r.\3\2\2\2\17\60\3\2\2\2\21\63\3\2\2\2\23\65\3\2\2\2\25\67"+
		"\3\2\2\2\279\3\2\2\2\31\32\7V\2\2\32\33\7t\2\2\33\34\7w\2\2\34#\7g\2\2"+
		"\35\36\7H\2\2\36\37\7c\2\2\37 \7n\2\2 !\7u\2\2!#\7g\2\2\"\31\3\2\2\2\""+
		"\35\3\2\2\2#\4\3\2\2\2$%\7#\2\2%\6\3\2\2\2&\'\7G\2\2\'(\7Z\2\2(\b\3\2"+
		"\2\2)*\7C\2\2*+\7H\2\2+\n\3\2\2\2,-\7G\2\2-\f\3\2\2\2./\7W\2\2/\16\3\2"+
		"\2\2\60\61\7(\2\2\61\62\7(\2\2\62\20\3\2\2\2\63\64\7*\2\2\64\22\3\2\2"+
		"\2\65\66\7+\2\2\66\24\3\2\2\2\678\7}\2\28\26\3\2\2\29:\7\177\2\2:\30\3"+
		"\2\2\2\4\2\"";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}