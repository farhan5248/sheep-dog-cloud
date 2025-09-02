package org.farhan.dsl.asciidoc.formatting2;

import org.eclipse.xtext.formatting2.IFormattableDocument;
import org.farhan.dsl.asciidoc.services.AsciiDocGrammarAccess;
import org.farhan.dsl.asciidoc.services.AsciiDocGrammarAccess.StepDefinitionElements;
import org.farhan.dsl.asciidoc.asciiDoc.Statement;
import org.farhan.dsl.asciidoc.asciiDoc.StepDefinition;
import org.farhan.dsl.asciidoc.asciiDoc.StepParameters;

public class StepDefinitionFormatter extends Formatter {

	private StepDefinition theStepDefinition;

	public StepDefinitionFormatter(StepDefinition theStepDefinition) {
		this.theStepDefinition = theStepDefinition;
	}

	public void format(IFormattableDocument doc, AsciiDocGrammarAccess ga, AsciiDocFormatter df) {
		StepDefinitionElements a = ga.getStepDefinitionAccess();

		formatKeywordTrailingSpace(df.getRegion(theStepDefinition, a.getEqualsSignEqualsSignKeyword_0()), doc);
		formatKeywordTrailingSpace(df.getRegion(theStepDefinition, a.getStepDefinitionKeyword_1()), doc);
		formatTitle(df.getRegion(theStepDefinition, a.getNameTitleParserRuleCall_2_0()), doc);
		formatEOL2RuleCall(df.getRegion(theStepDefinition, a.getEOLTerminalRuleCall_3()), doc);
		for (Statement s : theStepDefinition.getStatementList()) {
			StatementFormatter formatter = new StatementFormatter(s);
			formatter.isLast(isLastElement(s, theStepDefinition.getStatementList()));
			formatter.format(doc, ga, df);
		}
		for (StepParameters e : theStepDefinition.getStepParameterList()) {
			StepParametersFormatter formatter = new StepParametersFormatter(e);
			formatter.format(doc, ga, df);
		}
	}
}
