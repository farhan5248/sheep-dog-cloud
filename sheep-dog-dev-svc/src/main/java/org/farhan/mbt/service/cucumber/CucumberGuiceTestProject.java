package org.farhan.mbt.service.cucumber;

import org.farhan.mbt.core.IConvertibleObject;
import org.farhan.dsl.lang.IResourceRepository;

public class CucumberGuiceTestProject extends CucumberTestProject {

	public CucumberGuiceTestProject(String tags, IResourceRepository fa) {
		super(tags, fa);
	}

	@Override
	protected IConvertibleObject createClass(String file) {
		return new CucumberGuiceClass(file);
	}
}
