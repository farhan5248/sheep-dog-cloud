package org.farhan.mbt.controller;

import org.farhan.mbt.model.UMLTestCase;
import org.farhan.mbt.model.UMLTestStep;
import org.farhan.mbt.service.UMLService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@ConfigurationProperties(prefix = "sheepdog")
@RestController
@RequestMapping("/uml")
public class UMLController {

    Logger logger = LoggerFactory.getLogger(UMLController.class);
    private final UMLService service;

    @Autowired
    public UMLController(UMLService service) {
        this.service = service;
    }

    @GetMapping("/project/{projectId}/suite/{suiteId}/setup/step/{stepId}")
    public UMLTestStep getUMLTestStepTestSetup(
            @PathVariable String projectId,
            @PathVariable String suiteId,
            @PathVariable String stepId) throws Exception {
        logger.info("Starting getUMLTestStepTestSetup");
        UMLTestStep testStep = service.getUMLTestStep(projectId.contentEquals("default") ? "" : projectId, suiteId,
                null,
                stepId);
        logger.info("Ending getUMLTestStepTestSetup");
        return testStep;
    }

    @GetMapping("/project/{projectId}/suite/{suiteId}/case/{caseId}/step/{stepId}")
    public UMLTestStep getUMLTestStepTestCase(
            @PathVariable String projectId,
            @PathVariable String suiteId,
            @PathVariable String caseId,
            @PathVariable String stepId) throws Exception {
        logger.info("Starting getUMLTestStepTestCase");
        UMLTestStep testStep = service.getUMLTestStep(projectId.contentEquals("default") ? "" : projectId, suiteId,
                caseId,
                stepId);
        logger.info("Ending getUMLTestStepTestCase");
        return testStep;
    }

    @GetMapping("/project/{projectId}/suite/{suiteId}/case/{caseId}")
    public UMLTestCase getUMLTestCase(
            @PathVariable String projectId,
            @PathVariable String suiteId,
            @PathVariable String caseId) throws Exception {
        logger.info("Starting getUMLTestCase");
        UMLTestCase testCase = service.getUMLTestCase(projectId, suiteId, caseId);
        logger.info("Ending getUMLTestCase");
        return testCase;
    }
}
