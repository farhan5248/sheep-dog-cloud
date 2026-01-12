package org.farhan.dsl.asciidoc.impl;

import java.util.ArrayList;

import org.farhan.dsl.lang.IStatement;
import org.farhan.dsl.lang.IStepDefinition;
import org.farhan.dsl.lang.IStepParameters;
import org.farhan.dsl.lang.ITable;
import org.farhan.dsl.asciidoc.asciiDoc.Statement;
import org.farhan.dsl.asciidoc.asciiDoc.StepParameters;
import org.farhan.dsl.asciidoc.asciiDoc.StepDefinition;

public class StepParametersImpl implements IStepParameters {
	private StepDefinitionImpl parent;
	StepParameters eObject;

	public StepParametersImpl(StepParameters stepParameters) {
		eObject = stepParameters;
	}

	@Override
	public String getName() {
		return eObject.getName();
	}

	@Override
	public IStepDefinition getParent() {
		if (parent == null) {
			parent = new StepDefinitionImpl((StepDefinition) eObject.eContainer());
		}
		return parent;
	}

	@Override
	public ArrayList<IStatement> getStatementList() {
		ArrayList<IStatement> statementList = new ArrayList<IStatement>();
		if (eObject.getStatementList() != null) {
			for (Statement s : eObject.getStatementList().getStatementList()) {
				statementList.add(new StatementImpl(s));
			}
		}
		return statementList;
	}

	@Override
	public ITable getTable() {
		return new TableImpl(eObject.getTable());
	}

	@Override
	public void setName(String value) {
		eObject.setName(value);
	}

	@Override
	public void setTable(ITable value) {
		throw new UnsupportedOperationException("setTable(ITable value) is not implemented");
	}

	@Override
	public String getNameLong() {
		throw new UnsupportedOperationException("getNameLong() is not implemented");
	}

	@Override
	public IStatement getStatement(int index) {
		throw new UnsupportedOperationException("getStatement(int index) is not implemented");
	}

	@Override
	public IStatement getStatement(String name) {
		throw new UnsupportedOperationException("getStatement(String name) is not implemented");
	}

	@Override
	public boolean addStatement(IStatement value) {
		eObject.getStatementList().getStatementList().add(((StatementImpl) value).eObject);
		return true;
	}

}
