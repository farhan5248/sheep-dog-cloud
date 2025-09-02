package org.farhan.dsl.asciidoc.parser.antlr.internal;

import org.eclipse.xtext.parser.antlr.Lexer;
import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class MyAsciiDocLexer extends InternalAsciiDocLexer {

	// TODO maybe these should all be one flag called escapeKeyword?
	private boolean nextTokenIsWORD = false;

	public MyAsciiDocLexer() {
	}

	public MyAsciiDocLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}

	public MyAsciiDocLexer(CharStream input, RecognizerSharedState state) {
		super(input, state);
	}

	public boolean isKeyword(String s) throws MismatchedTokenException {
		int i = 0;
		while (i < s.length()) {
			input.LA(i + 1);
			if (input.LA(i + 1) != s.charAt(i)) {
				return false;
			}
			i++;
		}
		// System.out.println(">>>" + s + "<<<");
		return true;
	}

	@Override
	public void mTokens() throws RecognitionException {
		// TODO document how to update the lexer in markdown
		// get the position from input and find the next set of non-space characters
		// This is a hack, ideally the xtext grammar file needs to be defined better,
		// when I have more time I'll review the need to do this but for now I figured
		// it'd be a good example on how to hack the lexer for newbies

		if (isKeyword("----")) {
			mRULE_RAWTEXT();// ( '----' RULE_EOL ( . )+ '----' RULE_EOL )
		} else if (isKeyword("#")) {
			mRULE_SL_COMMENT();
		} else if (isKeyword("\n")) {
			mRULE_EOL();
			nextTokenIsWORD = false;
		} else if (isKeyword(" ") || isKeyword("\t") || isKeyword("\r")) {
			mRULE_WS();
		} else if (isKeyword("+")) {
			mT__23();
		} else if (isKeyword("|===")) {
			mT__24();
		} else if (isKeyword("*")) {
			mT__13();
		} else if (isKeyword("==")) {
			mT__11();
		} else if (isKeyword("=")) {
			mT__9();
		} else if (isKeyword("|")) {
			mT__25();
			nextTokenIsWORD = true;
		} else if (nextTokenIsWORD) {
			mRULE_WORD();
		} else if (isKeyword("Step-Object:")) {
			mT__10();// 'Object:'
		} else if (isKeyword("Step-Definition:")) {
			mT__12();// 'Definition:'
		} else if (isKeyword("Step-Parameters:")) {
			mT__14();// 'Parameters:'
		} else if (isKeyword("Test-Suite:")) {
			mT__15();// 'Feature:'
		} else if (isKeyword("Test-Setup:")) {
			mT__16();// 'Background:'
		} else if (isKeyword("Test-Case:")) {
			mT__17();// 'Scenario:'
		} else if (isKeyword("Test-Data:")) {
			mT__18();// 'Examples:'
		} else if (isKeyword("Given:")) {
			mT__19();// 'Given'
		} else if (isKeyword("When:")) {
			mT__20();// 'When'
		} else if (isKeyword("Then:")) {
			mT__21();// 'Then'
		} else if (isKeyword("And:")) {
			mT__22();// 'And'
		} else {
			mRULE_WORD();
		}
	}
}