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
	private boolean useDefaultLexer = false;

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
		return true;
	}

	public void printNextToken() throws MismatchedTokenException {
		int i = 0;
		try {
			String s = "";
			while (!Character.isWhitespace(input.LA(i))) {
				s += Character.toString(input.LA(i));
				i++;
			}
			System.out.println("Token >>>" + s + "<<<");
		} catch (Exception e) {
			System.out.println("Error >>>" + input.LA(i) + "<<<");
		}
	}

	@Override
	public void mTokens() throws RecognitionException {
		if (isKeyword("----")) {
			mRULE_RAWTEXT();
		} else if (isKeyword("#")) {
			mRULE_SL_COMMENT();
		} else if (isKeyword(" ") || isKeyword("\t") || isKeyword("\r")) {
			mRULE_WS();
		} else if (isKeyword("\n")) {
			mRULE_EOL();
			nextTokenIsWORD = false;
			useDefaultLexer = false;
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
			mT__10();
		} else if (isKeyword("Step-Definition:")) {
			mT__12();
		} else if (isKeyword("Step-Parameters:")) {
			mT__14();
		} else if (isKeyword("Test-Suite:")) {
			mT__15();
		} else if (isKeyword("Test-Setup:")) {
			mT__16();
		} else if (isKeyword("Test-Case:")) {
			mT__17();
		} else if (isKeyword("Test-Data:")) {
			mT__18();
		} else if (isKeyword("Given:")) {
			mT__19();
			useDefaultLexer = true;
		} else if (isKeyword("When:")) {
			mT__20();
			useDefaultLexer = true;
		} else if (isKeyword("Then:")) {
			mT__21();
			useDefaultLexer = true;
		} else if (isKeyword("And:")) {
			mT__22();
			useDefaultLexer = true;
		} else if (useDefaultLexer) {
			super.mTokens();
		} else {
			mRULE_WORD();
		}
	}
}