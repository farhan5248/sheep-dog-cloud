package org.farhan.mbt.service.cucumber;

import org.farhan.mbt.core.IConvertibleObject;
import org.farhan.mbt.core.IObjectRepository;

public class CucumberGuiceTestProject extends CucumberTestProject {

	public CucumberGuiceTestProject(String tags, IObjectRepository fa) {
		super(tags, fa);
	}

	@Override
	protected IConvertibleObject createClass(String file) {
		return new CucumberGuiceClass(file);
	}
}
