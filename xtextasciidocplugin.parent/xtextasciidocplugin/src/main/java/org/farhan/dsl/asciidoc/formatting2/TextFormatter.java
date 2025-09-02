package org.farhan.dsl.asciidoc.formatting2;

import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.farhan.dsl.asciidoc.services.AsciiDocGrammarAccess;
import org.farhan.dsl.asciidoc.services.AsciiDocGrammarAccess.TextElements;
import org.farhan.dsl.asciidoc.asciiDoc.Text;

public class TextFormatter extends Formatter {

	private Text theDocString;

	public TextFormatter(Text theDocString) {
		this.theDocString = theDocString;
	}

	public void format(IFormattableDocument doc, AsciiDocGrammarAccess ga, AsciiDocFormatter df) {
		TextElements a = ga.getTextAccess();
		formatKeywordNoSpace(df.getRegion(theDocString, a.getPlusSignKeyword_0()), doc);
		formatEOL1RuleCall(df.getRegion(theDocString, a.getEOLTerminalRuleCall_1()), doc);
		formatTitleNoSpace(df.getRegion(theDocString, a.getNameRAWTEXTTerminalRuleCall_2_0()), doc);
		formatEOL2RuleCall(df.getRegion(theDocString, a.getEOLTerminalRuleCall_3()), doc);
	}

}
