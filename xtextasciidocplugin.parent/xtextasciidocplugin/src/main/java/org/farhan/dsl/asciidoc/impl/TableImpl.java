package org.farhan.dsl.asciidoc.impl;

import java.util.ArrayList;

import org.farhan.dsl.lang.IRow;
import org.farhan.dsl.lang.ITable;
import org.farhan.dsl.asciidoc.asciiDoc.Row;
import org.farhan.dsl.asciidoc.asciiDoc.StepParameters;
import org.farhan.dsl.asciidoc.asciiDoc.Table;
import org.farhan.dsl.asciidoc.asciiDoc.TestStep;

public class TableImpl implements ITable {

	Table eObject;
	private ArrayList<IRow> rowList;
	private Object parent;

	@Override
	public Object getParent() {
		if (parent == null && eObject != null) {
			if (eObject.eContainer() instanceof TestStep) {
				parent = new TestStepImpl((TestStep) eObject.eContainer());
			} else if (eObject.eContainer() instanceof StepParameters) {
				parent = new StepParametersImpl((StepParameters) eObject.eContainer());
			}
		}
		return parent;
	}

	public TableImpl(Table table) {
		this.eObject = table;
		rowList = new ArrayList<IRow>();
	}

	@Override
	public ArrayList<IRow> getRowList() {
		rowList.clear();
		for (Row row : eObject.getRowList()) {
			rowList.add(new RowImpl(row));
		}
		return rowList;
	}

	@Override
	public IRow getRow(int index) {
		throw new UnsupportedOperationException("getRow(int index) is not implemented");
	}

	@Override
	public boolean addRow(IRow value) {
		eObject.getRowList().add(((RowImpl) value).eObject);
		return true;
	}

}
