package org.farhan.mbt.service.cucumber;

import org.farhan.mbt.core.ObjectRepository;
import org.farhan.mbt.core.UMLTestProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertUMLToCucumberSpring extends ConvertUMLToCucumber {

	private static final Logger log = LoggerFactory.getLogger(ConvertUMLToCucumberSpring.class);

	public ConvertUMLToCucumberSpring(String tags, ObjectRepository fa, String serverHost, int serverPort) {
		super(tags, fa, serverHost, serverPort);
	}

	public void initProjects() throws Exception {
		model = new UMLTestProject(this.tags, this.fa);
		project = new CucumberSpringTestProject(this.tags, this.fa);
		model.init();
		project.init();
		this.pathConverter = new CucumberPathConverter(model, (CucumberTestProject) project);
	}

}
