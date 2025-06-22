org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url '/hello'
    }
    response {
        status 500
        body([
            status: 500,
            error: 'Internal Server Error',
            message: 'No static resource hello.',
            path: 'uri=/hello'
        ])
        headers {
            contentType(applicationJson())
        }
    }
}