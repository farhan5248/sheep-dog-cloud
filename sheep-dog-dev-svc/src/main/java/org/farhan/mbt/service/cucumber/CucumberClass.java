package org.farhan.mbt.service.cucumber;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.farhan.dsl.lang.TestStepUtility;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;

public class CucumberClass extends CucumberJava {

	public CucumberClass(String thePath) {
		super(thePath);
		getType().addExtendedType("TestSteps");

		theJavaClass.addImport("org.farhan.common.TestSteps");
		theJavaClass.addImport("io.cucumber.java.en.Given");
	}

	protected void addConstructor(String name) {
		// TODO create no component, single component, component with package tests
		if (getType().getConstructors().isEmpty()) {
			ConstructorDeclaration constructor = getType().addConstructor(Modifier.Keyword.PUBLIC);
			constructor.createBody()
					.addStatement("super(\"" + getObjectNameFromPath(thePath) + "\",\""
							+ TestStepUtility.getComponentName(name) + "\",\""
							+ TestStepUtility.getObjectName(name) + "\");");
		}
	}

	public void addStepDefinition(String name, ArrayList<String> paramList) throws Exception {
		addConstructor(name);
		MethodDeclaration aMethod = addMethod(
				convertToCamelCase(TestStepUtility.getPredicate(name)).replace("'", ""));
		BlockStmt body = aMethod.getBody().get();
		if (body.isEmpty()) {
			if (aMethod.getAnnotations().isEmpty()) {
				aMethod.addSingleMemberAnnotation("Given", "\"^" + name + "$\"");
			}
			body = aMethod.createBody();
			if (paramList.size() == 0 && !TestStepUtility.isEdge(name)) {
				body.addStatement("object" + getCallForInputOutputsForState(name) + ";");
			} else if (paramList.size() == 1 && paramList.get(0).contentEquals("Content")) {
				addParameter(aMethod, "String", "docString");
				body.addStatement("object" + getCallForInputOutputsForDocString(name) + ";");
			} else if (paramList.size() >= 1) {
				theJavaClass.addImport("io.cucumber.datatable.DataTable");
				addParameter(aMethod, "DataTable", "dataTable");
				body.addStatement("object" + getCallForInputOutputsForDataTable(name) + ";");
			}
			if (TestStepUtility.isEdge(name)) {
				body.addStatement("object" + getCallForTransition() + ";");
			}
		}
	}

	private String getCallForInputOutputsForDataTable(String step) throws Exception {
		return "." + getSetOrAssert(step) + "InputOutputs(" + "dataTable" + getSectionArg(step) + getNegativeArg(step)
				+ ")";
	}

	private String getCallForInputOutputsForDocString(String step) throws Exception {
		return "." + getSetOrAssert(step) + "InputOutputs(" + "\"Content\", docString" + getSectionArg(step)
				+ getNegativeArg(step) + ")";
	}

	private String getCallForInputOutputsForState(String step) throws Exception {
		return "." + getSetOrAssert(step) + "InputOutputs(\""
				+ StringUtils.capitalize(TestStepUtility.getStateType(step)) + "\"" + getSectionArg(step)
				+ getNegativeArg(step) + ")";
	}

	private String getCallForTransition() {
		return ".transition()";
	}

	protected String getInterfaceName(String step) {
		String nameParts[] = TestStepUtility.getObjectName(step).split("/");
		String name = nameParts[nameParts.length - 1];
		return convertToPascalCase(name) + StringUtils.capitalize(TestStepUtility.getObjectType(step));
	}

	private String getNegativeArg(String step) {
		if (TestStepUtility.isNegativeStep(step)) {
			return ", true";
		} else {
			return "";
		}
	}

	protected String getObjectNameFromPath(String thePath) {
		String[] pathParts = thePath.split("/");
		String componentName = "";
		if (pathParts.length > 7) {
			// why 7? src-gen, test, java, org, farhan, stepdefs
			componentName = pathParts[6];
		}
		String objectName = pathParts[pathParts.length - 1].replace("Steps.java", "")
				.replaceFirst("(?i)^" + componentName, "");
		return objectName;
	}

	private String getSectionArg(String step) {
		String section = convertToPascalCase(TestStepUtility.getDetails(step));
		if (!section.isEmpty()) {
			section = ", \"" + section + "\"";
		}
		return section;
	}
}
