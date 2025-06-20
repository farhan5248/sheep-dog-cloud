package org.farhan.runners.surefire.springboot;

import java.io.File;

import org.farhan.mbt.RestServiceApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;

@ComponentScan(basePackages = {"org.farhan.mbt", "org.farhan.common", "org.farhan.impl"})
@EnableAutoConfiguration
@ActiveProfiles("surefire")
@CucumberContextConfiguration
@SpringBootTest(classes = RestServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SpringBootTestConfig {

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
	public void before() {
		deleteDir(new File("target/src-gen/"));
	}
}