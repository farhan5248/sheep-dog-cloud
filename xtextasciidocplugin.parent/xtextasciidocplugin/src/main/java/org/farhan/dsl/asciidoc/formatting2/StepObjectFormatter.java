package org.farhan.dsl.asciidoc.formatting2;

import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.farhan.dsl.asciidoc.services.AsciiDocGrammarAccess;
import org.farhan.dsl.asciidoc.services.AsciiDocGrammarAccess.StepObjectElements;
import org.farhan.dsl.asciidoc.asciiDoc.StepObject;
import org.farhan.dsl.asciidoc.asciiDoc.Statement;
import org.farhan.dsl.asciidoc.asciiDoc.StepDefinition;

public class StepObjectFormatter extends Formatter {

	private StepObject theStepObject;

	public StepObjectFormatter(StepObject theStepObject) {
		this.theStepObject = theStepObject;
	}

	public void format(IFormattableDocument doc, AsciiDocGrammarAccess ga, AsciiDocFormatter df) {
		StepObjectElements a = ga.getStepObjectAccess();

		formatKeywordTrailingSpace(df.getRegion(theStepObject, a.getEqualsSignKeyword_0()), doc);
		formatKeywordTrailingSpace(df.getRegion(theStepObject, a.getStepObjectKeyword_1()), doc);
		formatTitle(df.getRegion(theStepObject, a.getNameTitleParserRuleCall_2_0()), doc);
		formatEOL2RuleCall(df.getRegion(theStepObject, a.getEOLTerminalRuleCall_3()), doc);
		for (Statement s : theStepObject.getStatementList()) {
			StatementFormatter formatter = new StatementFormatter(s);
			formatter.isLast(isLastElement(s, theStepObject.getStatementList()));
			formatter.format(doc, ga, df);
		}
		for (StepDefinition s : theStepObject.getStepDefinitionList()) {
			StepDefinitionFormatter formatter = new StepDefinitionFormatter(s);
			formatter.format(doc, ga, df);
		}
	}

}
