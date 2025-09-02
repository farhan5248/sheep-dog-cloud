package org.farhan.dsl.asciidoc.formatting2;

import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.farhan.dsl.asciidoc.services.AsciiDocGrammarAccess;
import org.farhan.dsl.asciidoc.services.AsciiDocGrammarAccess.TestDataElements;
import org.farhan.dsl.asciidoc.asciiDoc.TestData;

public class TestDataFormatter extends Formatter {

	private TestData theTestData;

	public TestDataFormatter(TestData theTestData) {
		this.theTestData = theTestData;
	}

	public void format(IFormattableDocument doc, AsciiDocGrammarAccess ga, AsciiDocFormatter df) {
		TestDataElements a = ga.getTestDataAccess();
		formatKeywordTrailingSpace(df.getRegion(theTestData, a.getAsteriskKeyword_0()), doc);
		formatKeywordTrailingSpace(df.getRegion(theTestData, a.getTestDataKeyword_1()), doc);
		formatTitle(df.getRegion(theTestData, a.getNameTitleParserRuleCall_2_0()), doc);

		if (theTestData.getTable() != null || theTestData.getStatementList() != null) {
			formatEOL1RuleCall(df.getRegion(theTestData, a.getEOLTerminalRuleCall_3()), doc);
		} else {
			formatEOL2RuleCall(df.getRegion(theTestData, a.getEOLTerminalRuleCall_3()), doc);
		}

		if (theTestData.getStatementList() != null) {
			StatementListFormatter formatter = new StatementListFormatter(theTestData.getStatementList());
			formatter.format(doc, ga, df);
		}

		if (theTestData.getTable() != null) {
			TableFormatter formatter = new TableFormatter(theTestData.getTable());
			formatter.format(doc, ga, df);
		}
	}

}
