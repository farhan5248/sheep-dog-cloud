org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url ('/sheep-dog-dev-svc/getConvertUMLToCucumberObjectNames') {
            queryParameters {
                parameter tags: ''
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
        body('''[{"fileName":"src-gen/test/resources/cucumber/specs/app/Process.feature","fileContent":"","tags":null},{"fileName":"src-gen/test/java/org/farhan/stepdefs/blah/BlahObjectPageSteps.java","fileContent":"","tags":null},{"fileName":"src-gen/test/java/org/farhan/objects/blah/ObjectPage.java","fileContent":"","tags":null}]''')
    }
}
