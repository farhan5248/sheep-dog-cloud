org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'POST'
        url ('/sheep-dog-dev-svc/runConvertUMLToCucumberGuice') {
            queryParameters {
                parameter tags: ''
                parameter fileName: 'src-gen/test/java/org/farhan/objects/blah/ObjectPage.java'
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
  "fileName" : "src-gen/test/java/org/farhan/objects/blah/ObjectPage.java",
  "fileContent" : "package org.farhan.objects.blah;\r\n\r\nimport java.util.HashMap;\r\n\r\npublic interface ObjectPage {\r\n\r\n    public void setGrp(HashMap<String, String> keyMap);\r\n\r\n    public void setIns(HashMap<String, String> keyMap);\r\n\r\n    public void setContent(HashMap<String, String> keyMap);\r\n\r\n    public void setInvalid(HashMap<String, String> keyMap);\r\n\r\n    public void setValid(HashMap<String, String> keyMap);\r\n}\r\n",
  "tags" : null
})
    }
}
