package org.farhan.mbt.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.farhan.mbt.core.UMLTestSuite;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UMLTestProject {

    @JsonProperty("_links")
    HashMap<String, Object> _links;
    @JsonProperty("testSuiteList")
    ArrayList<String> testSuiteList;

    public UMLTestProject() {
        this._links = new HashMap<String, Object>();
        this.testSuiteList = new ArrayList<String>();
    }

    public UMLTestProject(org.farhan.mbt.core.UMLTestProject umlTestProject) {
        this._links = new HashMap<String, Object>();
        testSuiteList = new ArrayList<String>();
        _links.put("testSuiteList", testSuiteList);
        for (UMLTestSuite umlTestSuite : umlTestProject.getTestSuiteList()) {
            testSuiteList.add("project/" + umlTestProject.getId() + "/suite/" + umlTestSuite.getId());
        }
    }

    public HashMap<String, Object> get_links() {
        return _links;
    }

    public void set_links(HashMap<String, Object> _links) {
        this._links = _links;
    }

    public void setTestSuiteList(ArrayList<String> testCaseList) {
        this.testSuiteList = testCaseList;
    }

    public ArrayList<String> getTestSuiteList() {
        return testSuiteList;
    }
}
