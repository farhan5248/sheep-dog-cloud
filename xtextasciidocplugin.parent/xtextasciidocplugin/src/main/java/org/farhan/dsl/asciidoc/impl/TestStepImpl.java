package org.farhan.dsl.asciidoc.impl;

import org.farhan.dsl.lang.ITable;
import org.farhan.dsl.lang.ITestStep;
import org.farhan.dsl.lang.ITestStepContainer;
import org.farhan.dsl.lang.IText;
import org.farhan.dsl.lang.TestStepUtility;
import org.farhan.dsl.asciidoc.asciiDoc.TestCase;
import org.farhan.dsl.asciidoc.asciiDoc.TestSetup;
import org.farhan.dsl.asciidoc.asciiDoc.TestStep;

public class TestStepImpl implements ITestStep {

	TestStep eObject;
	private TestStepContainerImpl parent;

	public TestStepImpl(TestStep testStep) {
		this.eObject = testStep;
		parent = null;
	}

	@Override
	public String getName() {
		String name = "";
		name += eObject.getStepObjectName() != null ? eObject.getStepObjectName().trim() : "";
		name += eObject.getStepDefinitionName() != null ? " " + eObject.getStepDefinitionName().trim() : "";
		return name;
	}

	@Override
	public String getNameLong() {
		try {
			return TestStepUtility.getNameLong(this);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public ITestStepContainer getParent() {
		if (parent == null) {
			if (eObject.eContainer() instanceof TestCase) {
				parent = new TestCaseImpl((TestCase) eObject.eContainer());
			} else {
				parent = new TestSetupImpl((TestSetup) eObject.eContainer());
			}
		}
		return parent;

	}

	@Override
	public ITable getTable() {
		if (eObject.getTable() != null) {
			return new TableImpl(eObject.getTable());
		} else {
			return null;
		}
	}

	@Override
	public IText getText() {
		if (eObject.getText() != null) {
			TextImpl text = new TextImpl(eObject.getText());
			// eObject.getText().getName().replaceFirst("^----\n",
			// "").replaceFirst("\n----$", "").replace("\\----",----")

			return text;
		} else {
			return null;
		}
	}

	@Override
	public void setTable(ITable value) {
		throw new UnsupportedOperationException("setTable(ITable value) is not implemented");
	}

	@Override
	public void setText(IText value) {
		throw new UnsupportedOperationException("setText(IText value) is not implemented");
	}

	public boolean equals(Object object) {
		return eObject.equals(((TestStepImpl) object).eObject);
	}

	@Override
	public String getStepObjectName() {
		return eObject.getStepObjectName() != null ? eObject.getStepObjectName().trim() : "";
	}

	@Override
	public String getStepDefinitionName() {
		return eObject.getStepDefinitionName() != null ? eObject.getStepDefinitionName().trim() : "";
	}

	@Override
	public void setStepObjectName(String value) {
		eObject.setStepObjectName(value);
	}

	@Override
	public void setStepDefinitionName(String value) {
		eObject.setStepDefinitionName(value);
	}
}
