package org.farhan.dsl.asciidoc.impl;

import java.util.ArrayList;

import org.farhan.dsl.lang.ITestCase;
import org.farhan.dsl.lang.ITestData;
import org.farhan.dsl.asciidoc.asciiDoc.TestCase;

public class TestCaseImpl extends TestStepContainerImpl implements ITestCase {

	public TestCaseImpl(TestCase testCase) {
		super(testCase);
	}

	@Override
	public ArrayList<String> getTags() {
		// Not needed in this project
		return null;
	}

	@Override
	public ArrayList<ITestData> getTestDataList() {
		// Not needed in this project
		return null;
	}

	@Override
	public void setTags(ArrayList<String> value) {
		// Not needed in this project
	}

	@Override
	public void setTestDataList(ArrayList<ITestData> value) {
		// Not needed in this project
	}

}
