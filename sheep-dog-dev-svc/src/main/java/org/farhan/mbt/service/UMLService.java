package org.farhan.mbt.service;

import org.farhan.mbt.core.ObjectRepository;
import org.farhan.mbt.core.UMLTestProject;
import org.farhan.mbt.model.UMLTestStep;
import org.farhan.mbt.model.UMLTestSuite;
import org.farhan.mbt.model.UMLStepDefinition;
import org.farhan.mbt.model.UMLStepObject;
import org.farhan.mbt.model.UMLStepParameters;
import org.farhan.mbt.model.UMLTestCase;
import org.farhan.mbt.model.UMLTestData;
import org.farhan.mbt.model.UMLTestSetup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UMLService {

    private static final Logger logger = LoggerFactory.getLogger(UMLService.class);
    private final ObjectRepository repository;

    @Autowired
    public UMLService(ObjectRepository repository) {
        this.repository = repository;
    }

    // TODO make these int
    public UMLTestStep getUMLTestStep(String projectId, String suiteId, String caseId, String stepId)
            throws Exception {
        // TODO having a model like this isn't very efficient, but it'll work for now
        // It's not efficient because it loads the entire model for every query each
        // time. Even if I kept one model cached per id/tag, I'd have to make sure any
        // writes to it are updating the same one.
        UMLTestProject model = new UMLTestProject(projectId, repository);
        model.init();
        UMLTestStep testStep = null;
        if (caseId == null || caseId.isEmpty()) {
            testStep = new UMLTestStep(
                    model.getTestSuiteList().get(Integer.parseInt(suiteId)).getTestSetup().getTestStepList()
                            .get(Integer.parseInt(stepId)));
        } else {
            testStep = new UMLTestStep(model.getTestSuiteList().get(Integer.parseInt(suiteId))
                    .getTestCaseList().get(Integer.parseInt(caseId)).getTestStepList()
                    .get(Integer.parseInt(stepId)));
        }
        return testStep;
    }

    public UMLTestCase getUMLTestCase(String projectId, String suiteId, String caseId)
            throws Exception {
        UMLTestProject model = new UMLTestProject(projectId, repository);
        model.init();
        UMLTestCase testCase = new UMLTestCase(model.getTestSuiteList().get(Integer.parseInt(suiteId))
                .getTestCaseList().get(Integer.parseInt(caseId)));
        return testCase;
    }

    public UMLTestSetup getUMLTestSetup(String projectId, String suiteId)
            throws Exception {
        UMLTestProject model = new UMLTestProject(projectId, repository);
        model.init();
        UMLTestSetup testSetup = new UMLTestSetup(
                model.getTestSuiteList().get(Integer.parseInt(suiteId)).getTestSetup());
        return testSetup;
    }

    public UMLTestData getUMLTestData(String projectId, String suiteId, String caseId, String dataId)
            throws Exception {
        UMLTestProject model = new UMLTestProject(projectId, repository);
        model.init();
        UMLTestData testData = new UMLTestData(model.getTestSuiteList().get(Integer.parseInt(suiteId))
                .getTestCaseList().get(Integer.parseInt(caseId)).getTestDataList()
                .get(Integer.parseInt(dataId)));
        return testData;
    }

    public org.farhan.mbt.model.UMLTestProject getUMLTestProject(String projectId)
            throws Exception {
        UMLTestProject model = new UMLTestProject(projectId, repository);
        model.init();
        org.farhan.mbt.model.UMLTestProject umlTestProject = new org.farhan.mbt.model.UMLTestProject(model);
        return umlTestProject;
    }

    public UMLStepParameters getUMLStepParameters(String projectId, String objectId, String definitionId,
            String parametersId) throws Exception {
        UMLTestProject model = new UMLTestProject(projectId, repository);
        model.init();
        UMLStepParameters stepParameters = new UMLStepParameters(
                model.getStepObjectList().get(Integer.parseInt(objectId)).getStepDefinitionList()
                        .get(Integer.parseInt(definitionId)).getStepParametersList()
                        .get(Integer.parseInt(parametersId)));
        return stepParameters;
    }

    public UMLStepDefinition getUMLStepDefinition(String projectId, String objectId, String definitionId)
            throws Exception {
        UMLTestProject model = new UMLTestProject(projectId, repository);
        model.init();
        UMLStepDefinition stepDefinition = new UMLStepDefinition(model.getStepObjectList()
                .get(Integer.parseInt(objectId)).getStepDefinitionList().get(Integer.parseInt(definitionId)));
        return stepDefinition;
    }

    public UMLStepObject getUMLStepObject(String projectId, String objectId, String qualifiedName)
            throws Exception {
        UMLTestProject model = new UMLTestProject(projectId, repository);
        model.init();
        UMLStepObject stepObject = new UMLStepObject(
                objectId == null ? model.getStepObject(qualifiedName)
                        : model.getStepObjectList().get(Integer.parseInt(objectId)));
        return stepObject;
    }

    public UMLTestSuite getUMLTestSuite(String projectId, String suiteId, String qualifiedName)
            throws Exception {
        UMLTestProject model = new UMLTestProject(projectId, repository);
        model.init();
        UMLTestSuite testSuite = new UMLTestSuite(
                suiteId == null ? model.getTestSuite(qualifiedName)
                        : model.getTestSuiteList().get(Integer.parseInt(suiteId)));
        return testSuite;
    }
}
