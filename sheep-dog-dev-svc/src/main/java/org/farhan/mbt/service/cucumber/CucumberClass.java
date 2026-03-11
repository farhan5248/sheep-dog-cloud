package org.farhan.mbt.service.cucumber;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.farhan.dsl.grammar.StepDefinitionRefFragments;
import org.farhan.dsl.grammar.StepObjectRefFragments;

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
                            + StepObjectRefFragments.getComponentName(name) + "\",\""
                            + StepObjectRefFragments.getObjectName(name) + "\");");
        }
    }

    public void addStepDefinition(String name, ArrayList<String> paramList) throws Exception {
        addConstructor(name);
        String stepObjectName = StepObjectRefFragments.getAll(name);
        String stepDefinitionName = name.replace(stepObjectName, "").trim();
        MethodDeclaration aMethod = addMethod(convertToCamelCase(stepDefinitionName.replace("'", "")));
        BlockStmt body = aMethod.getBody().get();
        if (body.isEmpty()) {
            if (aMethod.getAnnotations().isEmpty()) {
                aMethod.addSingleMemberAnnotation("Given", "\"^" + name + "$\"");
            }
            body = aMethod.createBody();
            boolean isEdge = !StepObjectRefFragments.getObjectEdgeType(name).isEmpty();
            String stepType = isEdge ? "EdgeStep" : "VertexStep";
            String setOrAssert = isEdge ? "do" : getSetOrAssert(name);
            String partDesc = StepDefinitionRefFragments.getPartDesc(stepDefinitionName);
            String partType = StepDefinitionRefFragments.getPartType(stepDefinitionName);
            String stateType = StepDefinitionRefFragments.getStateType(stepDefinitionName);
            String stateDesc = StepDefinitionRefFragments.getStateDesc(stepDefinitionName);
            String args = "\"" + partDesc + "\", \"" + partType + "\", \"" + stateType + "\", \"" + stateDesc + "\"";
            if (paramList.size() == 1 && paramList.get(0).contentEquals("Content")) {
                addParameter(aMethod, "String", "docString");
                body.addStatement("object." + setOrAssert + stepType + "(" + args + ", docString);");
            } else if (paramList.size() >= 1) {
                theJavaClass.addImport("io.cucumber.datatable.DataTable");
                addParameter(aMethod, "DataTable", "dataTable");
                body.addStatement("object." + setOrAssert + stepType + "(" + args + ", dataTable);");
            } else {
                body.addStatement("object." + setOrAssert + stepType + "(" + args + ");");
            }
        }
    }

    protected String getInterfaceName(String step) {
        String nameParts[] = StepObjectRefFragments.getObjectName(step).split("/");
        String name = nameParts[nameParts.length - 1];
        return convertToPascalCase(name) + StringUtils.capitalize(StepObjectRefFragments.getObjectType(step));
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

}
