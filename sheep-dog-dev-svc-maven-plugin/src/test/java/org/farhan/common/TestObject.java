package org.farhan.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Assertions;

import com.google.inject.Injector;
import com.google.inject.Key;

import io.cucumber.datatable.DataTable;

public abstract class TestObject {

    protected static Injector injector;
    public HashMap<String, Object> properties = new HashMap<String, Object>();

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

    protected TestObject getTestObjectClass(String contains, String startsWith) throws Exception {
        for (Key<?> b : injector.getBindings().keySet()) {
            if (b.getTypeLiteral().toString().contains(contains)
                    && b.getTypeLiteral().toString().startsWith(startsWith)) {
                TestObject object = (TestObject) injector.getInstance(b);
                if (object.properties.size() > 1) {
                    return object;
                }
            }
        }
        return null;
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
            if (negativeTest) {
                try {
                    Object returnValue = this.getClass()
                            .getMethod(operation + convertToPascalCase(sectionName) + "Negative", HashMap.class)
                            .invoke(this, row);
                    if (operation.equals("get")) {
                        Assertions.assertNotNull(returnValue == null ? null : returnValue.toString());
                    }
                } catch (Exception e) {
                    Assertions.fail(e);
                }
            } else {
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
                                Assertions.assertEquals(expected, mappedActual);
                            } else {
                                Assertions.assertEquals(expected, actual);
                            }
                        }
                    } catch (Exception e) {
                        Assertions.fail(e);
                    }
                }
            }
        }
    }

    protected void processInputOutputs(String key, String value, String operation, String sectionName,
            boolean negativeTest) {

        HashMap<String, String> row = new HashMap<String, String>();
        row.put(key, value);
        try {
            if (negativeTest) {
                this.getClass().getMethod(operation + convertToPascalCase(sectionName) + "Negative", HashMap.class)
                        .invoke(this, row);
            } else {
                Object returnValue = this.getClass()
                        .getMethod(operation + convertToPascalCase(sectionName) + convertToPascalCase(key),
                                HashMap.class)
                        .invoke(this, row);
                if (operation.equals("get")) {
                    String actual = returnValue == null ? null : returnValue.toString();
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
        properties.put("component", component);
    }

    protected void setObject(String object) {
        properties.put("object", object);
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
}
