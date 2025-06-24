package org.farhan.runners.surefire;

import java.io.File;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.contract.stubrunner.junit.StubRunnerExtension;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.spring.CucumberContextConfiguration;

@ComponentScan(basePackages = { "org.farhan.impl" })
@EnableAutoConfiguration
@ActiveProfiles("failsafe")
@CucumberContextConfiguration
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource("classpath:application-surefire.properties")
@ExtendWith({ StubRunnerExtension.class, SpringExtension.class })
// TODO update this to read the surefire properties file
@AutoConfigureStubRunner(ids = "org.farhan:sheep-dog-dev-svc:+:stubs:8081", stubsMode = StubRunnerProperties.StubsMode.LOCAL)

public class TestConfig {

	public static String scenarioId = null;

	public void deleteDir(File aDir) {
		if (aDir.exists()) {
			for (String s : aDir.list()) {
				File f = new File(aDir.getAbsolutePath() + File.separator + s);
				if (f.isDirectory()) {
					deleteDir(f);
				}
				f.delete();
			}
		}
	}

	@Before
	public void before(Scenario scenario) {
		deleteDir(new File("target/src-gen/"));
		scenarioId = scenario.getName();
	}
}