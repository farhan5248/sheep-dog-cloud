package org.farhan.mbt.service.cucumber;

import org.farhan.mbt.core.ConvertibleObject;
import org.farhan.mbt.core.ObjectRepository;

public class CucumberGuiceTestProject extends CucumberTestProject {

	public CucumberGuiceTestProject(String tags, ObjectRepository fa) {
		super(tags, fa);
	}

	@Override
	protected ConvertibleObject createClass(String file) {
		return new CucumberGuiceClass(file);
	}
}
