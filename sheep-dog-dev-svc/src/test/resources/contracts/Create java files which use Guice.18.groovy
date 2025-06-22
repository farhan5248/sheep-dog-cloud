org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'POST'
        url ('/sheep-dog-dev-svc/runConvertUMLToCucumberGuice') {
            queryParameters {
                parameter tags: ''
                parameter fileName: 'src-gen/test/resources/cucumber/specs/app/Process.feature'
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
        body({
  "fileName" : "src-gen/test/resources/cucumber/specs/app/Process.feature",
  "fileContent" : "@tag1\nFeature: Process\n\n  \\@tag1\n  Desc 1\n\n  @tag2\n  Scenario: Story One\n\n    \\@tag2\n    Desc 2\n\n    Given The blah application, Object page is valid\n     Then The blah application, Object page is created as follows\n          \"\"\"\n            text1\n          \n            text2\n          \"\"\"\n\n  @tag3\n  Scenario Outline: Story Two\n\n    \\@tag3\n    Desc 3\n\n    Given The blah application, Object page is invalid\n     When The blah application, Object page is created as follows\n          | grp | ins   |\n          | 8   | <ins> |\n\n    Examples: Some data\n\n          | ins |\n          | 4   |\n\n    Examples: Dataset 2\n\n          | ins |\n          | 5   |\n          | 6   |\n\n",
  "tags" : null
})
    }
}
