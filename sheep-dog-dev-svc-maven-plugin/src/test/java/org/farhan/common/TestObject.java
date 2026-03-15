package org.farhan.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.farhan.dsl.grammar.ICell;
import org.farhan.dsl.grammar.IDescription;
import org.farhan.dsl.grammar.ILine;
import org.farhan.dsl.grammar.IRow;
import org.farhan.dsl.grammar.IStepDefinition;
import org.farhan.dsl.grammar.IStepObject;
import org.farhan.dsl.grammar.IStepParameters;
import org.farhan.dsl.grammar.ITable;
import org.farhan.dsl.grammar.ITestCase;
import org.farhan.dsl.grammar.ITestData;
import org.farhan.dsl.grammar.ITestDocument;
import org.farhan.dsl.grammar.ITestProject;
import org.farhan.dsl.grammar.ITestStep;
import org.farhan.dsl.grammar.ITestStepContainer;
import org.farhan.dsl.grammar.ITestSuite;
import org.farhan.dsl.grammar.IText;
import org.farhan.dsl.grammar.SheepDogBuilder;
import org.junit.jupiter.api.Assertions;

import io.cucumber.datatable.DataTable;

public abstract class TestObject {

    public static HashMap<String, Object> properties = new HashMap<String, Object>();
    protected String component;
    protected String object;

    protected static Object getProperty(String key) {
        return properties.get(key);
    }

    protected static void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    public void assertVertexStep(String partDesc, String partType, String stateType, String stateDesc) {
        putProperties(partDesc, partType, stateType, stateDesc);
        processInputOutputsStepDefinitionRef(stateDesc, "get", partDesc, partType, stateType);
        removeProperties();
    }

    public void assertVertexStep(String partDesc, String partType, String stateType, String stateDesc,
            DataTable dataTable) {
        putProperties(partDesc, partType, stateType, stateDesc);
        processInputOutputsStepDefinitionRef(stateDesc, "get", partDesc, partType, stateType);
        processInputOutputsTable(dataTable, "get", partDesc, partType, stateType);
        removeProperties();
    }

    public void assertVertexStep(String partDesc, String partType, String stateType, String stateDesc, String text) {
        putProperties(partDesc, partType, stateType, stateDesc);
        processInputOutputsStepDefinitionRef(stateDesc, "get", partDesc, partType, stateType);
        processInputOutputsText(text, "get", partDesc, partType, stateType);
        removeProperties();
    }

    public void doEdgeStep(String partDesc, String partType, String stateType, String stateDesc) {
        putProperties(partDesc, partType, stateType, stateDesc);
        processInputOutputsStepDefinitionRef(stateDesc, "set", partDesc, partType, stateType);
        removeProperties();
    }

    public void doEdgeStep(String partDesc, String partType, String stateType, String stateDesc, DataTable dataTable) {
        putProperties(partDesc, partType, stateType, stateDesc);
        processInputOutputsTable(dataTable, "set", partDesc, partType, stateType);
        processInputOutputsStepDefinitionRef(stateDesc, "set", partDesc, partType, stateType);
        removeProperties();
    }

    public void doEdgeStep(String partDesc, String partType, String stateType, String stateDesc, String text) {
        putProperties(partDesc, partType, stateType, stateDesc);
        processInputOutputsText(text, "set", partDesc, partType, stateType);
        processInputOutputsStepDefinitionRef(stateDesc, "set", partDesc, partType, stateType);
        removeProperties();
    }

    public void setVertexStep(String partDesc, String partType, String stateType, String stateDesc) {
        putProperties(partDesc, partType, stateType, stateDesc);
        processInputOutputsStepDefinitionRef(stateDesc, "set", partDesc, partType, stateType);
        removeProperties();
    }

    public void setVertexStep(String partDesc, String partType, String stateType, String stateDesc,
            DataTable dataTable) {
        putProperties(partDesc, partType, stateType, stateDesc);
        processInputOutputsStepDefinitionRef(stateDesc, "set", partDesc, partType, stateType);
        processInputOutputsTable(dataTable, "set", partDesc, partType, stateType);
        removeProperties();
    }

    public void setVertexStep(String partDesc, String partType, String stateType, String stateDesc, String text) {
        putProperties(partDesc, partType, stateType, stateDesc);
        processInputOutputsStepDefinitionRef(stateDesc, "set", partDesc, partType, stateType);
        processInputOutputsText(text, "set", partDesc, partType, stateType);
        removeProperties();
    }

    private String convertToPascalCase(String s) {
        StringBuilder result = new StringBuilder();
        for (String word : s.split("[ \\-\\(\\)/]+")) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)));
                if (word.length() > 1) {
                    result.append(word.substring(1));
                }
            }
        }
        return result.toString();
    }

    private void processInputOutputsStepDefinitionRef(String stateDesc, String operation, String partDesc,
            String partType, String stateType) {
        boolean negativeTest = false;
        if (stateType.contentEquals("isn't") || stateType.contentEquals("won't be")) {
            negativeTest = true;
        }
        processInputOutputs(stateDesc, "", operation, (partDesc + " " + partType).trim(), negativeTest);
    }

    private void processInputOutputsTable(DataTable dataTable, String operation, String partDesc, String partType,
            String stateType) {
        boolean negativeTest = false;
        if (stateType.contentEquals("isn't") || stateType.contentEquals("won't be")) {
            negativeTest = true;
        }
        processInputOutputs(dataTable, operation, (partDesc + " " + partType).trim(), negativeTest);
    }

    private void processInputOutputsText(String text, String operation, String partDesc, String partType,
            String stateType) {
        boolean negativeTest = false;
        if (stateType.contentEquals("isn't") || stateType.contentEquals("won't be")) {
            negativeTest = true;
        }
        processInputOutputs("Content", text, operation, (partDesc + " " + partType).trim(), negativeTest);
    }

    protected String listToString(List<?> list) {
        if (list == null)
            return null;
        if (list.isEmpty())
            return "";
        return list.toString();
    }

    protected void processInputOutputs(DataTable dataTable, String operation, String sectionName,
            boolean negativeTest) {

        List<List<String>> data = dataTable.asLists();
        ArrayList<String> headers = new ArrayList<String>();
        for (String cell : data.get(0)) {
            headers.add(cell);
        }
        for (int i = 1; i < data.size(); i++) {
            HashMap<String, String> row = new HashMap<String, String>();
            for (int j = 0; j < headers.size(); j++) {
                row.put(headers.get(j), data.get(i).get(j));
            }
            for (String fieldName : headers) {
                try {
                    Object returnValue = this.getClass()
                            .getMethod(
                                    operation + convertToPascalCase(sectionName) + convertToPascalCase(fieldName),
                                    HashMap.class)
                            .invoke(this, row);
                    if (operation.equals("get") && !fieldName.contains("Node Path")
                            && !fieldName.equals("Tag List")) {
                        String expected = replaceKeyword(row.get(fieldName));
                        String actual = returnValue == null ? null : returnValue.toString();
                        if (fieldName.equals("State") && (expected.equals("Absent") || expected.equals("Empty")
                                || expected.equals("Present"))) {
                            String mappedActual;
                            if (actual == null)
                                mappedActual = "Absent";
                            else if (actual.isEmpty())
                                mappedActual = "Empty";
                            else
                                mappedActual = "Present";
                            if (negativeTest) {
                                Assertions.assertNotEquals(expected, mappedActual);
                            } else {
                                Assertions.assertEquals(expected, mappedActual);
                            }
                        } else {
                            if (negativeTest) {
                                Assertions.assertNotEquals(expected, actual);
                            } else {
                                Assertions.assertEquals(expected, actual);
                            }
                        }
                    }
                } catch (Exception e) {
                    Assertions.fail(e);
                }
            }
        }
    }

    protected void processInputOutputs(String key, String value, String operation, String sectionName,
            boolean negativeTest) {

        HashMap<String, String> row = new HashMap<String, String>();
        row.put(key, value);
        try {
            Object returnValue = this.getClass()
                    .getMethod(operation + convertToPascalCase(sectionName) + convertToPascalCase(key),
                            HashMap.class)
                    .invoke(this, row);
            if (operation.equals("get")) {
                String actual = returnValue == null ? null : returnValue.toString();
                if (negativeTest) {
                    if (value.equals("true")) {
                        Assertions.assertNull(actual);
                    } else if (!value.isEmpty()) {
                        Assertions.assertNotEquals(replaceKeyword(value), actual);
                    }
                } else {
                    if (value.equals("true")) {
                        Assertions.assertNotNull(actual);
                    } else if (!value.isEmpty()) {
                        Assertions.assertEquals(replaceKeyword(value), actual);
                    }
                }
            }
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    protected String replaceKeyword(String value) {
        if (value.contentEquals("empty")) {
            return "";
        } else {
            return value;
        }
    }

    protected void setComponent(String component) {
        this.component = component;
    }

    protected void setObject(String object) {
        this.object = object;
    }

    private void putProperties(String partDesc, String partType, String stateType, String stateDesc) {
        properties.put("partDesc", partDesc);
        properties.put("partType", partType);
        properties.put("stateType", stateType);
        properties.put("stateDesc", stateDesc);
    }

    private void removeProperties() {
        properties.remove("partDesc");
        properties.remove("partType");
        properties.remove("stateType");
        properties.remove("stateDesc");
    }

    protected String listToString(ArrayList<?> list) {
        StringBuilder sb = new StringBuilder();
        for (Object item : list) {
            sb.append("\n").append(item.toString());
        }
        return sb.toString();
    }

    // === Node navigation ===
    protected String getFullNameFromPath() {
        return object.replaceFirst("^src/test/resources/[^/]+/", "");
    }

    private static Object getDocumentFromNode(Object node) {
        Object current = node;
        while (current != null && !(current instanceof ITestDocument)) {
            if (current instanceof ICell) {
                current = ((ICell) current).getParent();
            } else if (current instanceof IRow) {
                current = ((IRow) current).getParent();
            } else if (current instanceof ITable) {
                current = ((ITable) current).getParent();
            } else if (current instanceof IText) {
                current = ((IText) current).getParent();
            } else if (current instanceof ILine) {
                current = ((ILine) current).getParent();
            } else if (current instanceof IDescription) {
                current = ((IDescription) current).getParent();
            } else if (current instanceof ITestStep) {
                current = ((ITestStep) current).getParent();
            } else if (current instanceof ITestData) {
                current = ((ITestData) current).getParent();
            } else if (current instanceof ITestStepContainer) {
                current = ((ITestStepContainer) current).getParent();
            } else if (current instanceof IStepParameters) {
                current = ((IStepParameters) current).getParent();
            } else if (current instanceof IStepDefinition) {
                current = ((IStepDefinition) current).getParent();
            } else if (current instanceof ITestProject) {
                return null;
            } else {
                return null;
            }
        }
        return current;
    }

    private Object getChildNode(Object parent, String elementType, int index) {
        switch (elementType) {
        case "Table":
            if (parent instanceof ITestStep)
                return ((ITestStep) parent).getTable();
            if (parent instanceof ITestData)
                return ((ITestData) parent).getTable();
            return ((IStepParameters) parent).getTable();
        case "Text":
            return ((ITestStep) parent).getText();
        case "Description":
            if (parent instanceof ITestSuite)
                return ((ITestSuite) parent).getDescription();
            if (parent instanceof ITestStepContainer)
                return ((ITestStepContainer) parent).getDescription();
            if (parent instanceof IStepObject)
                return ((IStepObject) parent).getDescription();
            if (parent instanceof IStepDefinition)
                return ((IStepDefinition) parent).getDescription();
            if (parent instanceof IStepParameters)
                return ((IStepParameters) parent).getDescription();
            return ((ITestData) parent).getDescription();
        case "TestStepContainerList":
            return ((ITestSuite) parent).getTestStepContainer(index);
        case "TestStepList":
            return ((ITestStepContainer) parent).getTestStep(index);
        case "RowList":
            return ((ITable) parent).getRow(index);
        case "CellList":
            return ((IRow) parent).getCell(index);
        case "StepDefinitionList":
            return ((IStepObject) parent).getStepDefinition(index);
        case "StepParametersList":
            return ((IStepDefinition) parent).getStepParameters(index);
        case "LineList":
            return ((IDescription) parent).getLine(index);
        case "TestDataList":
            return ((ITestCase) parent).getTestData(index);
        default:
            throw new IllegalArgumentException("Unknown element type: " + elementType);
        }
    }

    private Object getOrCreateNode(Object parent, String elementType, int index) {
        switch (elementType) {
        case "Table": {
            Object child = getChildNode(parent, elementType, index);
            if (child != null)
                return child;
            if (parent instanceof ITestStep)
                return SheepDogBuilder.createTable((ITestStep) parent);
            if (parent instanceof ITestData)
                return SheepDogBuilder.createTable((ITestData) parent);
            return SheepDogBuilder.createTable((IStepParameters) parent);
        }
        case "Description": {
            Object child = getChildNode(parent, elementType, index);
            if (child != null)
                return child;
            if (parent instanceof ITestSuite)
                return SheepDogBuilder.createDescription((ITestSuite) parent);
            if (parent instanceof ITestStepContainer)
                return SheepDogBuilder.createDescription((ITestStepContainer) parent);
            if (parent instanceof IStepObject)
                return SheepDogBuilder.createDescription((IStepObject) parent);
            if (parent instanceof IStepDefinition)
                return SheepDogBuilder.createDescription((IStepDefinition) parent);
            if (parent instanceof IStepParameters)
                return SheepDogBuilder.createDescription((IStepParameters) parent);
            return SheepDogBuilder.createDescription((ITestData) parent);
        }
        default:
            try {
                return getChildNode(parent, elementType, index);
            } catch (IndexOutOfBoundsException e) {
                switch (elementType) {
                case "TestStepContainerList":
                    return SheepDogBuilder.createTestCase((ITestSuite) parent, "Test Case");
                case "TestStepList":
                    return SheepDogBuilder.createTestStep((ITestStepContainer) parent, "");
                case "RowList":
                    return SheepDogBuilder.createRow((ITable) parent);
                case "CellList":
                    return SheepDogBuilder.createCell((IRow) parent, "");
                case "StepDefinitionList":
                    return SheepDogBuilder.createStepDefinition((IStepObject) parent, "");
                case "StepParametersList":
                    return SheepDogBuilder.createStepParameters((IStepDefinition) parent, "");
                case "LineList":
                    return SheepDogBuilder.createLine((IDescription) parent, "");
                case "TestDataList":
                    return SheepDogBuilder.createTestData((ITestCase) parent, "");
                default:
                    throw new IllegalArgumentException("Unknown element type: " + elementType);
                }
            }
        }
    }

    private void navigateToNode(String path, boolean create) {
        String[] parts = path.split("/");
        Object current = getDocumentFromNode(getProperty("cursor"));
        int i = 0;
        while (i < parts.length && current != null) {
            String elementType = parts[i];
            if (elementType.endsWith("List")) {
                if (i + 1 >= parts.length || !parts[i + 1].matches("\\d+")) {
                    break;
                }
                int index = Integer.parseInt(parts[i + 1]) - 1;
                if (create) {
                    if (elementType.equals("TestStepContainerList") && i + 2 < parts.length
                            && parts[i + 2].equals("TestDataList")) {
                        Object container = null;
                        try {
                            container = getChildNode(current, elementType, index);
                        } catch (IndexOutOfBoundsException e) {
                        }
                        if (container instanceof ITestCase) {
                            current = container;
                        } else {
                            current = SheepDogBuilder.createTestCase((ITestSuite) current, "Test Case");
                        }
                    } else {
                        current = getOrCreateNode(current, elementType, index);
                    }
                } else {
                    try {
                        current = getChildNode(current, elementType, index);
                    } catch (IndexOutOfBoundsException e) {
                        setProperty("cursor", null);
                        return;
                    }
                }
                i += 2;
            } else {
                if (create && elementType.equals("Text")) {
                    break;
                }
                current = create ? getOrCreateNode(current, elementType, 0)
                                 : getChildNode(current, elementType, 0);
                i++;
            }
            if (current != null)
                setProperty("cursor", current);
        }
    }

    protected void createNodeDependencies(String path) {
        navigateToNode(path, true);
    }

    protected void setCursorAtNode(String path) {
        navigateToNode(path, false);
    }

}
