org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'POST'
        url ('/sheep-dog-dev-svc/runConvertAsciidoctorToUML') {
            queryParameters {
                parameter tags: ''
                parameter fileName: 'src/test/resources/asciidoc/stepdefs/blah application/Object page.asciidoc'
            }
        }
        body('''= Step-Object: Object page

== Step-Definition: is valid

== Step-Definition: is invalid

== Step-Definition: is created as follows

* Step-Parameters: 1
+
|===
| grp | ins
|===

* Step-Parameters: 2
+
|===
| Content
|===''')
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
  "fileName" : "src/test/resources/asciidoc/stepdefs/blah application/Object page.asciidoc",
  "fileContent" : "= Step-Object: Object page\n\n== Step-Definition: is valid\n\n== Step-Definition: is invalid\n\n== Step-Definition: is created as follows\n\n* Step-Parameters: 1\n+\n|===\n| grp | ins\n|===\n\n* Step-Parameters: 2\n+\n|===\n| Content\n|===",
  "tags" : ""
})
    }
}
