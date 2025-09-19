package org.farhan.mbt.service.cucumber;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.xtext.impl.RuleCallImpl;
import org.eclipse.xtext.nodemodel.impl.CompositeNodeWithSemanticElement;
import org.eclipse.xtext.resource.SaveOptions;
import org.farhan.dsl.cucumber.cucumber.AbstractScenario;
import org.farhan.dsl.cucumber.cucumber.Background;
import org.farhan.dsl.cucumber.cucumber.Cell;
import org.farhan.dsl.cucumber.cucumber.CucumberFactory;
import org.farhan.dsl.cucumber.cucumber.DocString;
import org.farhan.dsl.cucumber.cucumber.Examples;
import org.farhan.dsl.cucumber.cucumber.ExamplesTable;
import org.farhan.dsl.cucumber.cucumber.Feature;
import org.farhan.dsl.cucumber.cucumber.Line;
import org.farhan.dsl.cucumber.cucumber.Row;
import org.farhan.dsl.cucumber.cucumber.Scenario;
import org.farhan.dsl.cucumber.cucumber.ScenarioOutline;
import org.farhan.dsl.cucumber.cucumber.Statement;
import org.farhan.dsl.cucumber.cucumber.Step;
import org.farhan.dsl.cucumber.cucumber.StepTable;
import org.farhan.dsl.cucumber.cucumber.Tag;
import org.farhan.mbt.core.IConvertibleObject;

public class CucumberFeature implements IConvertibleObject {

	private Feature theFeature;
	private String thePath;

	public CucumberFeature(String thePath) {
		this.thePath = thePath;
		theFeature = CucumberFactory.eINSTANCE.createFeature();
		{
			String[] pathParts = thePath.split("/");
			theFeature.setName(pathParts[pathParts.length - 1].replace(".feature", ""));
		}
	}

	public Background addBackground(String backgroundName) {
		deleteAbstractScenario(backgroundName);
		Background background = CucumberFactory.eINSTANCE.createBackground();
		background.setName(backgroundName);
		theFeature.getAbstractScenarios().add(0, background);
		return background;
	}

	public Statement addBackgroundStatement(Background background, String statement) {
		return addStatement(background.getStatements(), statement);
	}

	public Cell addCell(EList<Cell> cellList, String name) {
		Cell cell = CucumberFactory.eINSTANCE.createCell();
		cell.setName(name);
		cellList.add(cell);
		return cell;
	}

	public DocString addDocString(Step step) {
		DocString docString = CucumberFactory.eINSTANCE.createDocString();
		step.setTheDocString(docString);
		return docString;
	}

	public Examples addExamples(ScenarioOutline scenarioOutline, String examplesName) {
		Examples examples = CucumberFactory.eINSTANCE.createExamples();
		examples.setName(examplesName);
		scenarioOutline.getExamples().add(examples);
		return examples;
	}

	public Statement addExamplesStatement(Examples examples, String statement) {
		return addStatement(examples.getStatements(), statement);
	}

	public ExamplesTable addExamplesTable(Examples examples) {
		ExamplesTable et = CucumberFactory.eINSTANCE.createExamplesTable();
		examples.setTheExamplesTable(et);
		return et;
	}

	public Row addExamplesTableRow(ExamplesTable examplesTable) {
		Row row = addRow(examplesTable.getRows());
		return row;
	}

	public Tag addExamplesTag(Examples examples, String tagName) {
		return addTag(examples.getTags(), tagName);
	}

	public void addFeatureName(String featureName) {
		theFeature.setName(featureName);
	}

	public Statement addFeatureStatement(String statement) {
		return addStatement(theFeature.getStatements(), statement);
	}

	public Tag addFeatureTag(String tagName) {
		return addTag(theFeature.getTags(), tagName);
	}

	public Line addLine(DocString docString, String name) {
		Line line = CucumberFactory.eINSTANCE.createLine();
		// Add hidden text
		line.setName("          " + name);
		docString.getLines().add(line);
		return line;
	}

	public Row addRow(EList<Row> rowList) {
		Row row = CucumberFactory.eINSTANCE.createRow();
		rowList.add(row);
		return row;
	}

	public Scenario addScenario(String scenarioName) {
		int position = deleteAbstractScenario(scenarioName);
		Scenario scenario = CucumberFactory.eINSTANCE.createScenario();
		scenario.setName(scenarioName);
		theFeature.getAbstractScenarios().add(position, scenario);
		return scenario;
	}

	public ScenarioOutline addScenarioOutline(String scenarioOutlineName) {
		int position = deleteAbstractScenario(scenarioOutlineName);
		ScenarioOutline scenarioOutline = CucumberFactory.eINSTANCE.createScenarioOutline();
		scenarioOutline.setName(scenarioOutlineName);
		theFeature.getAbstractScenarios().add(position, scenarioOutline);
		return scenarioOutline;
	}

	public Statement addScenarioOutlineStatement(ScenarioOutline scenarioOutline, String statement) {
		return addStatement(scenarioOutline.getStatements(), statement);
	}

	public Tag addScenarioOutlineTag(ScenarioOutline scenarioOutline, String tagName) {
		return addTag(scenarioOutline.getTags(), tagName);
	}

	public Statement addScenarioStatement(Scenario scenario, String statement) {
		return addStatement(scenario.getStatements(), statement);
	}

	public Tag addScenarioTag(Scenario scenario, String tagName) {
		return addTag(scenario.getTags(), tagName);
	}

	public Statement addStatement(EList<Statement> statements, String statementText) {
		Statement s = CucumberFactory.eINSTANCE.createStatement();
		// TODO need test for this, reverse and then forward engineer any text with a
		// tag in it
		s.setName(statementText.replaceAll("(\\s*)([\\\\]*)@", "$1\\\\@"));
		statements.add(s);
		return s;
	}

	public Step addStep(AbstractScenario abstractScenario, String name) {
		String keyword = name.split(" ")[0];
		Step step = null;
		switch (keyword) {
		case "Given:":
			step = CucumberFactory.eINSTANCE.createGiven();
			break;
		case "When:":
			step = CucumberFactory.eINSTANCE.createWhen();
			break;
		case "Then:":
			step = CucumberFactory.eINSTANCE.createThen();
			break;
		case "And:":
			step = CucumberFactory.eINSTANCE.createAnd();
			break;
		}
		step.setName(name.substring(keyword.length() + 1));
		abstractScenario.getSteps().add(step);
		return step;
	}

	public StepTable addStepTable(Step step) {
		StepTable stepTable = CucumberFactory.eINSTANCE.createStepTable();
		step.setTheStepTable(stepTable);
		return stepTable;
	}

	public Row addStepTableRow(StepTable stepTable) {
		Row row = addRow(stepTable.getRows());
		return row;
	}

	private Tag addTag(EList<Tag> tagList, String tagName) {

		for (Tag t : tagList) {
			if (t.getName().contentEquals(tagName)) {
				return t;
			}
		}
		Tag tag = CucumberFactory.eINSTANCE.createTag();
		tag.setName(tagName);
		tagList.add(tag);
		return tag;
	}

	private String convertStatementsToString(EList<Statement> eList) {
		String contents = "";
		for (Statement s : eList) {
			contents += s.getName() + "\n";
		}
		return contents.trim();
	}

	private int deleteAbstractScenario(String name) {
		EList<AbstractScenario> list = theFeature.getAbstractScenarios();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getName().contentEquals(name)) {
				list.remove(i);
				return i;
			}
		}
		return list.size();
	}

	@Override
	public Object get() {
		return theFeature;
	}

	public EList<AbstractScenario> getAbstractScenarioList() {
		// TODO remove this and return theFeature when its created
		return theFeature.getAbstractScenarios();
	}

	public String getBackgroundDescription(AbstractScenario abstractScenario) {
		return convertStatementsToString(abstractScenario.getStatements());
	}

	public String getBackgroundName(AbstractScenario abstractScenario) {
		return abstractScenario.getName();
	}

	public String getDocString(Step stepSrc) {
		String text = "";
		String indent = "          ";
		for (Line l : stepSrc.getTheDocString().getLines()) {
			if (l.getName() != null) {
				text += "\n" + l.getName().replaceFirst(indent, "");
			} else {
				text += "\n";
			}
		}
		return text.replaceFirst("\n", "");
	}

	public String getExamplesDescription(Examples examples) {
		return convertStatementsToString(examples.getStatements());
	}

	public EList<Examples> getExamplesList(AbstractScenario abstractScenario) {
		ScenarioOutline scenarioOutline = (ScenarioOutline) abstractScenario;
		return scenarioOutline.getExamples();
	}

	public String getExamplesName(Examples examples) {
		return examples.getName();
	}

	public ArrayList<String> getExamplesRow(Examples examples, Row examplesRow) {
		ArrayList<String> row = new ArrayList<String>();
		EList<Cell> header = examples.getTheExamplesTable().getRows().getFirst().getCells();
		for (int i = 0; i < header.size(); i++) {
			row.add(examplesRow.getCells().get(i).getName());
		}
		return row;
	}

	public ArrayList<Row> getExamplesRowList(Examples examples) {
		ArrayList<Row> body = new ArrayList<Row>();
		body.addAll(examples.getTheExamplesTable().getRows());
		body.remove(0);
		return body;
	}

	public ArrayList<String> getExamplesTable(Examples examples) {
		ArrayList<String> header = new ArrayList<String>();
		for (Cell c : examples.getTheExamplesTable().getRows().getFirst().getCells()) {
			header.add(c.getName());
		}
		return header;
	}

	public ArrayList<String> getExamplesTags(Examples examples) {
		ArrayList<String> tags = new ArrayList<String>();
		for (Tag c : examples.getTags()) {
			tags.add(c.getName());
		}
		return tags;
	}

	public String getFeatureDescription() {
		return convertStatementsToString(theFeature.getStatements());
	}

	public String getFeatureName() {
		return theFeature.getName();
	}

	public ArrayList<String> getFeatureTags() {
		return getTags(theFeature.getTags());
	}

	@Override
	public String getPath() {
		return thePath;
	}

	public String getScenarioDescription(AbstractScenario abstractScenario) {
		return convertStatementsToString(abstractScenario.getStatements());
	}

	public String getScenarioName(AbstractScenario abstractScenario) {
		return abstractScenario.getName();
	}

	public String getScenarioOutlineDescription(AbstractScenario abstractScenario) {
		return convertStatementsToString(abstractScenario.getStatements());
	}

	public String getScenarioOutlineName(AbstractScenario abstractScenario) {
		return abstractScenario.getName();
	}

	public ArrayList<String> getScenarioOutlineTags(AbstractScenario abstractScenario) {
		ScenarioOutline scenarioOutline = (ScenarioOutline) abstractScenario;
		return getTags(scenarioOutline.getTags());
	}

	public ArrayList<String> getScenarioTags(AbstractScenario abstractScenario) {
		Scenario scenario = (Scenario) abstractScenario;
		return getTags(scenario.getTags());
	}

	public String getStepKeyword(Step step) {
		CompositeNodeWithSemanticElement keyword = (CompositeNodeWithSemanticElement) step.eAdapters().getFirst();
		RuleCallImpl rc = (RuleCallImpl) keyword.getGrammarElement();
		String keywordString = rc.getRule().getName();
		return keywordString;
	}

	public EList<Step> getStepList(AbstractScenario abstractScenario) {
		return abstractScenario.getSteps();
	}

	public String getStepName(Step step) {
		return getStepKeyword(step) + " " + step.getName();
	}

	public String getStepNameLong(Step stepSrc) {
		return getStepName(stepSrc);
	}

	public ArrayList<ArrayList<String>> getStepTable(Step stepSrc) {
		ArrayList<ArrayList<String>> stepTableRowList = new ArrayList<ArrayList<String>>();
		ArrayList<String> row;
		for (Row r : stepSrc.getTheStepTable().getRows()) {
			row = new ArrayList<String>();
			for (Cell c : r.getCells()) {
				row.add(c.getName());
			}
			stepTableRowList.add(row);
		}
		return stepTableRowList;
	}

	private ArrayList<String> getTags(EList<Tag> tagList) {
		ArrayList<String> tags = new ArrayList<String>();
		for (Tag t : tagList) {
			tags.add(t.getName().replace("@", ""));
		}
		return tags;
	}

	public boolean hasDocString(Step step) {
		return step.getTheDocString() != null;
	}

	public boolean hasStepTable(Step step) {
		return step.getTheStepTable() != null;
	}

	public boolean isBackground(AbstractScenario abstractScenario) {
		return abstractScenario instanceof Background;
	}

	public boolean isScenarioOutline(AbstractScenario abstractScenario) {
		return abstractScenario instanceof ScenarioOutline;
	}

	@Override
	public void parse(String text) throws Exception {
		try {
			if (text.isEmpty()) {
				return;
			}
			URI uri = URI.createFileURI(thePath);
			Resource resource = new ResourceSetImpl().createResource(uri);
			InputStream content = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
			resource.load(content, Collections.EMPTY_MAP);
			theFeature = (Feature) resource.getContents().get(0);
		} catch (Exception e) {
			throw new Exception("There was a problem parsing file: " + thePath);
		}
	}

	public String toString() {
		URI uri = URI.createFileURI(thePath);
		Resource resource = new ResourceSetImpl().createResource(uri);
		resource.getContents().add(theFeature);
		Map<Object, Object> options = SaveOptions.newBuilder().format().getOptions().toOptionsMap();
		OutputStream os = new ByteArrayOutputStream();
		try {
			resource.save(os, options);
		} catch (IOException e) {
			return null;
		}
		return os.toString();
	}

}
