package org.farhan.mbt.service.cucumber;

import java.util.ArrayList;

import org.farhan.dsl.cucumber.cucumber.Background;
import org.farhan.dsl.cucumber.cucumber.DocString;
import org.farhan.dsl.cucumber.cucumber.Examples;
import org.farhan.dsl.cucumber.cucumber.ExamplesTable;
import org.farhan.dsl.cucumber.cucumber.Row;
import org.farhan.dsl.cucumber.cucumber.Scenario;
import org.farhan.dsl.cucumber.cucumber.ScenarioOutline;
import org.farhan.dsl.cucumber.cucumber.Step;
import org.farhan.dsl.cucumber.cucumber.StepTable;
import org.farhan.mbt.core.Converter;

import org.farhan.mbt.core.Logger;
import org.farhan.mbt.core.ObjectRepository;
import org.farhan.mbt.core.UMLStepDefinition;
import org.farhan.mbt.core.UMLStepObject;
import org.farhan.mbt.core.UMLTestCase;
import org.farhan.mbt.core.UMLTestData;
import org.farhan.mbt.core.UMLTestSuite;
import org.farhan.mbt.core.UMLTestProject;
import org.farhan.mbt.core.UMLTestSetup;
import org.farhan.mbt.core.UMLTestStep;

public class ConvertUMLToCucumber extends Converter {

	protected UMLTestSuite srcObj;
	protected CucumberFeature tgtObjTestSuite;
	protected CucumberJava tgtObjStepObject;
	protected CucumberPathConverter pathConverter;

	public ConvertUMLToCucumber(String tags, ObjectRepository fa, Logger log) {
		super(tags, fa, log);
	}

	@Override
	public String convertFile(String path, String content) throws Exception {
		initProjects();
		if (path.startsWith(project.getDir(project.TEST_CASES))) {
			UMLTestSuite srcTestSuite = model.getTestSuite(pathConverter.findUMLPath(path));
			tgtObjTestSuite = (CucumberFeature) project.addFile(path);
			tgtObjTestSuite.parse(content);
			tgtObjTestSuite.addFeatureName(srcTestSuite.getName());
			convertTestSuite(srcTestSuite);
			return tgtObjTestSuite.toString();
		} else {
			UMLStepObject srcStepObject = model.getStepObject(pathConverter.findUMLPath(path));
			if (path.startsWith(project.getDir(project.TEST_STEPS))) {
				tgtObjStepObject = (CucumberClass) project.addFile(path);
			} else {
				tgtObjStepObject = (CucumberInterface) project.addFile(path);
			}
			tgtObjStepObject.parse(content);
			convertStepObject(srcStepObject);
			return tgtObjStepObject.toString();
		}
	}

	protected void convertTestCase(Scenario scenario, UMLTestCase srcTestCase) throws Exception {
		log.debug("test case: " + srcTestCase.getName());

		for (String tag : srcTestCase.getTags()) {
			tgtObjTestSuite.addScenarioTag(scenario, tag);
		}

		if (!srcTestCase.getDescription().isEmpty()) {
			for (String statement : srcTestCase.getDescription().split("\n")) {
				tgtObjTestSuite.addScenarioStatement(scenario, statement);
			}
		}

		for (UMLTestStep srcTestStep : srcTestCase.getTestStepList()) {
			convertTestStep(tgtObjTestSuite.addStep(scenario, srcTestStep.getNameLong()), srcTestStep);
		}
	}

	protected void convertTestCaseWithData(ScenarioOutline scenarioOutline, UMLTestCase srcTestCase) throws Exception {
		log.debug("test case: " + srcTestCase.getName());
		for (String tag : srcTestCase.getTags()) {
			tgtObjTestSuite.addScenarioOutlineTag(scenarioOutline, tag);
		}
		if (!srcTestCase.getDescription().isEmpty()) {
			for (String statement : srcTestCase.getDescription().split("\n")) {
				tgtObjTestSuite.addScenarioOutlineStatement(scenarioOutline, statement);
			}
		}

		for (UMLTestStep srcTestStep : srcTestCase.getTestStepList()) {
			convertTestStep(tgtObjTestSuite.addStep(scenarioOutline, srcTestStep.getNameLong()), srcTestStep);
		}
		for (UMLTestData srcTestData : srcTestCase.getTestDataList()) {
			convertTestData(tgtObjTestSuite.addExamples(scenarioOutline, srcTestData.getName()), srcTestData);
		}
	}

	protected void convertTestData(Examples examples, UMLTestData srcTestData) {
		log.debug("test data: " + srcTestData.getName());
		for (String c : srcTestData.getTags()) {
			tgtObjTestSuite.addExamplesTag(examples, c);
		}

		if (!srcTestData.getDescription().isEmpty()) {
			for (String statement : srcTestData.getDescription().split("\n")) {
				tgtObjTestSuite.addExamplesStatement(examples, statement);
			}
		}
		ExamplesTable examplesTable = tgtObjTestSuite.addExamplesTable(examples);

		for (ArrayList<String> srcRow : srcTestData.getRowList()) {
			Row row = tgtObjTestSuite.addExamplesTableRow(examplesTable);
			for (String srcCell : srcRow) {
				tgtObjTestSuite.addCell(row.getCells(), srcCell);
			}
		}
	}

	protected void convertTestSetup(Background background, UMLTestSetup srcTestSetup) throws Exception {
		log.debug("test setup: " + srcTestSetup.getName());
		// TODO replace getDescription with getStatementList
		if (!srcTestSetup.getDescription().isEmpty()) {
			for (String statement : srcTestSetup.getDescription().split("\n")) {
				tgtObjTestSuite.addBackgroundStatement(background, statement);
			}
		}

		for (UMLTestStep srcStep : srcTestSetup.getTestStepList()) {
			// the srcStep will be a url /asciidoctor/project/1/suite/1/case/2/step/3
			// the restTemplate will call that URL and get a resource class which is passed to convertTestStep
			// the asciidoc service will use the set of id in the url and make calls to get the UML element and then populate the resource class
			// example calls are getProj(id).getTestSuite(id).getTestCase(id).getTestStep(id)

			// For now I have to get the id of all nodes from srcStep and pass it to getUMLTestStep
			// getUMLTestStep(project.getId(), suite.getId(), case.getId(), step.getId())
			// It'll return the right srcStep

			convertTestStep(tgtObjTestSuite.addStep(background, srcStep.getNameLong()), srcStep);
		}
	}

	protected void convertTestStep(Step step, UMLTestStep srcStep) throws Exception {
		log.debug("test step: " + srcStep.getName());
		if (srcStep.hasDocString()) {
			DocString docString = tgtObjTestSuite.addDocString(step);
			for (String l : srcStep.getStepText().split("\n")) {
				tgtObjTestSuite.addLine(docString, l);
			}
		} else if (srcStep.hasStepTable()) {
			StepTable stepTable = tgtObjTestSuite.addStepTable(step);
			for (ArrayList<String> srcRow : srcStep.getStepData()) {
				Row row = tgtObjTestSuite.addStepTableRow(stepTable);
				for (String srcCell : srcRow) {
					tgtObjTestSuite.addCell(row.getCells(), srcCell);
				}
			}
		}
	}

	protected void convertTestSuite(UMLTestSuite srcTestSuite) throws Exception {
		log.debug("test suite: " + srcTestSuite.getName());

		for (String tag : srcTestSuite.getTags()) {
			tgtObjTestSuite.addFeatureTag(tag);
		}

		if (!srcTestSuite.getDescription().isEmpty()) {
			for (String statement : srcTestSuite.getDescription().split("\n")) {
				tgtObjTestSuite.addFeatureStatement(statement);
			}
		}
		if (srcTestSuite.hasTestSetup()) {
			convertTestSetup(tgtObjTestSuite.addBackground(srcTestSuite.getTestSetup().getName()),
					srcTestSuite.getTestSetup());
		}
		for (UMLTestCase srcTestCase : srcTestSuite.getTestCaseList()) {
			if (srcTestCase.hasTestData()) {
				convertTestCaseWithData(tgtObjTestSuite.addScenarioOutline(srcTestCase.getName()), srcTestCase);
			} else {
				convertTestCase(tgtObjTestSuite.addScenario(srcTestCase.getName()), srcTestCase);
			}
		}
	}

	protected void convertStepObject(UMLStepObject srcStepObject) throws Exception {
		log.debug("step object: " + srcStepObject.getName());
		for (UMLStepDefinition srcStepDefinition : srcStepObject.getStepDefinitionList()) {

			ArrayList<String> parametersListMerged = new ArrayList<String>();
			for (org.farhan.mbt.core.UMLStepParameters a : srcStepDefinition.getStepParametersList()) {
				for (String s : a.getUmlElement().getDetails().getFirst().getValue().split("\\|")) {
					if (!parametersListMerged.contains(s.trim())) {
						parametersListMerged.add(s.trim());
					}
				}
			}
			tgtObjStepObject.addStepDefinition(srcStepDefinition.getNameLong(), parametersListMerged);
		}
	}

	@Override
	public ArrayList<String> getFileNames() throws Exception {
		initProjects();
		ArrayList<String> objects = new ArrayList<String>();
		for (UMLTestSuite co : model.getTestSuiteList()) {
			objects.add(pathConverter.convertFilePath(co.getUmlElement().getQualifiedName(), project.TEST_CASES));
		}
		for (UMLStepObject co : model.getStepObjectList()) {
			objects.add(pathConverter.convertFilePath(co.getUmlElement().getQualifiedName(), project.TEST_STEPS));
			objects.add(pathConverter.convertFilePath(co.getUmlElement().getQualifiedName(), project.TEST_OBJECTS));
		}
		return objects;
	}

	public void initProjects() throws Exception {
		model = new UMLTestProject(this.tags, this.fa);
		project = new CucumberTestProject(this.tags, this.fa);
		model.init();
		project.init();
		this.pathConverter = new CucumberPathConverter(model, (CucumberTestProject) project);
	}
}
