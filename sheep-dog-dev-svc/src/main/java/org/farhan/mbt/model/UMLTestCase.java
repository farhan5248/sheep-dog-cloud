package org.farhan.mbt.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.farhan.mbt.core.UMLTestStep;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UMLTestCase {

    @JsonProperty("name")
    String name;
    @JsonProperty("description")
    ArrayList<String> description;
    @JsonProperty("tags")
    ArrayList<String> tags;
    @JsonProperty("_links")
    HashMap<String, Object> _links;
    @JsonProperty("testStepList")
    ArrayList<String> testStepList;

    public UMLTestCase() {
        this.name = null;
        this.description = null;
        this.tags = null;
        this._links = new HashMap<String, Object>();
        this.testStepList = new ArrayList<String>();
    }

    public UMLTestCase(org.farhan.mbt.core.UMLTestCase umlTestCase) {
        this.name = umlTestCase.getName();
        this.description = new ArrayList<>(Arrays.asList(umlTestCase.getDescription().split("\n")));
        this.tags = umlTestCase.getTags();
        this._links = new HashMap<String, Object>();

        testStepList = new ArrayList<String>();
        _links.put("testStepList", testStepList);
        for (UMLTestStep umlTestStep : umlTestCase.getTestStepList()) {
            testStepList.add(
                    "/project/" + umlTestCase.getParent().getParent().getId()
                            + "/suite/" + umlTestCase.getParent().getId() + "/case/" + umlTestCase.getId()
                            + "/step/" + umlTestStep.getId());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getDescription() {
        return description;
    }

    public void setDescription(ArrayList<String> description) {
        this.description = description;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public HashMap<String, Object> get_links() {
        return _links;
    }

    public void set_links(HashMap<String, Object> _links) {
        this._links = _links;
    }

    public void setTestStepList(ArrayList<String> testStepList) {
        this.testStepList = testStepList;
    }

    public ArrayList<String> getTestStepList() {
        return testStepList;
    }
}
