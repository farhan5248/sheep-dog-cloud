package org.farhan.dsl.asciidoc.impl;

import java.util.ArrayList;

import org.farhan.dsl.lang.IStatement;
import org.farhan.dsl.lang.IStepDefinition;
import org.farhan.dsl.lang.IStepParameters;
import org.farhan.dsl.asciidoc.asciiDoc.Cell;
import org.farhan.dsl.asciidoc.asciiDoc.Row;
import org.farhan.dsl.asciidoc.asciiDoc.Statement;
import org.farhan.dsl.asciidoc.asciiDoc.StepParameters;
import org.farhan.dsl.asciidoc.asciiDoc.Table;

public class StepParametersImpl implements IStepParameters {
	private IStepDefinition parent;
	private StepParameters eObject;

	public StepParametersImpl(StepParameters stepParameters) {
		eObject = stepParameters;
	}

	@Override
	public String getName() {
		return eObject.getName();
	}

	@Override
	public IStepDefinition getParent() {
		return parent;
	}

	@Override
	public ArrayList<IStatement> getStatementList() {
		ArrayList<IStatement> statementList = new ArrayList<IStatement>();
		// TODO why does this need 2 nested statement list calls?
		if (eObject.getStatementList() != null) {
			for (Statement s : eObject.getStatementList().getStatementList()) {
				statementList.add(new StatementImpl(s));
			}
		}
		return statementList;
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
	public void setName(String value) {
		eObject.setName(value);
	}

	@Override
	public void setParent(IStepDefinition value) {
		parent = value;
	}

	@Override
	public void setStatementList(ArrayList<IStatement> value) {
		// TODO should setters like these be used instead of directly accessing the
		// eObject setters?
		// Not needed in this project
	}

	@Override
	public void setTable(ArrayList<ArrayList<String>> value) {
		// Not needed in this project
	}

}
