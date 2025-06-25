package org.farhan.mbt.next.cucumber;

import org.farhan.mbt.core.UMLStepObject;
import org.farhan.mbt.core.UMLTestProject;
import org.farhan.mbt.core.UMLTestSuite;

public class CucumberPathConverter {

	private UMLTestProject model;
	private CucumberTestProject project;

	public CucumberPathConverter(UMLTestProject model, CucumberTestProject project) {
		this.model = model;
		this.project = project;
	}

	public String convertUMLPath(String path) {
		String qualifiedName = path.replace(",", "").trim();
		qualifiedName = qualifiedName.replaceFirst(project.getFileExt(project.TEST_CASES) + "$", "");
		qualifiedName = qualifiedName.replaceFirst("^" + project.getDir(project.TEST_CASES), "");
		qualifiedName = qualifiedName.replace("/", "::");
		qualifiedName = "pst::" + model.TEST_CASES + qualifiedName;
		return qualifiedName;
	}

	public String findUMLPath(String path) {
		for (UMLTestSuite co : model.getTestSuiteList()) {
			if (convertFilePath(co.getUmlElement().getQualifiedName(), project.TEST_CASES).contentEquals(path)) {
				return co.getUmlElement().getQualifiedName();
			}
		}
		for (UMLStepObject co : model.getStepObjectList()) {
			if (convertFilePath(co.getUmlElement().getQualifiedName(), project.TEST_STEPS).contentEquals(path)) {
				return co.getUmlElement().getQualifiedName();
			}
			if (convertFilePath(co.getUmlElement().getQualifiedName(), project.TEST_OBJECTS).contentEquals(path)) {
				return co.getUmlElement().getQualifiedName();
			}
		}
		return null;
	}

	public String convertFilePath(String path, String layer) {
		String[] pathParts = path.split("::");
		String componentName = pathParts[2];
		String objectName = pathParts[pathParts.length - 1];
		String newComponentName = getComponentName(path);
		String newObjectName = getObjectName(path);

		if (layer.contentEquals(project.TEST_CASES)) {
			path = path.replace("pst::" + model.TEST_CASES, "");
		} else {
			if (layer.contentEquals(project.TEST_STEPS)) {
				path = path.replace("pst::" + model.TEST_STEPS + "::" + componentName,
						"::" + newComponentName.toLowerCase());
				path = path.replaceFirst(objectName + "$", newComponentName + newObjectName + "Steps");
			}
			if (layer.contentEquals(project.TEST_OBJECTS)) {
				path = path.replace("pst::" + model.TEST_STEPS + "::" + componentName,
						"::" + newComponentName.toLowerCase());
				path = path.replaceFirst(objectName + "$", newObjectName);
			}
			path = path.replace(" ", "");
			path = path.replace("-", "");
		}

		path = path.replace("::", "/");
		path = project.getDir(layer) + path + project.getFileExt(layer);

		return path;
	}

	private String getComponentName(String path) {
		String[] pathParts = path.split("::");
		String name = pathParts[2].replaceFirst("\\s[^\\s]+$", "");
		name = removeDelimiterAndCapitalize(name, "\\.");
		name = removeDelimiterAndCapitalize(name, "\\-");
		name = removeDelimiterAndCapitalize(name, " ");
		return name;
	}

	private String getObjectName(String path) {
		String[] pathParts = path.split("::");
		String name = pathParts[pathParts.length - 1];
		name = removeDelimiterAndCapitalize(name, "\\.");
		name = removeDelimiterAndCapitalize(name, "\\-");
		name = removeDelimiterAndCapitalize(name, " ");
		return name;
	}

	private String removeDelimiterAndCapitalize(String name, String delimiter) {
		String[] parts = name.split(delimiter);
		StringBuilder sb = new StringBuilder();
		for (String part : parts) {
			sb.append(part.substring(0, 1).toUpperCase()).append(part.substring(1));
		}
		return sb.toString();
	}
}
