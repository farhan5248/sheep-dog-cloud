package org.farhan.dsl.asciidoc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.farhan.dsl.asciidoc.asciiDoc.TestStepContainer;
import org.farhan.dsl.asciidoc.asciiDoc.TestSetup;
import org.farhan.dsl.asciidoc.asciiDoc.Cell;
import org.farhan.dsl.asciidoc.asciiDoc.AsciiDocFactory;
import org.farhan.dsl.asciidoc.asciiDoc.TestSuite;
import org.farhan.dsl.asciidoc.asciiDoc.Table;
import org.farhan.dsl.asciidoc.asciiDoc.Row;
import org.farhan.dsl.asciidoc.asciiDoc.Statement;
import org.farhan.dsl.asciidoc.asciiDoc.TestStep;
import org.farhan.dsl.asciidoc.asciiDoc.StepDefinition;
import org.farhan.dsl.asciidoc.asciiDoc.StepObject;
import org.farhan.dsl.asciidoc.asciiDoc.StepParameters;
import org.farhan.dsl.common.*;

public class LanguageAccessImpl implements ILanguageAccess {

	public final static String STEP_PARAMETER_TEXT = "| Content";

	TestStep step;

	public LanguageAccessImpl(TestStep step) {
		this.step = step;
	}

	private String cellsToString(List<Cell> cells, boolean sorted) {
		String cellsAsString = "";
		List<String> sortedCells = new ArrayList<String>();
		for (Cell cell : cells) {
			if (cell.getName() != null) {
				sortedCells.add(cell.getName());
			}
		}
		if (sorted) {
			Collections.sort(sortedCells);
		}
		for (String cell : sortedCells) {
			cellsAsString += "| " + cell;
		}
		return cellsAsString;
	}

	@Override
	public Object createStepDefinition(Object stepObject, String predicate) {
		StepDefinition stepDefinition;
		stepDefinition = AsciiDocFactory.eINSTANCE.createStepDefinition();
		stepDefinition.setName(predicate);
		EList<StepDefinition> list = ((StepObject) stepObject).getStepDefinitionList();
		list.add(stepDefinition);

		TreeMap<String, StepDefinition> sorted = new TreeMap<String, StepDefinition>();
		for (int i = list.size(); i > 0; i--) {
			sorted.put(list.get(i - 1).getName().toLowerCase(), list.removeLast());
		}
		for (String name : sorted.keySet()) {
			list.add(sorted.get(name));
		}
		return stepDefinition;
	}

	public void createStepDefinitionParameters(Object stepDefinition) {
		StepDefinition so = (StepDefinition) stepDefinition;
		StepParameters parameters = AsciiDocFactory.eINSTANCE.createStepParameters();
		parameters.setName(Integer.toString(so.getStepParameterList().size() + 1));
		so.getStepParameterList().add(parameters);

		Table table = AsciiDocFactory.eINSTANCE.createTable();
		parameters.setTable(table);

		Row row = AsciiDocFactory.eINSTANCE.createRow();
		table.getRowList().add(row);
		if (getHeader() == null) {
			// TODO there's no automated test for this..
			// This is a docstring and also the abuse of this method :P
			Cell cell = AsciiDocFactory.eINSTANCE.createCell();
			cell.setName("Content");
			row.getCellList().add(cell);
		} else {
			for (Cell srcCell : getHeader()) {
				Cell cell = AsciiDocFactory.eINSTANCE.createCell();
				cell.setName(srcCell.getName());
				row.getCellList().add(cell);
			}
		}
	}

	private Object createStepObject() {
		StepObject stepObject = AsciiDocFactory.eINSTANCE.createStepObject();
		stepObject.setName(TestStepNameHelper.getObject(getStepName()));
		return stepObject;
	}

	@Override
	public Object createStepObject(String objectQualifiedName) throws Exception {
		Object stepObject = getStepObject(objectQualifiedName);
		if (stepObject != null) {
			return stepObject;
		} else {
			Resource theResource = new ResourceSetImpl().createResource(getObjectURI(objectQualifiedName));
			theResource.getContents().add((EObject) createStepObject());
			return theResource.getContents().get(0);
		}
	}

	@Override
	public ArrayList<Object> getAllSteps() {
		TestStepContainer as = (TestStepContainer) step.eContainer();
		ArrayList<Object> stepNames = new ArrayList<Object>();
		for (TestStep s : as.getTestStepList()) {
			if (s.getName() != null) {
				stepNames.add(s);
			}
		}
		return stepNames;
	}

	@Override
	public ArrayList<Object> getBackgroundSteps() {
		ArrayList<Object> steps = new ArrayList<Object>();
		TestSuite feature = (TestSuite) step.eContainer().eContainer();
		if (feature.getTestStepContainerList().getFirst() instanceof TestSetup) {
			for (TestStep s : feature.getTestStepContainerList().getFirst().getTestStepList()) {
				steps.add(s);
			}
		}
		return steps;
	}

	@Override
	public String getFileExtension() {
		return ".asciidoc";
	}

	@Override
	public ArrayList<String> getFiles() throws Exception {

		File folder = new File(getProjectPath() + getOutputDirPath());
		ArrayList<String> components = new ArrayList<String>();
		for (File ir : folder.listFiles()) {
			components.add(ir.getName());
		}
		return components;
	}

	@Override
	public ArrayList<String> getFilesRecursively(String component) throws Exception {
		ArrayList<String> components = new ArrayList<String>();
		for (String stepDefObjectResource : getFolderResources(new File(getProjectPath() + getOutputDirPath()))) {
			// ([^\/]+)\/([^\/]+)\/(.*).feature group 3
			components.add(stepDefObjectResource.replaceFirst(".*" + component + "/", component + "/"));
		}
		return components;
	}

	private ArrayList<String> getFolderResources(File folder) throws Exception {
		ArrayList<String> files = new ArrayList<String>();
		if (folder.exists()) {
			for (File r : folder.listFiles()) {
				if (!r.isFile()) {
					files.addAll(getFolderResources(r));
				} else {
					files.add(r.getAbsolutePath());
				}
			}
		}
		return files;
	}

	private List<Cell> getHeader() {
		Table table = step.getTable();
		if (table != null) {
			if (table.getRowList().size() > 0) {
				// TODO if this is sorted then there's no need to sort it in cellToString
				return table.getRowList().get(0).getCellList();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private URI getObjectURI(String objectQualifiedName) {
		return URI.createFileURI(getProjectPath() + getOutputDirPath() + objectQualifiedName);
	}

	private String getOutputDirPath() {
		// TODO fix output config
		// AsciiDocOutputConfigurationProvider.stepDefsOutput.getOutputDirectory();
		return "src/test/resources/asciidoc/stepdefs/";
	}

	public ArrayList<Object> getPreviousSteps() {
		// TODO add a test to make sure the previous steps only are returned
		TestStepContainer as = (TestStepContainer) step.eContainer();
		ArrayList<Object> steps = new ArrayList<Object>();
		for (TestStep s : as.getTestStepList()) {
			if (s.equals(step)) {
				break;
			} else {
				if (s.getName() != null) {
					steps.add(s);
				}
			}
		}
		return steps;
	}

	private String getProjectPath() {
		return getStepResource().getURI().path().split("src/test/resources/asciidoc/specs/")[0];
	}

	public Object getStep() {
		return step;
	}

	@Override
	public String getStepDefinitionDescription(Object stepDefinition) {

		String description = "";
		if (stepDefinition instanceof StepDefinition) {
			StepDefinition as = (StepDefinition) stepDefinition;
			for (Statement s : as.getStatementList()) {
				description += s.getName() + "\n";
			}
		}
		return description;
	}

	public String getStepDefinitionName(Object stepDefinition) {
		return ((StepDefinition) stepDefinition).getName();
	}

	@Override
	public List<?> getStepDefinitionParameters(Object stepDefinition) {
		StepDefinition so = (StepDefinition) stepDefinition;
		return so.getStepParameterList();
	}

	@Override
	public String getStepDefinitionParametersDocumentation(Object parameters) {
		StepParameters e = (StepParameters) parameters;

		String description = "";
		for (Statement s : e.getStatementList().getStatementList()) {
			description += s.getName() + "\n";
		}
		return description;
	}

	@Override
	public String getStepDefinitionParametersString(Object parameters) {
		StepParameters e = (StepParameters) parameters;
		return cellsToString(e.getTable().getRowList().get(0).getCellList(), true);
	}

	public EList<?> getStepDefinitions(Object stepObject) {
		return ((StepObject) stepObject).getStepDefinitionList();
	}

	public String getStepName() {
		return step.getName();
	}

	@Override
	public String getStepName(Object step) {
		return ((TestStep) step).getName();
	}

	@Override
	public Object getStepObject(String objectQualifiedName) throws Exception {
		Resource resource = new ResourceSetImpl().createResource(getObjectURI(objectQualifiedName));
		if (new ResourceSetImpl().getURIConverter().exists(resource.getURI(), null)) {
			resource.load(new HashMap());
			return resource.getContents().get(0);
		} else {
			return null;
		}
	}

	@Override
	public String getStepObjectDescription(String objectQualifiedName) throws Exception {
		StepObject stepObject = (StepObject) getStepObject(objectQualifiedName);
		String description = "";
		if (stepObject != null) {
			for (Statement s : stepObject.getStatementList()) {
				description += s.getName() + "\n";
			}
		}
		return description;
	}

	@Override
	public String getStepParametersString() {
		List<Cell> header = getHeader();
		if (header == null) {
			// TODO it's better to create a single cell with the content than hardcoding it
			// this way
			return STEP_PARAMETER_TEXT;
		} else {
			return cellsToString(getHeader(), true);
		}
	}

	public Resource getStepResource() {
		return step.eResource();
	}

	public boolean hasParameters(Object stepDefinition) {
		// TODO why is stepDefinition passed in when it's not used?
		if (step.getTable() != null) {
			if (step.getTable().getRowList().size() > 0) {
				return true;
			}
		}
		if (step.getText() != null) {
			return true;
		}
		return false;
	}

	@Override
	public void saveObject(Object theObject, Map<Object, Object> options) throws Exception {
		((EObject) theObject).eResource().save(options);
	}

}
