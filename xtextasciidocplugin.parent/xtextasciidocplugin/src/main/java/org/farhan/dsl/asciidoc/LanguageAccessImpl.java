package org.farhan.dsl.asciidoc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
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
import org.eclipse.xtext.resource.SaveOptions;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LanguageAccessImpl {

	private static final Logger logger = LoggerFactory.getLogger(LanguageAccessImpl.class);
	public final static String STEP_PARAMETER_TEXT = "| Content";

	TestStep step;

	// Temp hack to support LSP code generation
	OutputStream os;
	String fileName;

	public LanguageAccessImpl(TestStep step) {
		this.step = step;
		this.os = null;
	}

	public LanguageAccessImpl(TestStep step, OutputStream os) {
		this.os = os;
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
		// stepObject.setName(TestStepNameHelper.getObject(getStepName()));
		return stepObject;
	}

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

	private String getName(TestStep eObject) {
		String name = "";
		name += eObject.getStepObjectName() != null ? eObject.getStepObjectName().trim() : "";
		name += eObject.getStepDefinitionName() != null ? " " + eObject.getStepDefinitionName().trim() : "";
		return name;
	}

	public ArrayList<Object> getAllSteps() {
		TestStepContainer as = (TestStepContainer) step.eContainer();
		ArrayList<Object> stepNames = new ArrayList<Object>();
		for (TestStep s : as.getTestStepList()) {
			if (getName(s) != null) {
				stepNames.add(s);
			}
		}
		return stepNames;
	}

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

	public String getFileExtension() {
		return ".asciidoc";
	}

	public ArrayList<String> getFiles() throws Exception {

		File folder = new File(getProjectPath() + getOutputDirPath());
		ArrayList<String> components = new ArrayList<String>();
		for (File ir : folder.listFiles()) {
			components.add(ir.getName());
		}
		return components;
	}

	public ArrayList<String> getFilesRecursively(String component) throws Exception {
		ArrayList<String> components = new ArrayList<String>();
		for (String stepDefObjectResource : getFiles(new File(getProjectPath() + getOutputDirPath() + component))) {
			stepDefObjectResource = stepDefObjectResource.replace(File.separator, "/");
			components.add(stepDefObjectResource.replaceFirst(".*" + component + "/", component + "/"));
		}
		return components;
	}

	private ArrayList<String> getFiles(File folder) throws Exception {
		ArrayList<String> files = new ArrayList<String>();
		if (folder.exists()) {
			for (File r : folder.listFiles()) {
				if (!r.isFile()) {
					files.addAll(getFiles(r));
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

	public ArrayList<Object> getPreviousSteps() {
		// TODO add a test to make sure the previous steps only are returned
		TestStepContainer as = (TestStepContainer) step.eContainer();
		ArrayList<Object> steps = new ArrayList<Object>();
		for (TestStep s : as.getTestStepList()) {
			if (s.equals(step)) {
				break;
			} else {
				if (getName(s) != null) {
					steps.add(s);
				}
			}
		}
		return steps;
	}

	private URI getObjectURI(String objectQualifiedName) {
		// TODO this is so not safe to run on Ubuntu
		String objectPath = getProjectPath() + getOutputDirPath() + objectQualifiedName.replace("/", File.separator);
		URI uri = URI.createFileURI(objectPath);
		return uri;
	}

	private String getOutputDirPath() {
		// TODO fix output config
		// AsciiDocOutputConfigurationProvider.stepDefsOutput.getOutputDirectory();
		return "src/test/resources/asciidoc/stepdefs/".replace("/", File.separator);
	}

	private String getProjectPath() {
		String uriPath = getStepResource().getURI().toFileString().replace(File.separator, "/");
		String projectPath = uriPath.split("src/test/resources/asciidoc/specs/")[0].replace("/", File.separator);
		return projectPath;
	}

	public Object getStep() {
		return step;
	}

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

	public List<?> getStepDefinitionParameters(Object stepDefinition) {
		StepDefinition so = (StepDefinition) stepDefinition;
		return so.getStepParameterList();
	}

	public String getStepDefinitionParametersDocumentation(Object parameters) {
		StepParameters e = (StepParameters) parameters;

		String description = "";
		for (Statement s : e.getStatementList().getStatementList()) {
			description += s.getName() + "\n";
		}
		return description;
	}

	public String getStepDefinitionParametersString(Object parameters) {
		StepParameters e = (StepParameters) parameters;
		return cellsToString(e.getTable().getRowList().get(0).getCellList(), true);
	}

	public EList<?> getStepDefinitions(Object stepObject) {
		return ((StepObject) stepObject).getStepDefinitionList();
	}

	public String getStepName() {
		return getName(step) != null ? getName(step) : "";
	}

	public String getStepName(Object step) {
		return getName((TestStep) step);
	}

	public Object getStepObject(String objectQualifiedName) throws Exception {
		Resource resource = new ResourceSetImpl().createResource(getObjectURI(objectQualifiedName));
		if (new ResourceSetImpl().getURIConverter().exists(resource.getURI(), null)) {
			resource.load(new HashMap());
			return resource.getContents().get(0);
		} else {
			return null;
		}
	}

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

	public void saveObject(Object theObject, Map<Object, Object> options) throws Exception {
		Resource resource = ((EObject) theObject).eResource();
		if (os == null) {
			resource.save(options);
		} else {
			resource.save(os, SaveOptions.newBuilder().format().getOptions().toOptionsMap());
			fileName = resource.getURI().toString();
		}
	}

	public String getStepObjectContent() {
		// This is a temp hack until I change how code generation works to support LSP.
		return os.toString();
	}

	public String getStepObjectFileName() {
		return fileName;
	}

}
