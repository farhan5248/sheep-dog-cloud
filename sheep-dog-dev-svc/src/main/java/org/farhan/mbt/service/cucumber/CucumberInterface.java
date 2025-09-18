package org.farhan.mbt.service.cucumber;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.farhan.dsl.lang.TestStepUtility;

import com.github.javaparser.ast.body.MethodDeclaration;

public class CucumberInterface extends CucumberJava {

	public CucumberInterface(String thePath) {
		super(thePath);
		getType().setInterface(true);
	}

	public MethodDeclaration addMethod(String methodName, boolean hasParams) {
		MethodDeclaration aMethod = super.addMethod(methodName);
		aMethod.removeBody();
		if (hasParams) {
			theJavaClass.addImport("java.util.HashMap");
			addParameter(aMethod, "HashMap<String, String>", "keyMap");
		}
		return aMethod;
	}

	public void addStepDefinition(String name, ArrayList<String> paramList) throws Exception {

		if (TestStepUtility.isEdge(name)) {
			addMethod("transition", false);
			for (String param : paramList) {
				addMethod(getSetOrAssert(name) + convertToPascalCase(TestStepUtility.getDetails(name))
						+ StringUtils.capitalize(convertToCamelCase(param)), true);
			}
		} else {
			if (TestStepUtility.isNegativeStep(name)) {
				addMethod(getSetOrAssert(name) + convertToPascalCase(TestStepUtility.getDetails(name)) + "Negative",
						true);
			} else {
				if (paramList.size() == 0) {
					addMethod(getSetOrAssert(name) + convertToPascalCase(TestStepUtility.getDetails(name))
							+ convertToPascalCase(TestStepUtility.getStateType(name)), true);
				} else {
					for (String param : paramList) {
						addMethod(getSetOrAssert(name) + convertToPascalCase(TestStepUtility.getDetails(name))
								+ StringUtils.capitalize(convertToCamelCase(param)), true);
					}
				}
			}
		}
	}

}
