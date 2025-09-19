package org.farhan.mbt.service.cucumber;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeSet;
import org.apache.commons.lang3.StringUtils;
import org.farhan.dsl.lang.TestStepUtility;
import org.farhan.mbt.core.IConvertibleObject;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier.Keyword;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public abstract class CucumberJava implements IConvertibleObject {

	protected String thePath;
	protected CompilationUnit theJavaClass;

	public CucumberJava(String thePath) {
		this.thePath = thePath;
		// TODO if the contents are parsed in, then this might create duplication. So
		// move this out or check if these elements exist
		theJavaClass = new CompilationUnit();
		theJavaClass.setStorage(Paths.get(thePath));
		ClassOrInterfaceDeclaration javaClassType = new ClassOrInterfaceDeclaration();
		{
			// TODO maybe reuse getObjectNameFromPath
			String[] pathParts = thePath.split("/");
			javaClassType.setName(pathParts[pathParts.length - 1].replace(".java", ""));
		}
		javaClassType.setPublic(true);
		theJavaClass.addType(javaClassType);
		theJavaClass.setPackageDeclaration(getPackageDeclaration());
	}

	protected void addParameter(MethodDeclaration aMethod, String type, String name) {
		if (aMethod.getParameters().isEmpty()) {
			aMethod.addParameter(type, name);
		}
	}

	public abstract void addStepDefinition(String name, ArrayList<String> paramList) throws Exception;

	@Override
	public Object get() {
		return theJavaClass;
	}

	public MethodDeclaration addMethod(String methodName) {
		if (getType().getMethodsByName(methodName).isEmpty()) {
			return getType().addMethod(methodName, Keyword.PUBLIC);
		} else {
			return getType().getMethodsByName(methodName).getFirst();
		}
	}

	protected String getPackageDeclaration() {
		String packageName;
		packageName = thePath.replaceAll("\\" + "/" + "[^\\" + "/" + "]*$", "");
		packageName = packageName.replace("-", "");
		packageName = packageName.replace("/", ".");
		packageName = packageName.replaceFirst("^srcgen\\.test\\.java\\.org\\.farhan", "org.farhan");
		return packageName.toLowerCase();
	}

	@Override
	public String getPath() {
		return thePath;
	}

	protected String getSetOrAssert(String stepName) throws Exception {
		if (TestStepUtility.getStateModality(stepName).endsWith("be")) {
			return "assert";
		} else {
			return "set";
		}
	}

	protected ClassOrInterfaceDeclaration getType() {
		return (ClassOrInterfaceDeclaration) theJavaClass.getType(0);
	}

	@Override
	public void parse(String text) throws Exception {
		try {
			if (text.isEmpty()) {
				return;
			}
			if (getType().getMethods().isEmpty()) {
				InputStream content = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
				theJavaClass = new JavaParser().parse(content).getResult().get();
			}
		} catch (Exception e) {
			throw new Exception("There was a problem parsing file: " + thePath);
		}
	}

	protected String convertToCamelCase(String text, String delimiter) {
		String[] nameParts = text.split(delimiter);
		text = "";
		for (String s : nameParts) {
			text += StringUtils.capitalize(s);
		}
		return StringUtils.uncapitalize(text);
	}

	protected String convertToPascalCase(String text) {
		return StringUtils.capitalize(convertToCamelCase(text));
	}

	protected String convertToCamelCase(String text) {
		text = convertToCamelCase(text, " ");
		text = convertToCamelCase(text, "\\.");
		text = convertToCamelCase(text, "\\-");
		text = convertToCamelCase(text, "/");
		text = convertToCamelCase(text, ",");
		return text;
	}

	public String toString() {
		// TODO make tests for this
		TreeSet<String> sorted = new TreeSet<String>();
		for (int i = theJavaClass.getImports().size(); i > 0; i--) {
			sorted.add(theJavaClass.getImports().get(i - 1).getNameAsString());
			theJavaClass.getImports().remove(i - 1);
		}
		for (String name : sorted) {
			theJavaClass.addImport(name);
		}
		return theJavaClass.toString();
	}

}
