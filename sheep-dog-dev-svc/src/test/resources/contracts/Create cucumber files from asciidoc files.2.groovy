org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'POST'
        url ('/sheep-dog-dev-svc/runConvertAsciidoctorToUML') {
            queryParameters {
                parameter fileName: 'src/test/resources/asciidoc/specs/app/Process.asciidoc'
            }
        }
        body('''= Test-Suite: Process

@tag1
Desc 1

== Test-Case: Story One

@tag2
Desc 2

* Given: The blah application, Object page is valid

* Then: The Object page is created as follows
+
----
  text1

  text2
----

== Test-Case: Story Two

@tag3
Desc 3

* Given: The blah application, Object page is invalid

* When: The Object page is created as follows
+
|===
| grp | ins
| 8 | {ins}
|===

* Test-Data: Some data
+
|===
| ins
| 4
|===

* Test-Data: Dataset 2
+
|===
| ins
| 5
| 6
|===''')
        headers {
            header('scenarioId', 'Create cucumber files from asciidoc files')
        }
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
        body('''{"fileName":"src/test/resources/asciidoc/specs/app/Process.asciidoc","fileContent":"= Test-Suite: Process\\n\\n@tag1\\nDesc 1\\n\\n== Test-Case: Story One\\n\\n@tag2\\nDesc 2\\n\\n* Given: The blah application, Object page is valid\\n\\n* Then: The Object page is created as follows\\n+\\n----\\n  text1\\n\\n  text2\\n----\\n\\n== Test-Case: Story Two\\n\\n@tag3\\nDesc 3\\n\\n* Given: The blah application, Object page is invalid\\n\\n* When: The Object page is created as follows\\n+\\n|===\\n| grp | ins\\n| 8 | {ins}\\n|===\\n\\n* Test-Data: Some data\\n+\\n|===\\n| ins\\n| 4\\n|===\\n\\n* Test-Data: Dataset 2\\n+\\n|===\\n| ins\\n| 5\\n| 6\\n|===","tags":""}''')
    }
}
