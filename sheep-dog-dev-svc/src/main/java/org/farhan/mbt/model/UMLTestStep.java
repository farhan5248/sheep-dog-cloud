package org.farhan.mbt.model;

import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UMLTestStep {
    @JsonProperty("name")
    private String name;

    @JsonProperty("nameLong")
    private String nameLong;

    @JsonProperty("stepText")
    private String stepText;

    @JsonProperty("stepData")
    private ArrayList<ArrayList<String>> stepData;

    // Default constructor required for Jackson deserialization
    public UMLTestStep() {
    }

    public UMLTestStep(org.farhan.mbt.core.UMLTestStep umlTestStep) {
        name = umlTestStep.getName();
        nameLong = umlTestStep.getNameLong();
        stepText = umlTestStep.getStepText();
        stepData = umlTestStep.getStepData();
    }

    public String getName() {
        return name;
    }

    public void setNameLong(String nameLong) {
        this.nameLong = nameLong;
    }

    public String getNameLong() {
        return nameLong;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStepText() {
        return stepText;
    }

    public void setStepText(String stepText) {
        this.stepText = stepText;
    }

    public ArrayList<ArrayList<String>> getStepData() {
        return stepData;
    }

    public void setStepData(ArrayList<ArrayList<String>> stepData) {
        this.stepData = stepData;
    }

}
