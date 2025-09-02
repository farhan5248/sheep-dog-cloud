package org.farhan.dsl.asciidoc.parser.antlr;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.TokenSource;
import org.farhan.dsl.asciidoc.parser.antlr.internal.MyAsciiDocLexer;

public class MyAsciiDocParser extends AsciiDocParser {

	@Override
	protected TokenSource createLexer(CharStream stream) {
		return new MyAsciiDocLexer(stream);
	}
}
