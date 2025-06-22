package org.farhan.runners.surefire;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;

import org.springframework.web.client.RestTemplate;

@ExtendWith({ StubRunnerExtension.class, SpringExtension.class })
@AutoConfigureStubRunner(ids = "org.farhan:sheep-dog-dev-svc:+:stubs:8080", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class StubRunnerTest {

    @Test
    void clearConvertAsciidoctorToUMLObjects() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("scenarioId", "Create cucumber files from asciidoc files");
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        restTemplate.exchange(
                "http://localhost:8080/sheep-dog-dev-svc/clearConvertAsciidoctorToUMLObjects?tags={tags}",
                org.springframework.http.HttpMethod.DELETE,
                requestEntity,
                Void.class,
                "");
    }
}