package org.farhan.runners.surefire;

import org.junit.jupiter.api.Test;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.web.client.RestTemplate;

@ExtendWith({ StubRunnerExtension.class, SpringExtension.class })
@AutoConfigureStubRunner(ids = "org.farhan:sheep-dog-dev-svc:s:stubs:8080", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class StubRunnerTest {

    @Test
    void clearConvertAsciidoctorToUMLObjects() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete("http://localhost:8080/sheep-dog-dev-svc/clearConvertAsciidoctorToUMLObjects");
    }
}