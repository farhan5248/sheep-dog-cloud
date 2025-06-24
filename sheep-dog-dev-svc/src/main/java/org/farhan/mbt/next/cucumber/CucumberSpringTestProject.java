package org.farhan.mbt.next.cucumber;

import org.farhan.mbt.core.ConvertibleObject;
import org.farhan.mbt.core.ObjectRepository;
import org.farhan.mbt.cucumber.CucumberSpringClass;
import org.farhan.mbt.cucumber.CucumberTestProject;

public class CucumberSpringTestProject extends CucumberTestProject {

	public CucumberSpringTestProject(String tags, ObjectRepository fa) {
		super(tags, fa);
	}

	@Override
	protected ConvertibleObject createClass(String path) {
		return new CucumberSpringClass(path);
	}

}
