org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'DELETE'
        url '/sheep-dog-dev-svc/clearConvertAsciidoctorToUMLObjects'
        headers {
            header('scenarioId', 'Create java files which use Guice')
        }
    }
    response {
        status 200
    }
}
