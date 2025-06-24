package org.farhan.mbt.next.cucumber;

import java.util.ArrayList;

import org.farhan.dsl.cucumber.cucumber.Background;
import org.farhan.dsl.cucumber.cucumber.Examples;
import org.farhan.dsl.cucumber.cucumber.Scenario;
import org.farhan.dsl.cucumber.cucumber.ScenarioOutline;
import org.farhan.dsl.cucumber.cucumber.Step;
import org.farhan.mbt.core.Converter;

import org.farhan.mbt.core.Logger;
import org.farhan.mbt.core.ObjectRepository;
import org.farhan.mbt.core.UMLStepDefinition;
import org.farhan.mbt.core.UMLStepObject;
import org.farhan.mbt.core.UMLTestCase;
import org.farhan.mbt.core.UMLTestData;
import org.farhan.mbt.core.UMLTestSuite;
import org.farhan.mbt.cucumber.CucumberClass;
import org.farhan.mbt.cucumber.CucumberFeature;
import org.farhan.mbt.cucumber.CucumberInterface;
import org.farhan.mbt.cucumber.CucumberJava;
import org.farhan.mbt.cucumber.CucumberPathConverter;
import org.farhan.mbt.cucumber.CucumberTestProject;
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
			return convertTestSuite(path, content);
		} else {
			return convertStepObject(path, content);
		}
	}

	protected String convertStepObject(String path, String content) throws Exception {
		log.debug("step object: " + path);
		UMLStepObject srcStepObject = model.getStepObject(pathConverter.findUMLPath(path));
		if (path.startsWith(project.getDir(project.TEST_STEPS))) {
			tgtObjStepObject = (CucumberClass) project.addFile(path);
		} else {
			tgtObjStepObject = (CucumberInterface) project.addFile(path);
		}
		tgtObjStepObject.parse(content);
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
		return tgtObjStepObject.toString();
	}

	protected void convertTestCase(Scenario scenario, UMLTestCase srcTestCase) throws Exception {
		log.debug("test case: " + srcTestCase.getName());
		tgtObjTestSuite.setScenarioTags(scenario, srcTestCase.getTags());
		tgtObjTestSuite.setScenarioDescription(scenario, srcTestCase.getDescription());
		for (UMLTestStep srcTestStep : srcTestCase.getTestStepList()) {
			convertTestStep(tgtObjTestSuite.addStep(scenario, srcTestStep.getNameLong()), srcTestStep);
		}
	}

	protected void convertTestCaseWithData(ScenarioOutline scenarioOutline, UMLTestCase srcTestCase) throws Exception {
		log.debug("test case: " + srcTestCase.getName());
		tgtObjTestSuite.setScenarioOutlineTags(scenarioOutline, srcTestCase.getTags());
		tgtObjTestSuite.setScenarioOutlineDescription(scenarioOutline, srcTestCase.getDescription());
		for (UMLTestStep srcTestStep : srcTestCase.getTestStepList()) {
			convertTestStep(tgtObjTestSuite.addStep(scenarioOutline, srcTestStep.getNameLong()), srcTestStep);
		}
		for (UMLTestData srcTestData : srcTestCase.getTestDataList()) {
			convertTestData(tgtObjTestSuite.addExamples(scenarioOutline, srcTestData.getName()), srcTestData);
		}
	}

	protected void convertTestData(Examples examples, UMLTestData srcTestData) {
		log.debug("test data: " + srcTestData.getName());
		tgtObjTestSuite.setExamplesTags(examples, srcTestData.getTags());
		tgtObjTestSuite.setExamplesDescription(examples, srcTestData.getDescription());
		tgtObjTestSuite.setExamplesTable(examples, srcTestData.getTable());
		for (ArrayList<String> row : srcTestData.getRowList()) {
			tgtObjTestSuite.addExamplesRow(examples, row);
		}
	}

	protected void convertTestSetup(Background background, UMLTestSetup srcTestSetup) throws Exception {
		log.debug("test setup: " + srcTestSetup.getName());
		tgtObjTestSuite.setBackgroundDescription(background, srcTestSetup.getDescription());
		for (UMLTestStep srcStep : srcTestSetup.getTestStepList()) {
			convertTestStep(tgtObjTestSuite.addStep(background, srcStep.getNameLong()), srcStep);
		}
	}

	protected void convertTestStep(Step step, UMLTestStep srcStep) throws Exception {
		log.debug("test step: " + srcStep.getName());
		if (srcStep.hasDocString()) {
			tgtObjTestSuite.setDocString(step, srcStep.getStepText());
		} else if (srcStep.hasStepTable()) {
			tgtObjTestSuite.setStepTable(step, srcStep.getStepData());
		}
	}

	protected String convertTestSuite(String path, String content) throws Exception {
		log.debug("test suite: " + path);
		UMLTestSuite srcTestSuite = model.getTestSuite(pathConverter.findUMLPath(path));
		tgtObjTestSuite = (CucumberFeature) project.addFile(path);
		tgtObjTestSuite.parse(content);
		tgtObjTestSuite.setFeatureName(srcTestSuite.getName());
		tgtObjTestSuite.setFeatureTags(srcTestSuite.getTags());
		tgtObjTestSuite.setFeatureDescription(srcTestSuite.getDescription());

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
		return tgtObjTestSuite.toString();
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
