package org.farhan.dsl.asciidoc.impl;

import java.util.ArrayList;

import org.farhan.dsl.lang.IStatement;
import org.farhan.dsl.lang.ITestStep;
import org.farhan.dsl.lang.ITestStepContainer;
import org.farhan.dsl.lang.ITestSuite;
import org.farhan.dsl.asciidoc.asciiDoc.Statement;
import org.farhan.dsl.asciidoc.asciiDoc.TestStep;
import org.farhan.dsl.asciidoc.asciiDoc.TestStepContainer;
import org.farhan.dsl.asciidoc.asciiDoc.TestSuite;

public class TestStepContainerImpl implements ITestStepContainer {

	protected TestStepContainer eObject;
	ITestSuite parent;

	public TestStepContainerImpl(TestStepContainer testCase) {
		this.eObject = testCase;
	}

	@Override
	public ITestStep createTestStep(String value) {
		// Not needed in this project
		return null;
	}

	@Override
	public String getName() {
		return eObject.getName();
	}

	@Override
	public ITestSuite getParent() {
		if (parent == null) {
			parent = new TestSuiteImpl((TestSuite) eObject.eContainer());
		}
		return parent;
	}

	@Override
	public ArrayList<IStatement> getStatementList() {
		ArrayList<IStatement> statementList = new ArrayList<IStatement>();
		for (Statement s : eObject.getStatementList()) {
			statementList.add(new StatementImpl(s));
		}
		return statementList;
	}

	@Override
	public ArrayList<ITestStep> getTestStepList() {
		ArrayList<ITestStep> testStepList = new ArrayList<ITestStep>();
		for (TestStep s : eObject.getTestStepList()) {
			TestStepImpl testStep = new TestStepImpl(s);
			testStep.setParent(this);
			testStepList.add(testStep);
		}
		return testStepList;
	}

	@Override
	public void setName(String value) {
		// Not needed in this project
	}

	@Override
	public void setParent(ITestSuite value) {
		this.parent = value;
	}

	@Override
	public void setStatementList(ArrayList<IStatement> value) {
		// Not needed in this project
	}

	@Override
	public void setTestStepList(ArrayList<ITestStep> value) {
		// Not needed in this project
	}

}
