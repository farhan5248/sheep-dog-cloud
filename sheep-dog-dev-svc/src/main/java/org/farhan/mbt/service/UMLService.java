package org.farhan.mbt.service;

import org.farhan.mbt.core.ObjectRepository;
import org.farhan.mbt.core.UMLTestProject;
import org.farhan.mbt.model.UMLTestStep;
import org.farhan.mbt.model.UMLTestCase;
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
        UMLTestStep testStep = null;
        // TODO having a model like this isn't very efficient, but it'll work for now
        // It's not efficient because it loads the entire model for every query each
        // time. Even if I kept one model cached per id/tag, I'd have to make sure any
        // writes to it are updating the same one.
        UMLTestProject model = new UMLTestProject(projectId, repository);
        model.init();
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
}
