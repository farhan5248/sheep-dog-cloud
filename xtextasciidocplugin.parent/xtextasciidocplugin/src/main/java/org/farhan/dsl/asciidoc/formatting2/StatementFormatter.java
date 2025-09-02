package org.farhan.dsl.asciidoc.formatting2;

import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.eclipse.xtext.formatting2.regionaccess.ISemanticRegion;
import org.farhan.dsl.asciidoc.services.AsciiDocGrammarAccess;
import org.farhan.dsl.asciidoc.services.AsciiDocGrammarAccess.StatementElements;
import org.farhan.dsl.asciidoc.asciiDoc.Statement;

public class StatementFormatter extends Formatter {

	private Statement theStatement;

	public StatementFormatter(Statement theStatement) {
		this.theStatement = theStatement;
	}

	public void format(IFormattableDocument doc, AsciiDocGrammarAccess ga, AsciiDocFormatter df) {
		StatementElements a = ga.getStatementAccess();
		formatTitleNoSpace(df.getRegion(theStatement, a.getNameTitleParserRuleCall_0_0()), doc);
		formatEOL12RuleCall(df.getRegion(theStatement, a.getEOLTerminalRuleCall_1()), doc);
	}

	protected void formatEOL12RuleCall(ISemanticRegion iSR, IFormattableDocument doc) {
		if (isLast) {
			formatEOL2RuleCall(iSR, doc);
		} else {
			formatEOL2MaxRuleCall(iSR, doc);
		}
	}

}
