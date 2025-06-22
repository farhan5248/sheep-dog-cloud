package org.farhan.runners.surefire;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.web.client.RestTemplate;

@ExtendWith({ StubRunnerExtension.class, SpringExtension.class })
@AutoConfigureStubRunner(ids = "org.farhan:sheep-dog-dev-svc:+:stubs:8080", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class HelloClientTest {

    @Test
    void shouldReturnHelloMessage() {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("http://localhost:8080/hello", String.class);
    }
}