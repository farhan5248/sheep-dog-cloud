package org.farhan.mbt.service.cucumber;

import org.farhan.dsl.lang.IResourceRepository;
import org.farhan.mbt.core.UMLTestProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConvertUMLToCucumberSpring extends ConvertUMLToCucumber {

	public ConvertUMLToCucumberSpring(String tags, IResourceRepository fa, String serverHost, int serverPort) {
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
