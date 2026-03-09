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
    protected HashMap<String, Object> properties = new HashMap<String, Object>();

    public void assertInputOutputsDataTable(DataTable dataTable) {
        processInputOutputs(dataTable, "assert", "", false);
    }

    public void assertInputOutputsDataTable(DataTable dataTable, String sectionName) {
        processInputOutputs(dataTable, "assert", sectionName, false);
    }

    public void assertInputOutputsDataTable(DataTable dataTable, String sectionName, boolean negativeTest) {
        processInputOutputs(dataTable, "assert", sectionName, negativeTest);
    }

    public void assertInputOutputsDocString(String key, String value) {
        processInputOutputs(key, value, "assert", "", false);
    }

    public void assertInputOutputsState(String key) {
        processInputOutputs(key, "true", "assert", "", false);
    }

    public void assertInputOutputsState(String key, boolean negativeTest) {
        processInputOutputs(key, "true", "assert", "", negativeTest);
    }

    public void assertInputOutputsState(String key, String sectionName) {
        processInputOutputs(key, "", "assert", sectionName, false);
    }

    public void assertInputOutputsState(String key, String value, String sectionName) {
        processInputOutputs(key, value, "assert", sectionName, false);
    }

    public void assertInputOutputsState(String key, String sectionName, boolean negativeTest) {
        processInputOutputs(key, "", "assert", sectionName, negativeTest);
    }

    public void setInputOutputsDataTable(DataTable dataTable) {
        processInputOutputs(dataTable, "set", "", false);
    }

    public void setInputOutputsDataTable(DataTable dataTable, String sectionName) {
        processInputOutputs(dataTable, "set", sectionName, false);
    }

    public void setInputOutputsDataTable(DataTable dataTable, String sectionName, boolean negativeTest) {
        processInputOutputs(dataTable, "set", sectionName, negativeTest);
    }

    public void setInputOutputsDocString(String key, String value) {
        processInputOutputs(key, value, "set", "", false);
    }

    public void setInputOutputsState(String key) {
        processInputOutputs(key, "true", "set", "", false);
    }

    public void setInputOutputsState(String key, boolean negativeTest) {
        processInputOutputs(key, "true", "set", "", negativeTest);
    }

    public void setPart(String part) {
        properties.put("part", part);
    }

    public void transition() {
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

    protected String listToString(ArrayList<?> proposals) {
        StringBuilder sb = new StringBuilder();
        for (Object p : proposals) {
            sb.append("\n").append(p.toString());
        }
        return sb.toString();
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
                    this.getClass().getMethod(operation + convertToPascalCase(sectionName)
                            + "Negative", HashMap.class).invoke(this, row);
                } catch (Exception e) {
                    Assertions.fail(e);
                }
            } else {
                for (String fieldName : headers) {
                    try {
                        this.getClass().getMethod(operation + convertToPascalCase(sectionName)
                                + convertToPascalCase(fieldName), HashMap.class).invoke(this, row);
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
                this.getClass().getMethod(
                        operation + convertToPascalCase(sectionName) + "Negative",
                        HashMap.class).invoke(this, row);
            } else {
                this.getClass().getMethod(
                        operation + convertToPascalCase(sectionName) + convertToPascalCase(key),
                        HashMap.class).invoke(this, row);
            }
        } catch (Exception e) {
            Assertions.fail(e);
        }
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

    protected void setPath(String path) {
        properties.put("path", path);
    }

    public void setInputOutputsState(String key, String sectionName) {
        processInputOutputs(key, "", "set", sectionName, false);
    }

    public void setInputOutputsState(String key, String sectionName, boolean negativeTest) {
        processInputOutputs(key, "", "set", sectionName, negativeTest);
    }
}
