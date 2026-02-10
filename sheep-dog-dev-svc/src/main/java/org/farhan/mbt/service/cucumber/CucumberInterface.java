package org.farhan.mbt.service.cucumber;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.farhan.dsl.lang.StepDefinitionRefFragments;
import org.farhan.dsl.lang.StepObjectRefFragments;

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

        String stepObjectName = StepObjectRefFragments.getAll(name);
        String stepDefinitionName = name.replace(stepObjectName, "").trim();
        if (StepObjectRefFragments.isObjectEdgeType(name)) {
            addMethod("transition", false);
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
                if (paramList.size() == 0) {
                    addMethod(
                            getSetOrAssert(name)
                                    + convertToPascalCase(StepDefinitionRefFragments.getPart(stepDefinitionName))
                                    + convertToPascalCase(StepDefinitionRefFragments.getStateType(stepDefinitionName)),
                            true);
                } else {
                    for (String param : paramList) {
                        addMethod(getSetOrAssert(name)
                                + convertToPascalCase(StepDefinitionRefFragments.getPart(stepDefinitionName))
                                + StringUtils.capitalize(convertToCamelCase(param)), true);
                    }
                }
            }
        }
    }

}
