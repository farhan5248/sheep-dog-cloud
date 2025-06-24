org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'DELETE'
        url ('/sheep-dog-dev-svc/clearConvertAsciidoctorToUMLObjects') {
            queryParameters {
            }
        }
        headers {
            header('scenarioId', 'Create cucumber files from asciidoc files')
        }
    }
    response {
        status 200
    }
}
