org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'POST'
        url ('/sheep-dog-dev-svc/runConvertUMLToCucumber') {
            queryParameters {
                parameter tags: ''
                parameter fileName: 'src-gen/test/java/org/farhan/stepdefs/blah/BlahObjectPageSteps.java'
            }
        }
        headers {
            header('scenarioId', 'Create cucumber files from asciidoc files')
        }
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
        body({
  "fileName" : "src-gen/test/java/org/farhan/stepdefs/blah/BlahObjectPageSteps.java",
  "fileContent" : "package org.farhan.stepdefs.blah;\r\n\r\nimport io.cucumber.datatable.DataTable;\r\nimport io.cucumber.java.en.Given;\r\nimport org.farhan.common.TestSteps;\r\n\r\npublic class BlahObjectPageSteps extends TestSteps {\r\n\r\n    public BlahObjectPageSteps() {\r\n        super(\"ObjectPage\", \"blah\", \"Object\");\r\n    }\r\n\r\n    @Given(\"^The blah application, Object page is created as follows$\")\r\n    public void isCreatedAsFollows(DataTable dataTable) {\r\n        object.setInputOutputs(dataTable);\r\n    }\r\n\r\n    @Given(\"^The blah application, Object page is invalid$\")\r\n    public void isInvalid() {\r\n        object.setInputOutputs(\"Invalid\");\r\n    }\r\n\r\n    @Given(\"^The blah application, Object page is valid$\")\r\n    public void isValid() {\r\n        object.setInputOutputs(\"Valid\");\r\n    }\r\n}\r\n",
  "tags" : null
})
    }
}
