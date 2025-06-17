package org.farhan.runners.failsafe;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import java.io.File;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "org.farhan.impl")
@EnableAutoConfiguration
@ActiveProfiles("failsafe")
@CucumberContextConfiguration
@ContextConfiguration(classes = Config.class)
public class Config {

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