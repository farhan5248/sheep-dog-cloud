org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'DELETE'
        url '/sheep-dog-dev-svc/clearConvertAsciidoctorToUMLObjects'
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
    }
}
