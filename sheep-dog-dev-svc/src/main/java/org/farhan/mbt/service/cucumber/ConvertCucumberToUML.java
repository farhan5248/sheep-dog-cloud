package org.farhan.mbt.service.cucumber;

import org.farhan.dsl.cucumber.cucumber.AbstractScenario;
import org.farhan.dsl.cucumber.cucumber.Examples;
import org.farhan.dsl.cucumber.cucumber.Row;
import org.farhan.dsl.cucumber.cucumber.Scenario;
import org.farhan.dsl.cucumber.cucumber.ScenarioOutline;
import org.farhan.dsl.cucumber.cucumber.Step;
import org.farhan.mbt.core.Converter;
import org.farhan.mbt.core.IObjectRepository;
import org.farhan.mbt.core.UMLTestCase;
import org.farhan.mbt.core.UMLTestData;
import org.farhan.mbt.core.UMLTestSuite;
import org.farhan.mbt.core.UMLTestProject;
import org.farhan.mbt.core.UMLTestSetup;
import org.farhan.mbt.core.UMLTestStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertCucumberToUML extends Converter {

	private static final Logger log = LoggerFactory.getLogger(ConvertCucumberToUML.class);
	protected CucumberPathConverter pathConverter;
	private CucumberFeature srcObjTestSuite;

	public ConvertCucumberToUML(String tags, IObjectRepository fa) {
		super(tags, fa);
	}

	@Override
	public String convertFile(String path, String content) throws Exception {
		initProjects();
		convertTestSuite(path, content);
		return "";
	}

	private void convertTestCase(AbstractScenario srcTestCase, UMLTestCase testCase) {
		log.debug("test case: " + srcTestCase.getName());
		testCase.setTags(srcObjTestSuite.getScenarioTags(srcTestCase));
		testCase.setDescription(srcObjTestSuite.getScenarioDescription(srcTestCase));
		for (Step srcStep : srcObjTestSuite.getStepList(srcTestCase)) {
			convertTestStep(testCase.addTestStep(srcObjTestSuite.getStepName(srcStep)), srcStep);
		}
	}

	private void convertTestCaseWithTestData(AbstractScenario srcTestCase, UMLTestCase testCase) {
		log.debug("test case: " + srcTestCase.getName());
		testCase.setTags(srcObjTestSuite.getScenarioOutlineTags(srcTestCase));
		testCase.setDescription(srcObjTestSuite.getScenarioOutlineDescription(srcTestCase));
		for (Step srcStep : srcObjTestSuite.getStepList(srcTestCase)) {
			convertTestStep(testCase.addTestStep(srcObjTestSuite.getStepName(srcStep)), srcStep);
		}
		for (Examples srcExamples : srcObjTestSuite.getExamplesList(srcTestCase)) {
			convertTestData(testCase.addTestData(srcObjTestSuite.getExamplesName(srcExamples)), srcExamples);
		}
	}

	private void convertTestData(UMLTestData examples, Examples srcExamples) {
		log.debug("test data: " + srcExamples.getName());
		examples.setTags(srcObjTestSuite.getExamplesTags(srcExamples));
		examples.setDescription(srcObjTestSuite.getExamplesDescription(srcExamples));
		examples.setTable(srcObjTestSuite.getExamplesTable(srcExamples));
		for (Row row : srcObjTestSuite.getExamplesRowList(srcExamples)) {
			examples.addRow(srcObjTestSuite.getExamplesRow(srcExamples, row));
		}
	}

	private void convertTestSetup(AbstractScenario srcAbstractScenario, UMLTestSuite testSuite) {
		log.debug("test setup: " + srcAbstractScenario.getName());
		UMLTestSetup background = testSuite.addTestSetup(srcObjTestSuite.getBackgroundName(srcAbstractScenario));
		background.setDescription(srcObjTestSuite.getBackgroundDescription(srcAbstractScenario));
		for (Step srcStep : srcObjTestSuite.getStepList(srcAbstractScenario)) {
			convertTestStep(background.addTestStep(srcObjTestSuite.getStepName(srcStep)), srcStep);
		}
	}

	private void convertTestStep(UMLTestStep step, Step srcStep) {
		log.debug("test step: " + srcStep.getName());
		step.setKeyword(srcObjTestSuite.getStepKeyword(srcStep));
		step.setNameLong(srcObjTestSuite.getStepNameLong(srcStep));
		if (srcObjTestSuite.hasDocString(srcStep)) {
			step.setStepText(srcObjTestSuite.getDocString(srcStep));
		} else if (srcObjTestSuite.hasStepTable(srcStep)) {
			step.setStepData(srcObjTestSuite.getStepTable(srcStep));
		}
	}

	private void convertTestSuite(String path, String content) throws Exception {
		log.debug("test suite: " + path);

		srcObjTestSuite = (CucumberFeature) project.addFile(path);
		srcObjTestSuite.parse(content);
		if (tags.isEmpty() || isTestSuiteSelected() || isAnyTestCaseSelected()) {
			UMLTestSuite testSuite = model.addTestSuite(pathConverter.convertUMLPath(srcObjTestSuite.getPath()));
			testSuite.setTags(srcObjTestSuite.getFeatureTags());
			testSuite.setDescription(srcObjTestSuite.getFeatureDescription());
			for (AbstractScenario as : srcObjTestSuite.getAbstractScenarioList()) {
				if (srcObjTestSuite.isBackground(as)) {
					convertTestSetup(as, testSuite);
				} else {
					if (tags.isEmpty() || isTestSuiteSelected() || isTestCaseSelected(as)) {
						if (srcObjTestSuite.isScenarioOutline(as)) {
							convertTestCaseWithTestData(as,
									testSuite.addTestCase(srcObjTestSuite.getScenarioOutlineName(as)));
						} else {
							convertTestCase(as, testSuite.addTestCase(srcObjTestSuite.getScenarioName(as)));
						}
					}
				}
			}
			model.save();
		} else {
			project.deleteObject(srcObjTestSuite);
		}

	}

	public void initProjects() throws Exception {
		project = new CucumberTestProject(this.tags, this.fa);
		model = new UMLTestProject(this.tags, this.fa);
		project.init();
		model.init();
		this.pathConverter = new CucumberPathConverter(model, (CucumberTestProject) project);
	}

	private boolean isAnyTestCaseSelected() {
		for (AbstractScenario a : srcObjTestSuite.getAbstractScenarioList()) {
			if (isTestCaseSelected(a)) {
				return true;
			}
		}
		return false;
	}

	private boolean isTestCaseSelected(AbstractScenario as) {

		if (as instanceof Scenario) {
			for (String t : srcObjTestSuite.getScenarioTags(as)) {
				if (t.trim().contentEquals(tags)) {
					return true;
				}
			}
		}
		if (as instanceof ScenarioOutline) {
			for (String t : srcObjTestSuite.getScenarioOutlineTags(as)) {
				if (t.trim().contentEquals(tags)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isTestSuiteSelected() throws Exception {
		for (String t : srcObjTestSuite.getFeatureTags()) {
			if (t.trim().contentEquals(tags)) {
				return true;
			}
		}
		return false;
	}
}
