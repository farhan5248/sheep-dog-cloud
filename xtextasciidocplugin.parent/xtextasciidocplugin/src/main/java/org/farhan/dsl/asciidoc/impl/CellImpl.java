package org.farhan.dsl.asciidoc.impl;

import org.farhan.dsl.lang.ICell;
import org.farhan.dsl.lang.IRow;
import org.farhan.dsl.asciidoc.asciiDoc.Cell;
import org.farhan.dsl.asciidoc.asciiDoc.Row;

public class CellImpl implements ICell {

	private RowImpl parent;
	Cell eObject;

	public CellImpl(Cell cell) {
		this.eObject = cell;
	}

	@Override
	public String getName() {
		return eObject.getName();
	}

	@Override
	public void setName(String value) {
		eObject.setName(value);
	}

	@Override
	public IRow getParent() {
		if (parent == null) {
			parent = new RowImpl((Row) eObject.eContainer());
		}
		return parent;
	}

}
