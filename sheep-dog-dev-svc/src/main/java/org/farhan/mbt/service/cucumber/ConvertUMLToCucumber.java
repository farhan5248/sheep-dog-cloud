package org.farhan.mbt.service.cucumber;

import java.util.ArrayList;
import java.util.TreeMap;

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
import org.farhan.mbt.core.UMLTestProject;
import org.farhan.mbt.model.UMLTestSuite;
import org.farhan.mbt.model.UMLTestCase;
import org.farhan.mbt.model.UMLTestData;
import org.farhan.mbt.model.UMLTestSetup;
import org.farhan.mbt.model.UMLTestStep;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class ConvertUMLToCucumber extends Converter {

	protected UMLTestSuite srcObj;
	protected CucumberFeature tgtObjTestSuite;
	protected CucumberJava tgtObjStepObject;
	protected CucumberPathConverter pathConverter;

	private final RestTemplate restTemplate;
	private final int RETRY_COUNT = 10;
	private String serverHost;
	private int serverPort;

	public ConvertUMLToCucumber(String tags, ObjectRepository fa, Logger log, String serverHost, int serverPort) {
		super(tags, fa, log);
		restTemplate = new RestTemplate();
		this.serverHost = serverHost;
		this.serverPort = serverPort;
	}

	@Override
	public String convertFile(String path, String content) throws Exception {
		initProjects();
		if (path.startsWith(project.getDir(project.TEST_CASES))) {
			TreeMap<String, String> parameters = new TreeMap<String, String>();
			parameters.put("qualifiedName", pathConverter.findUMLPath(path));
			UMLTestSuite srcTestSuite = (UMLTestSuite) getResource(
					"project/" + (tags.isEmpty() ? "default" : tags) + "/suite?qualifiedName={qualifiedName}",
					new ParameterizedTypeReference<UMLTestSuite>() {
					}, parameters);
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

	protected void convertTestCase(Scenario scenario, UMLTestCase srcTestCase) throws Exception {
		log.debug("test case: " + srcTestCase.getName());

		for (String tag : srcTestCase.getTags()) {
			tgtObjTestSuite.addScenarioTag(scenario, tag);
		}

		if (!srcTestCase.getDescription().isEmpty()) {
			for (String statement : srcTestCase.getDescription()) {
				tgtObjTestSuite.addScenarioStatement(scenario, statement);
			}
		}

		for (String umlTestStepLink : srcTestCase.getTestStepList()) {
			UMLTestStep srcStep = (UMLTestStep) getResource(umlTestStepLink,
					new ParameterizedTypeReference<UMLTestStep>() {
					}, null);
			convertTestStep(tgtObjTestSuite.addStep(scenario, srcStep.getNameLong()),
					srcStep);
		}
	}

	protected void convertTestCaseWithData(ScenarioOutline scenarioOutline,
			UMLTestCase srcTestCase) throws Exception {
		log.debug("test case: " + srcTestCase.getName());
		for (String tag : srcTestCase.getTags()) {
			tgtObjTestSuite.addScenarioOutlineTag(scenarioOutline, tag);
		}
		if (!srcTestCase.getDescription().isEmpty()) {
			for (String statement : srcTestCase.getDescription()) {
				tgtObjTestSuite.addScenarioOutlineStatement(scenarioOutline, statement);
			}
		}

		for (String umlTestStepLink : srcTestCase.getTestStepList()) {
			UMLTestStep srcStep = (UMLTestStep) getResource(umlTestStepLink,
					new ParameterizedTypeReference<UMLTestStep>() {
					}, null);
			convertTestStep(tgtObjTestSuite.addStep(scenarioOutline, srcStep.getNameLong()),
					srcStep);
		}
		for (String umlTestDataLink : srcTestCase.getTestDataList()) {
			UMLTestData srcTestData = (UMLTestData) getResource(
					umlTestDataLink,
					new ParameterizedTypeReference<UMLTestData>() {
					}, null);
			convertTestData(tgtObjTestSuite.addExamples(scenarioOutline, srcTestData.getName()), srcTestData);
		}

	}

	protected void convertTestData(Examples examples, UMLTestData srcTestData) {
		log.debug("test data: " + srcTestData.getName());
		for (String c : srcTestData.getTags()) {
			tgtObjTestSuite.addExamplesTag(examples, c);
		}

		if (!srcTestData.getDescription().isEmpty()) {
			for (String statement : srcTestData.getDescription()) {
				tgtObjTestSuite.addExamplesStatement(examples, statement);
			}
		}
		ExamplesTable examplesTable = tgtObjTestSuite.addExamplesTable(examples);
		for (ArrayList<String> srcRow : srcTestData.getDataTable()) {
			Row row = tgtObjTestSuite.addExamplesTableRow(examplesTable);
			for (String srcCell : srcRow) {
				tgtObjTestSuite.addCell(row.getCells(), srcCell);
			}
		}
	}

	protected void convertTestSetup(Background background, UMLTestSetup srcTestSetup)
			throws Exception {
		log.debug("test setup: " + srcTestSetup.getName());
		// TODO replace getDescription with getStatementList
		if (!srcTestSetup.getDescription().isEmpty()) {
			for (String statement : srcTestSetup.getDescription()) {
				tgtObjTestSuite.addBackgroundStatement(background, statement);
			}
		}

		for (String umlTestStepLink : srcTestSetup.getTestStepList()) {
			UMLTestStep srcStep = (UMLTestStep) getResource(umlTestStepLink,
					new ParameterizedTypeReference<UMLTestStep>() {
					}, null);
			convertTestStep(tgtObjTestSuite.addStep(background, srcStep.getNameLong()),
					srcStep);
		}
	}

	protected void convertTestStep(Step step, UMLTestStep srcStep) throws Exception {
		log.debug("test step: " + srcStep.getName());
		if (srcStep.getStepText() != null) {
			DocString docString = tgtObjTestSuite.addDocString(step);
			// TODO remove the need for splitting by line
			for (String l : srcStep.getStepText().split("\n")) {
				tgtObjTestSuite.addLine(docString, l);
			}
		} else if (srcStep.getStepTable() != null) {
			// TODO make names like get step table/data vs test data consistent
			StepTable stepTable = tgtObjTestSuite.addStepTable(step);
			for (ArrayList<String> srcRow : srcStep.getStepTable()) {
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
			for (String statement : srcTestSuite.getDescription()) {
				tgtObjTestSuite.addFeatureStatement(statement);
			}
		}
		if (srcTestSuite.getTestSetup() != null) {
			UMLTestSetup srcTestSetup = (UMLTestSetup) getResource(srcTestSuite.getTestSetup(),
					new ParameterizedTypeReference<UMLTestSetup>() {
					}, null);
			convertTestSetup(tgtObjTestSuite.addBackground(srcTestSetup.getName()),
					srcTestSetup);
		}
		for (String testCaseLink : srcTestSuite.getTestCaseList()) {
			UMLTestCase srcTestCase = (UMLTestCase) getResource(testCaseLink,
					new ParameterizedTypeReference<UMLTestCase>() {
					}, null);
			if (!srcTestCase.getTestDataList().isEmpty()) {
				convertTestCaseWithData(tgtObjTestSuite.addScenarioOutline(srcTestCase.getName()),
						srcTestCase);
			} else {
				convertTestCase(tgtObjTestSuite.addScenario(srcTestCase.getName()),
						srcTestCase);
			}
		}
	}

	@Override
	public ArrayList<String> getFileNames() throws Exception {
		initProjects();
		ArrayList<String> objects = new ArrayList<String>();

		org.farhan.mbt.model.UMLTestProject srcTestProject = (org.farhan.mbt.model.UMLTestProject) getResource(
				"project/" + (tags.isEmpty() ? "default" : tags),
				new ParameterizedTypeReference<org.farhan.mbt.model.UMLTestProject>() {
				}, new TreeMap<String, String>());
		for (String testSuiteLink : srcTestProject.getTestSuiteList()) {
			UMLTestSuite srcTestSuite = (UMLTestSuite) getResource(testSuiteLink,
					new ParameterizedTypeReference<UMLTestSuite>() {
					}, null);
			objects.add(pathConverter.convertFilePath(srcTestSuite.getQualifiedName(), project.TEST_CASES));
		}

		for (UMLStepObject co : model.getStepObjectList()) {
			objects.add(pathConverter.convertFilePath(co.getUmlElement().getQualifiedName(), project.TEST_STEPS));
			objects.add(pathConverter.convertFilePath(co.getUmlElement().getQualifiedName(), project.TEST_OBJECTS));
		}
		return objects;
	}

	private String getHost() {
		return "http://" + serverHost + ":" + serverPort + "/";
	}

	@SuppressWarnings("unchecked")
	protected Object getResource(String resource, ParameterizedTypeReference typeReference,
			TreeMap<String, String> parameters)
			throws Exception {
		String url = getHost() + "uml/" + resource;
		int retryCount = 0;
		while (retryCount < RETRY_COUNT) {
			try {
				return restTemplate.exchange(
						url,
						HttpMethod.GET,
						null,
						typeReference, parameters != null ? parameters : new TreeMap<String, String>()).getBody();
			} catch (RestClientException e) {
				Thread.sleep(1000);
				retryCount++;
			}
		}
		// TODO add parameters to the exception message
		throw new Exception("Max retries reached while getting resource: " + url);
	}

	public void initProjects() throws Exception {
		model = new UMLTestProject(this.tags, this.fa);
		project = new CucumberTestProject(this.tags, this.fa);
		model.init();
		project.init();
		this.pathConverter = new CucumberPathConverter(model, (CucumberTestProject) project);
	}
}
