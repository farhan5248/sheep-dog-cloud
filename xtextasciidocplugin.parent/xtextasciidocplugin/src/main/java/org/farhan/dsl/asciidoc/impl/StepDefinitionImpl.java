package org.farhan.dsl.asciidoc.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.emf.common.util.EList;
import org.farhan.dsl.lang.ICell;
import org.farhan.dsl.lang.IRow;
import org.farhan.dsl.lang.IStatement;
import org.farhan.dsl.lang.IStepDefinition;
import org.farhan.dsl.lang.IStepObject;
import org.farhan.dsl.lang.IStepParameters;
import org.farhan.dsl.lang.IText;
import org.farhan.dsl.lang.StepDefinitionUtility;
import org.farhan.dsl.asciidoc.asciiDoc.Cell;
import org.farhan.dsl.asciidoc.asciiDoc.Statement;
import org.farhan.dsl.asciidoc.asciiDoc.StepDefinition;
import org.farhan.dsl.asciidoc.asciiDoc.StepObject;
import org.farhan.dsl.asciidoc.asciiDoc.StepParameters;

public class StepDefinitionImpl implements IStepDefinition {

	private static Logger logger = LoggerFactory.getLogger(StepDefinitionImpl.class);
	private StepObjectImpl parent;
	StepDefinition eObject;

	public StepDefinitionImpl(StepDefinition value) {
		this.eObject = value;
	}

	private String cellsToString(List<ICell> arrayList) {
		String cellsAsString = "";
		List<String> sortedCells = new ArrayList<String>();
		for (ICell cell : arrayList) {
			sortedCells.add(cell.getName());
		}
		Collections.sort(sortedCells);
		for (String cell : sortedCells) {
			cellsAsString += "| " + cell;
		}
		return cellsAsString.trim();
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
	public IStepObject getParent() {
		if (parent == null) {
			parent = new StepObjectImpl((StepObject) eObject.eContainer());
		}
		return parent;
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
	public ArrayList<IStatement> getStatementList() {
		ArrayList<IStatement> statementList = new ArrayList<IStatement>();
		for (Statement s : eObject.getStatementList()) {
			statementList.add(new StatementImpl(s));
		}
		return statementList;
	}

	@Override
	public ArrayList<IStepParameters> getStepParameterList() {
		ArrayList<IStepParameters> list = new ArrayList<IStepParameters>();
		for (StepParameters t : eObject.getStepParameterList()) {
			StepParametersImpl stepParameters = new StepParametersImpl((StepParameters) t);
			list.add(stepParameters);
		}
		return list;
	}

	@Override
	public IStepParameters getStepParameters(int index) {
		throw new UnsupportedOperationException("getStepParameters(int index) is not implemented");
	}

	@Override
	public IStepParameters getStepParameters(IRow headers) {
		return StepDefinitionUtility.getStepParameters(this, cellsToString(headers.getCellList()));
	}

	@Override
	public IStepParameters getStepParameters(IText value) {
		return StepDefinitionUtility.getStepParameters(this, "| Content");
	}

	@Override
	public IStepParameters getStepParameters(String name) {
		throw new UnsupportedOperationException("getStepParameters(String name) is not implemented");
	}

	@Override
	public void setName(String value) {
		eObject.setName(value);
	}

	@Override
	public boolean addStatement(IStatement value) {
		eObject.getStatementList().add(((StatementImpl) value).eObject);
		return true;
	}

	@Override
	public boolean addStepParameters(IStepParameters value) {

		EList<StepParameters> unsortedList = eObject.getStepParameterList();
		TreeMap<String, StepParameters> sortedMap = new TreeMap<String, StepParameters>();
		StepParameters aStepParameter = ((StepParametersImpl) value).eObject;
		sortedMap.put(getKey(aStepParameter), aStepParameter);
		for (StepParameters sp : unsortedList) {
			sortedMap.put(getKey(sp), sp);
		}
		unsortedList.clear();
		for (String key : sortedMap.keySet()) {
			unsortedList.add(sortedMap.get(key));
			unsortedList.getLast().setName(String.valueOf(unsortedList.size()));
		}
		return true;
	}

	private String getKey(StepParameters aStepParameter) {
		List<Cell> arrayList = aStepParameter.getTable().getRowList().getFirst().getCellList();
		String cellsAsString = "";
		List<String> sortedCells = new ArrayList<String>();
		for (Cell cell : arrayList) {
			sortedCells.add(cell.getName());
		}
		Collections.sort(sortedCells);
		for (String cell : sortedCells) {
			cellsAsString += "| " + cell;
		}
		return cellsAsString.trim();
	}

}
