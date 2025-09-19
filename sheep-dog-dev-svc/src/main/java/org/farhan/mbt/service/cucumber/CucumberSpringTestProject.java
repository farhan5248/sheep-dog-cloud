package org.farhan.mbt.service.cucumber;

import org.farhan.mbt.core.IConvertibleObject;
import org.farhan.mbt.core.IObjectRepository;

public class CucumberSpringTestProject extends CucumberTestProject {

	public CucumberSpringTestProject(String tags, IObjectRepository fa) {
		super(tags, fa);
	}

	@Override
	protected IConvertibleObject createClass(String path) {
		return new CucumberSpringClass(path);
	}

}
