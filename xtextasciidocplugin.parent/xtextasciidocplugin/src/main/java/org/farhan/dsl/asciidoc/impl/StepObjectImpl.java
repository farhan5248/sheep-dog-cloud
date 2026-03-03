package org.farhan.dsl.asciidoc.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.resource.SaveOptions;
import org.farhan.dsl.grammar.IDescription;
import org.farhan.dsl.grammar.ILine;
import org.farhan.dsl.grammar.IStepDefinition;
import org.farhan.dsl.grammar.IStepObject;
import org.farhan.dsl.grammar.ITestProject;
import org.farhan.dsl.grammar.SheepDogFactory;
import org.farhan.dsl.asciidoc.asciiDoc.StepDefinition;
import org.farhan.dsl.asciidoc.asciiDoc.StepObject;

public class StepObjectImpl implements IStepObject {

	private ITestProject parent;
	StepObject eObject;
	private String qualifiedName;

	public StepObjectImpl(StepObject eObject) {
		this.eObject = eObject;
	}

	@Override
	public String getName() {
		return eObject.getName();
	}

	@Override
	public ITestProject getParent() {
		if (parent == null) {
			parent = SheepDogFactory.instance.createTestProject();
		}
		return parent;
	}

	@Override
	public String getFullName() {
		return qualifiedName;
	}

	@Override
	public IDescription getDescription() {
		if (eObject.getDescription() != null) {
			return new DescriptionImpl(eObject.getDescription());
		}
		return null;
	}

	@Override
	public IStepDefinition getStepDefinition(String stepDefinitionName) {
		for (StepDefinition sd : eObject.getStepDefinitionList()) {
			if (sd.getName().contentEquals(stepDefinitionName)) {
				StepDefinitionImpl stepDefinition = new StepDefinitionImpl((StepDefinition) sd);
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
			list.add(stepDefinition);
		}
		return list;
	}

	@Override
	public void setName(String value) {
		eObject.setName(value);
	}

	@Override
	public void setFullName(String value) {
		this.qualifiedName = value;
		String extension = org.farhan.dsl.grammar.SheepDogFactory.instance.createTestProject().getFileExtension();
		eObject.setName((new File(qualifiedName)).getName().replaceFirst(extension + "$", ""));
	}

	@Override
	public IStepDefinition getStepDefinition(int index) {
		throw new UnsupportedOperationException("getStepDefinition(int index) is not implemented");
	}

	@Override
	public String getContent() throws Exception {
		Resource theResource = new ResourceSetImpl().createResource(URI.createFileURI("tmpFile.asciidoc"));
		theResource.getContents().add(eObject);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		theResource.save(os, SaveOptions.newBuilder().format().getOptions().toOptionsMap());
		return os.toString();
	}

	@Override
	public void setContent(String text) throws Exception {
		if (!text.isEmpty()) {
			Resource theResource = new ResourceSetImpl().createResource(URI.createFileURI("tmpFile.asciidoc"));
			theResource.load(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)), Collections.EMPTY_MAP);
			eObject = (StepObject) theResource.getContents().get(0);
		}
	}

	@Override
	public void setDescription(IDescription value) {
		eObject.setDescription(((DescriptionImpl) value).eObject);
	}

	@Override
	public boolean addLine(ILine value) {
		org.farhan.dsl.asciidoc.asciiDoc.Description list = eObject.getDescription();
		if (list == null) {
			list = org.farhan.dsl.asciidoc.asciiDoc.AsciiDocFactory.eINSTANCE.createDescription();
			eObject.setDescription(list);
		}
		list.getLineList().add(((LineImpl) value).eObject);
		return true;
	}

	@Override
	public boolean addStepDefinition(IStepDefinition value) {
		EList<StepDefinition> unsortedList = eObject.getStepDefinitionList();
		TreeMap<String, StepDefinition> sortedMap = new TreeMap<String, StepDefinition>();
		StepDefinition aStepDefinition = ((StepDefinitionImpl) value).eObject;
		sortedMap.put(aStepDefinition.getName(), aStepDefinition);
		for (StepDefinition sd : unsortedList) {
			sortedMap.put(sd.getName(), sd);
		}
		unsortedList.clear();
		for (String key : sortedMap.keySet()) {
			unsortedList.add(sortedMap.get(key));
		}
		return true;
	}

}
