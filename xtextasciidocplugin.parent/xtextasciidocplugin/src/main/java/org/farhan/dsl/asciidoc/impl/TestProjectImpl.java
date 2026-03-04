package org.farhan.dsl.asciidoc.impl;

import java.io.File;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.farhan.dsl.grammar.SheepDogLoggerFactory;
import org.farhan.dsl.grammar.IResourceRepository;
import org.farhan.dsl.grammar.IStepObject;
import org.farhan.dsl.grammar.ITestDocument;
import org.farhan.dsl.grammar.ITestProject;
import org.farhan.dsl.grammar.ITestSuite;
import org.farhan.dsl.grammar.SheepDogFactory;

public class TestProjectImpl implements ITestProject {

	private static Logger logger = SheepDogLoggerFactory.getLogger(TestProjectImpl.class);

	private IResourceRepository sr;
	private String projectPath;
	public final String layer2dir;

	TestProjectImpl(IResourceRepository sr) {
		// TODO In the future the project name should be accessible here. The
		// constructor should get a path that includes the project name. It
		// can then intercept the project name and save it.
		this.sr = sr;
		layer2dir = "src/test/resources/asciidoc/stepdefs";
		projectPath = null;
	}

	public boolean addTestDocument(ITestDocument value) {
		try {
			IStepObject stepObject = (IStepObject) value;
			sr.put("", projectPath + "/" + layer2dir + "/" + stepObject.getFullName(), stepObject.getContent());
			return true;
		} catch (Exception e) {
			logger.error("Couldn't write step object:", e);
			return false;
		}
	}

	@Override
	public boolean addTestSuite(ITestSuite value) {
		throw new UnsupportedOperationException("addTestSuite(ITestSuite value) is not implemented");
	}

	@Override
	public String getFileExtension() {
		// TODO make a static config object for this and they layer 2 directory
		return ".asciidoc";
	}

	public String getName() {
		return projectPath;
	}

	@Override
	public ITestDocument getTestDocument(int index) {
		throw new UnsupportedOperationException("getTestDocument(int index) is not implemented");
	}

	@Override
	public ITestDocument getTestDocument(String qualifiedName) {
		if (sr.contains("", projectPath + "/" + layer2dir + "/" + qualifiedName)) {
			try {
				String text = sr.get("", projectPath + "/" + layer2dir + "/" + qualifiedName);
				if (text.isEmpty()) {
					logger.error("Couldn't load StepObject for, file is empty: " + qualifiedName);
				} else {
					IStepObject stepObject = SheepDogFactory.instance.createStepObject();
					stepObject.setFullName(qualifiedName);
					stepObject.setContent(text);
					return stepObject;
				}
			} catch (Exception e) {
				logger.error("Couldn't load StepObject for: " + qualifiedName, e);
			}
		}
		return null;
	}

	@Override
	public ArrayList<ITestDocument> getTestDocumentList() {
		ArrayList<ITestDocument> objects = new ArrayList<ITestDocument>();
		try {
			// TODO instead of empty tags, append it to the prefix?
			for (String stepObjectFileName : sr.list("", projectPath + "/" + layer2dir, getFileExtension())) {
				objects.add(getTestDocument(stepObjectFileName.replace(projectPath + "/" + layer2dir + "/", "")));
			}
		} catch (Exception e) {
			logger.error("Couldn't get StepObject list:", e);
		}
		return objects;
	}

	@Override
	public ITestSuite getTestSuite(int index) {
		throw new UnsupportedOperationException("getTestSuite(int index) is not implemented");
	}

	@Override
	public ITestSuite getTestSuite(String name) {
		throw new UnsupportedOperationException("getTestSuite(String name) is not implemented");
	}

	@Override
	public ArrayList<ITestSuite> getTestSuiteList() {
		throw new UnsupportedOperationException("getTestSuiteList() is not implemented");
	}

	public void setName(String projectPath) {
		this.projectPath = projectPath.replace(File.separator, "/");
	}
}
