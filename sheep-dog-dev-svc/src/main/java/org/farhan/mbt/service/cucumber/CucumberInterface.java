package org.farhan.mbt.service.cucumber;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.farhan.dsl.grammar.StepDefinitionRefFragments;
import org.farhan.dsl.grammar.StepObjectRefFragments;

import com.github.javaparser.ast.body.MethodDeclaration;

public class CucumberInterface extends CucumberJava {

    public CucumberInterface(String thePath) {
        super(thePath);
        getType().setInterface(true);
    }

    public MethodDeclaration addMethod(String methodName, boolean hasParams) {
        MethodDeclaration aMethod = super.addMethod(methodName);
        aMethod.removeBody();
        if (methodName.startsWith("get")) {
            aMethod.setType("String");
        }
        if (hasParams) {
            theJavaClass.addImport("java.util.HashMap");
            addParameter(aMethod, "HashMap<String, String>", "keyMap");
        }
        return aMethod;
    }

    @Override
    protected String getSetOrAssert(String stepName) throws Exception {
        String result = super.getSetOrAssert(stepName);
        if (result.equals("assert")) {
            return "get";
        }
        return result;
    }

    public void addStepDefinition(String name, ArrayList<String> paramList) throws Exception {

        String stepObjectName = StepObjectRefFragments.getAll(name);
        String stepDefinitionName = name.replace(stepObjectName, "").trim();
        if (!StepObjectRefFragments.getObjectEdgeType(name).isEmpty()) {
            addMethod("transition", false);
            addMethod(
                    getSetOrAssert(name)
                            + convertToPascalCase(StepDefinitionRefFragments.getPart(stepDefinitionName))
                            + convertToPascalCase(StepDefinitionRefFragments.getStateDesc(stepDefinitionName)),
                    true);
            for (String param : paramList) {
                addMethod(getSetOrAssert(name)
                        + convertToPascalCase(StepDefinitionRefFragments.getPart(stepDefinitionName))
                        + StringUtils.capitalize(convertToCamelCase(param)), true);
            }
        } else {
            if (isNegativeStep(name)) {
                addMethod(getSetOrAssert(name)
                        + convertToPascalCase(StepDefinitionRefFragments.getPart(stepDefinitionName)) + "Negative",
                        true);
            } else {
                addMethod(
                        getSetOrAssert(name)
                                + convertToPascalCase(StepDefinitionRefFragments.getPart(stepDefinitionName))
                                + convertToPascalCase(StepDefinitionRefFragments.getStateDesc(stepDefinitionName)),
                        true);
                for (String param : paramList) {
                    addMethod(getSetOrAssert(name)
                            + convertToPascalCase(StepDefinitionRefFragments.getPart(stepDefinitionName))
                            + StringUtils.capitalize(convertToCamelCase(param)), true);
                }
            }
        }
    }

}
