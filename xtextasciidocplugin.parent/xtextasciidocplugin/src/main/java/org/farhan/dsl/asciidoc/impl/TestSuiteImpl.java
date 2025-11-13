package org.farhan.dsl.asciidoc.impl;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.farhan.dsl.lang.IStatement;
import org.farhan.dsl.lang.ITestCase;
import org.farhan.dsl.lang.ITestProject;
import org.farhan.dsl.lang.ITestSetup;
import org.farhan.dsl.lang.ITestStepContainer;
import org.farhan.dsl.lang.ITestSuite;
import org.farhan.dsl.asciidoc.asciiDoc.TestCase;
import org.farhan.dsl.asciidoc.asciiDoc.TestSetup;
import org.farhan.dsl.asciidoc.asciiDoc.TestStepContainer;
import org.farhan.dsl.asciidoc.asciiDoc.TestSuite;

public class TestSuiteImpl implements ITestSuite {

	private static final Logger logger = LoggerFactory.getLogger(TestSuiteImpl.class);

	private ITestProject parent;
	private TestSuite eObject;

	public TestSuiteImpl(TestSuite testSuite) {
		this.eObject = testSuite;
	}

	@Override
	public ITestCase createTestCase(String value) {
		// Not needed in this project
		return null;
	}

	@Override
	public ITestStepContainer createTestSetup(String name) {
		// Not needed in this project
		return null;
	}

	@Override
	public String getName() {
		return eObject.getName();
	}

	@Override
	public ITestProject getParent() {
		return parent;
	}

	@Override
	public String getQualifiedName() {
		// Not needed in this project
		return null;
	}

	@Override
	public ArrayList<IStatement> getStatementList() {
		// Not needed in this project
		return null;
	}

	@Override
	public ArrayList<String> getTags() {
		// Not needed in this project
		return null;
	}

	@Override
	public ITestSetup getTestSetup() {
		// Not needed in this project
		return null;
	}

	@Override
	public ITestStepContainer getTestStepContainer(String name) {

		for (TestStepContainer t : eObject.getTestStepContainerList()) {
			if (t instanceof TestCase) {
				if (t.getName().contentEquals(name)) {
					TestCaseImpl testCase = new TestCaseImpl((TestCase) t);
					testCase.setParent(this);
					return testCase;
				}
			} else {
				if (t.getName().contentEquals(name)) {
					TestSetupImpl testCase = new TestSetupImpl((TestSetup) t);
					testCase.setParent(this);
					return testCase;
				}
			}
		}
		return null;
	}

	@Override
	public ArrayList<ITestStepContainer> getTestStepContainerList() {
		// Not needed in this project
		return null;
	}

	@Override
	public void setName(String value) {
		eObject.setName(value);
	}

	@Override
	public void setParent(ITestProject value) {
		this.parent = value;
	}

	@Override
	public void setQualifiedName(String value) {
		// Not needed in this project
	}

	@Override
	public void setStatementList(ArrayList<IStatement> value) {
		// Not needed in this project
	}

	@Override
	public void setTags(ArrayList<String> value) {
		// Not needed in this project
	}

	@Override
	public void setTestSetup(ITestSetup value) {
		// Not needed in this project
	}

	@Override
	public void setTestStepContainerList(ArrayList<ITestStepContainer> value) {
		// Not needed in this project
	}

}
