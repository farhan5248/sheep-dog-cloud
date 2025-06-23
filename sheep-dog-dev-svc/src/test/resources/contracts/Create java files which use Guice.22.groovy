org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'POST'
        url ('/sheep-dog-dev-svc/runConvertUMLToCucumberGuice') {
            queryParameters {
                parameter fileName: 'src-gen/test/java/org/farhan/stepdefs/blah/BlahObjectPageSteps.java'
            }
        }
        headers {
            header('scenarioId', 'Create java files which use Guice')
        }
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
        body('''{"fileName":"src-gen/test/java/org/farhan/stepdefs/blah/BlahObjectPageSteps.java","fileContent":"package org.farhan.stepdefs.blah;\\n\\nimport com.google.inject.Inject;\\nimport io.cucumber.datatable.DataTable;\\nimport io.cucumber.guice.ScenarioScoped;\\nimport io.cucumber.java.en.Given;\\nimport org.farhan.common.TestSteps;\\nimport org.farhan.objects.blah.ObjectPage;\\n\\n@ScenarioScoped\\npublic class BlahObjectPageSteps extends TestSteps {\\n\\n    @Inject\\n    public BlahObjectPageSteps(ObjectPage object) {\\n        super(object, \\"blah\\", \\"Object\\");\\n    }\\n\\n    @Given(\\"^The blah application, Object page is created as follows$\\")\\n    public void isCreatedAsFollows(DataTable dataTable) {\\n        object.setInputOutputs(dataTable);\\n    }\\n\\n    @Given(\\"^The blah application, Object page is invalid$\\")\\n    public void isInvalid() {\\n        object.setInputOutputs(\\"Invalid\\");\\n    }\\n\\n    @Given(\\"^The blah application, Object page is valid$\\")\\n    public void isValid() {\\n        object.setInputOutputs(\\"Valid\\");\\n    }\\n}\\n","tags":null}''')
    }
}
