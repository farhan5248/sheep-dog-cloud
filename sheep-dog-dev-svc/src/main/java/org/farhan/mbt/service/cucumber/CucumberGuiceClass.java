package org.farhan.mbt.service.cucumber;

import org.farhan.dsl.lang.TestStepUtility;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ConstructorDeclaration;

public class CucumberGuiceClass extends CucumberClass {

	public CucumberGuiceClass(String thePath) {
		super(thePath);
	}

	protected void addConstructor(String name) {
		if (getType().getConstructors().isEmpty()) {
			ConstructorDeclaration constructor = getType().addConstructor(Modifier.Keyword.PUBLIC);
			constructor.addAndGetParameter(getObjectNameFromPath(thePath), "object");
			constructor.createBody().addStatement("super(object,\"" + TestStepUtility.getComponentName(name)
					+ "\",\"" + TestStepUtility.getObjectName(name) + "\");");
			constructor.addMarkerAnnotation("Inject");
			getType().addMarkerAnnotation("ScenarioScoped");
			theJavaClass.addImport("io.cucumber.guice.ScenarioScoped");
			theJavaClass.addImport("com.google.inject.Inject");
			theJavaClass.addImport(getPackageDeclaration().replaceFirst(".stepdefs.", ".objects.") + "."
					+ getObjectNameFromPath(thePath));
		}
	}

}
