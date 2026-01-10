package org.farhan.dsl.asciidoc.formatting2;

import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.farhan.dsl.asciidoc.services.AsciiDocGrammarAccess;
import org.farhan.dsl.asciidoc.services.AsciiDocGrammarAccess.NestedStatementListElements;
import org.farhan.dsl.asciidoc.asciiDoc.Statement;
import org.farhan.dsl.asciidoc.asciiDoc.NestedStatementList;

public class NestedStatementListFormatter extends Formatter {

	private NestedStatementList theStatementList;

	public NestedStatementListFormatter(NestedStatementList theStatementList) {
		this.theStatementList = theStatementList;
	}

	public void format(IFormattableDocument doc, AsciiDocGrammarAccess ga, AsciiDocFormatter df) {
		NestedStatementListElements a = ga.getNestedStatementListAccess();
		formatKeywordNoSpace(df.getRegion(theStatementList, a.getPlusSignKeyword_0()), doc);
		formatEOL1RuleCall(df.getRegion(theStatementList, a.getEOLTerminalRuleCall_1()), doc);

		for (Statement s : theStatementList.getStatementList()) {
			StatementFormatter formatter = new StatementFormatter(s);
			formatter.isLast(isLastElement(s, theStatementList.getStatementList()));
			formatter.format(doc, ga, df);
		}
	}
}
