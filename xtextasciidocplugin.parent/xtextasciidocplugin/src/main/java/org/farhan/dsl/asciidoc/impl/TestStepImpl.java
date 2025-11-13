package org.farhan.dsl.asciidoc.impl;

import java.util.ArrayList;

import org.farhan.dsl.lang.ITestStep;
import org.farhan.dsl.lang.ITestStepContainer;
import org.farhan.dsl.asciidoc.asciiDoc.Cell;
import org.farhan.dsl.asciidoc.asciiDoc.Row;
import org.farhan.dsl.asciidoc.asciiDoc.Table;
import org.farhan.dsl.asciidoc.asciiDoc.TestCase;
import org.farhan.dsl.asciidoc.asciiDoc.TestSetup;
import org.farhan.dsl.asciidoc.asciiDoc.TestStep;

public class TestStepImpl implements ITestStep {

	TestStep eObject;
	ITestStepContainer parent;

	public TestStepImpl(TestStep testStep) {
		this.eObject = testStep;
		parent = null;
	}

	@Override
	public String getName() {
		// TODO apply this to the sheepdog plugin as well
		if (eObject.getName() == null) {
			return "";
		} else {
			return eObject.getName();
		}

	}

	@Override
	public String getNameLong() {
		// Not needed in this project
		return null;
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
	public ArrayList<ArrayList<String>> getTable() {
		ArrayList<ArrayList<String>> newTable = new ArrayList<ArrayList<String>>();
		Table table = eObject.getTable();
		if (table != null) {
			for (Row r : table.getRowList()) {
				ArrayList<String> newRow = new ArrayList<String>();
				newTable.add(newRow);
				for (Cell c : r.getCellList()) {
					newRow.add(c.getName());
				}
			}
		}
		return newTable;
	}

	@Override
	public String getText() {
		if (eObject.getText() != null) {
			eObject.getText().getName();
			return eObject.getText().getName().replaceFirst("^----\n", "").replaceFirst("\n----$", "").replace("\\----",
					"----");
		} else {
			return null;
		}
	}

	@Override
	public void setName(String value) {
		// Not needed in this project
	}

	@Override
	public void setNameLong(String value) {
		// Not needed in this project
	}

	@Override
	public void setParent(ITestStepContainer value) {
		this.parent = value;
	}

	@Override
	public void setTable(ArrayList<ArrayList<String>> value) {
		// Not needed in this project
	}

	@Override
	public void setText(String value) {
		// Not needed in this project
	}

}
