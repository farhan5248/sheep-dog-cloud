package org.farhan.dsl.asciidoc.formatting2;

import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.farhan.dsl.asciidoc.services.AsciiDocGrammarAccess;
import org.farhan.dsl.asciidoc.services.AsciiDocGrammarAccess.NestedDescriptionElements;
import org.farhan.dsl.asciidoc.asciiDoc.Line;
import org.farhan.dsl.asciidoc.asciiDoc.NestedDescription;

public class NestedStatementListFormatter extends Formatter {

	private NestedDescription theStatementList;

	public NestedStatementListFormatter(NestedDescription theStatementList) {
		this.theStatementList = theStatementList;
	}

	public void format(IFormattableDocument doc, AsciiDocGrammarAccess ga, AsciiDocFormatter df) {
		NestedDescriptionElements a = ga.getNestedDescriptionAccess();
		formatKeywordNoSpace(df.getRegion(theStatementList, a.getPlusSignKeyword_0()), doc);
		formatEOL1RuleCall(df.getRegion(theStatementList, a.getEOLTerminalRuleCall_1()), doc);

		for (Line s : theStatementList.getLineList()) {
			StatementFormatter formatter = new StatementFormatter(s);
			formatter.isLast(isLastElement(s, theStatementList.getLineList()));
			formatter.format(doc, ga, df);
		}
	}
}
