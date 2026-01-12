package org.farhan.dsl.asciidoc.impl;

import java.util.ArrayList;

import org.farhan.dsl.lang.ICell;
import org.farhan.dsl.lang.IRow;
import org.farhan.dsl.lang.ITable;
import org.farhan.dsl.asciidoc.asciiDoc.Cell;
import org.farhan.dsl.asciidoc.asciiDoc.Row;
import org.farhan.dsl.asciidoc.asciiDoc.Table;

public class RowImpl implements IRow {

	private TableImpl parent;
	Row eObject;

	public RowImpl(Row row) {
		this.eObject = row;
	}

	@Override
	public ITable getParent() {
		if (parent == null) {
			parent = new TableImpl((Table) eObject.eContainer());
		}
		return parent;
	}

	@Override
	public ArrayList<ICell> getCellList() {
		ArrayList<ICell> cells = new ArrayList<ICell>();
		for (Cell c : eObject.getCellList()) {
			cells.add(new CellImpl(c));
		}
		return cells;
	}

	@Override
	public ICell getCell(int index) {
		throw new UnsupportedOperationException("getCell(int index) is not implemented");
	}

	@Override
	public ICell getCell(String name) {
		throw new UnsupportedOperationException("getCell(String name) is not implemented");
	}

	@Override
	public boolean equals(Object object) {
		return eObject.equals(((RowImpl) object).eObject);
	}

	@Override
	public boolean addCell(ICell value) {
		eObject.getCellList().add(((CellImpl) value).eObject);
		return true;
	}

}
