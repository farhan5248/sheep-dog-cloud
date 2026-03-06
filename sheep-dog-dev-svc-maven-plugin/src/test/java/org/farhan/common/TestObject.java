package org.farhan.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Assertions;

import io.cucumber.datatable.DataTable;

public abstract class TestObject {

    protected HashMap<String, String> attributes = new HashMap<String, String>();

    public void assertInputOutputsDataTable(DataTable dataTable) {
        processInputOutputs(dataTable, "assert", "");
    }

    public void assertInputOutputsDataTable(DataTable dataTable, String sectionName) {
        processInputOutputs(dataTable, "assert", sectionName);
    }

    public void assertInputOutputsDataTable(DataTable dataTable, String sectionName, boolean negativeTest) {
        processInputOutputs(dataTable, "assert", sectionName);
    }

    public void assertInputOutputsDocString(String key, String value) {
        HashMap<String, String> row = new HashMap<String, String>();
        row.put(key, value);
        processInputOutputs(row, "assert", "");
    }

    public void assertInputOutputsState(String key) {
        HashMap<String, String> row = new HashMap<String, String>();
        row.put(key, "true");
        processInputOutputs(row, "assert", "");
    }

    public void assertInputOutputsState(String key, boolean negativeTest) {
        HashMap<String, String> row = new HashMap<String, String>();
        row.put(key, Boolean.toString(negativeTest));
        processInputOutputs(row, "assert", "");
    }

    public String getStackTraceAsString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        String exceptionAsString = sw.toString();
        return exceptionAsString;
    }

    public void setComponent(String component) {
        attributes.put("component", component);
    }

    public void setInputOutputsDataTable(DataTable dataTable) {
        processInputOutputs(dataTable, "set", "");
    }

    public void setInputOutputsDataTable(DataTable dataTable, String sectionName) {
        processInputOutputs(dataTable, "set", sectionName);
    }

    public void setInputOutputsDataTable(DataTable dataTable, String sectionName, boolean negativeTest) {
        processInputOutputs(dataTable, "set", sectionName);
    }

    public void setInputOutputsDocString(String key, String value) {
        HashMap<String, String> row = new HashMap<String, String>();
        row.put(key, value);
        processInputOutputs(row, "set", "");
    }

    public void setPath(String path) {
        attributes.put("path", path);
    }

    public void transition() {
    }

    private String cleanName(String name) {
        return name.replaceAll("[ \\-\\(\\)/]", "");
    }

    private void processInputOutputs(DataTable dataTable, String operation, String sectionName) {
        List<List<String>> data = dataTable.asLists();

        ArrayList<String> headers = new ArrayList<String>();
        for (String cell : data.get(0)) {
            headers.add(cell);
        }
        for (int i = 1; i < data.size(); i++) {
            HashMap<String, String> row = new HashMap<String, String>();
            for (int j = 0; j < data.get(i).size(); j++) {
                row.put(headers.get(j), data.get(i).get(j));
            }
            processInputOutputs(row, operation, sectionName);
        }
    }

    private void processInputOutputs(HashMap<String, String> row, String operation, String sectionName) {
        try {
            for (String s : row.keySet()) {
                this.getClass().getMethod(operation + cleanName(sectionName) + cleanName(s), HashMap.class).invoke(this,
                        row);
            }
        } catch (Exception e) {
            Assertions.fail(getStackTraceAsString(e));
        }
    }
}
