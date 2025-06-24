package org.farhan.mbt.next.cucumber;

import org.farhan.mbt.core.ConvertibleObject;
import org.farhan.mbt.core.ObjectRepository;
import org.farhan.mbt.cucumber.CucumberGuiceClass;
import org.farhan.mbt.cucumber.CucumberTestProject;

public class CucumberGuiceTestProject extends CucumberTestProject {

	public CucumberGuiceTestProject(String tags, ObjectRepository fa) {
		super(tags, fa);
	}

	@Override
	protected ConvertibleObject createClass(String file) {
		return new CucumberGuiceClass(file);
	}
}
