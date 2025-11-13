package org.farhan.dsl.asciidoc.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.resource.SaveOptions;
import org.farhan.dsl.lang.IStatement;
import org.farhan.dsl.lang.IStepDefinition;
import org.farhan.dsl.lang.IStepObject;
import org.farhan.dsl.lang.ITestProject;
import org.farhan.dsl.asciidoc.asciiDoc.AsciiDocFactory;
import org.farhan.dsl.asciidoc.asciiDoc.Statement;
import org.farhan.dsl.asciidoc.asciiDoc.StepDefinition;
import org.farhan.dsl.asciidoc.asciiDoc.StepObject;

public class StepObjectImpl implements IStepObject {

	private ITestProject parent;
	private StepObject eObject;
	private String qualifiedName;

	public StepObjectImpl(StepObject eObject) {
		this.eObject = eObject;
	}

	@Override
	public IStepDefinition createStepDefinition(String predicate) {
		EList<StepDefinition> list = eObject.getStepDefinitionList();
		list.add(AsciiDocFactory.eINSTANCE.createStepDefinition());

		IStepDefinition stepDefinition = new StepDefinitionImpl(list.getLast());
		stepDefinition.setParent(this);
		stepDefinition.setName(predicate);

		TreeMap<String, StepDefinition> sorted = new TreeMap<String, StepDefinition>();
		for (int i = list.size(); i > 0; i--) {
			sorted.put(list.get(i - 1).getName().toLowerCase(), list.removeLast());
		}
		for (String name : sorted.keySet()) {
			list.add(sorted.get(name));
		}

		return stepDefinition;
	}

	@Override
	public String getName() {
		return eObject.getName();
	}

	@Override
	public ITestProject getParent() {
		return parent;
	}

	@Override
	public String getQualifiedName() {
		return qualifiedName;
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
	public IStepDefinition getStepDefinition(String predicate) {
		for (StepDefinition sd : eObject.getStepDefinitionList()) {
			if (sd.getName().contentEquals(predicate)) {
				StepDefinitionImpl stepDefinition = new StepDefinitionImpl((StepDefinition) sd);
				stepDefinition.setParent(this);
				return stepDefinition;
			}
		}
		return null;
	}

	@Override
	public ArrayList<IStepDefinition> getStepDefinitionList() {
		ArrayList<IStepDefinition> list = new ArrayList<IStepDefinition>();
		for (StepDefinition t : eObject.getStepDefinitionList()) {
			StepDefinitionImpl stepDefinition = new StepDefinitionImpl((StepDefinition) t);
			stepDefinition.setParent(this);
			list.add(stepDefinition);
		}
		return list;
	}

	@Override
	public void setName(String value) {
		eObject.setName(value);
	}

	@Override
	public void setParent(ITestProject value) {
		parent = value;
	}

	@Override
	public void setQualifiedName(String value) {
		this.qualifiedName = value;
	}

	@Override
	public void setStepDefinitionList(ArrayList<IStepDefinition> value) {
		// Not needed in this project
	}

	public String serialize() throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		eObject.eResource().save(os, SaveOptions.newBuilder().format().getOptions().toOptionsMap());
		return os.toString();
	}

}
