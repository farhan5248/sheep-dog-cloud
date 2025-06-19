package org.farhan.stepdefs.maven;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import org.farhan.common.TestSteps;
import org.farhan.objects.maven.UmlToCucumberGoal;

public class MavenUmlToCucumberGoalSteps extends TestSteps {

    public MavenUmlToCucumberGoalSteps(UmlToCucumberGoal object) {
        super(object, "maven", "uml-to-cucumber");
    }

    @Given("^The maven plugin, uml-to-cucumber goal is executed$")
    public void isExecuted() {
        object.transition();
    }

    @Given("^The maven plugin, uml-to-cucumber goal is executed with$")
    public void isExecutedWith(DataTable dataTable) {
        object.setInputOutputs(dataTable);
        object.transition();
    }
}
