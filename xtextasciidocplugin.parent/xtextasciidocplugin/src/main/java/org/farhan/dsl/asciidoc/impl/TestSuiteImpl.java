package org.farhan.dsl.asciidoc.impl;

import java.util.ArrayList;

import org.farhan.dsl.lang.IDescription;
import org.farhan.dsl.lang.ILine;
import org.farhan.dsl.asciidoc.asciiDoc.Description;
import org.farhan.dsl.lang.ITestCase;
import org.farhan.dsl.lang.ITestProject;
import org.farhan.dsl.lang.ITestSetup;
import org.farhan.dsl.lang.ITestStepContainer;
import org.farhan.dsl.lang.ITestSuite;
import org.farhan.dsl.lang.SheepDogFactory;
import org.farhan.dsl.asciidoc.asciiDoc.TestCase;
import org.farhan.dsl.asciidoc.asciiDoc.TestSetup;
import org.farhan.dsl.asciidoc.asciiDoc.TestStepContainer;
import org.farhan.dsl.asciidoc.asciiDoc.TestSuite;

public class TestSuiteImpl implements ITestSuite {

	private ITestProject parent;
	TestSuite eObject;

	public TestSuiteImpl(TestSuite testSuite) {
		this.eObject = testSuite;
	}

	@Override
	public boolean addLine(ILine value) {
		Description list = eObject.getDescription();
		if (list == null) {
			list = org.farhan.dsl.asciidoc.asciiDoc.AsciiDocFactory.eINSTANCE.createDescription();
			eObject.setDescription(list);
		}
		list.getLineList().add(((LineImpl) value).eObject);
		return true;
	}

	@Override
	public boolean addTestCase(ITestCase value) {
		eObject.getTestStepContainerList().add(((TestCaseImpl) value).eObject);
		return true;
	}

	@Override
	public boolean addTestSetup(ITestSetup value) {
		eObject.getTestStepContainerList().add(0, ((TestSetupImpl) value).eObject);
		return true;
	}

	@Override
	public String getContent() {
		throw new UnsupportedOperationException("getContent() is not implemented");
	}

	@Override
	public String getName() {
		return eObject.getName();
	}

	@Override
	public String getNameLong() {
		throw new UnsupportedOperationException("getNameLong() is not implemented");
	}

	@Override
	public ITestProject getParent() {
		if (parent == null) {
			parent = SheepDogFactory.instance.createTestProject();
		}
		return parent;
	}

	@Override
	public IDescription getDescription() {
		if (eObject.getDescription() != null) {
			return new DescriptionImpl(eObject.getDescription());
		}
		return null;
	}

	@Override
	public ITestStepContainer getTestStepContainer(int index) {
		return new TestStepContainerImpl(eObject.getTestStepContainerList().get(index));
	}

	@Override
	public ITestStepContainer getTestStepContainer(String name) {

		for (TestStepContainer t : eObject.getTestStepContainerList()) {
			if (t instanceof TestCase) {
				if (t.getName().contentEquals(name)) {
					TestCaseImpl testCase = new TestCaseImpl((TestCase) t);
					return testCase;
				}
			} else {
				if (t.getName().contentEquals(name)) {
					TestSetupImpl testCase = new TestSetupImpl((TestSetup) t);
					return testCase;
				}
			}
		}
		return null;
	}

	@Override
	public ArrayList<ITestStepContainer> getTestStepContainerList() {
		ArrayList<ITestStepContainer> list = new ArrayList<ITestStepContainer>();
		for (TestStepContainer tsc : eObject.getTestStepContainerList()) {
			TestStepContainerImpl tsci = new TestStepContainerImpl(tsc);
			list.add(tsci);
		}
		return list;
	}

	@Override
	public void setContent(String text) {
		throw new UnsupportedOperationException("setContent(String text) is not implemented");
	}

	@Override
	public void setName(String value) {
		eObject.setName(value);
	}

	@Override
	public void setNameLong(String value) {
		throw new UnsupportedOperationException("setNameLong(String value) is not implemented");
	}

	public void setParent(ITestProject parent) {
		this.parent = parent;
	}

}
