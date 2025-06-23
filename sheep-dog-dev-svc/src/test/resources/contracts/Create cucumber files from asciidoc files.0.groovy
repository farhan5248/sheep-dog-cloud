org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url '/actuator/health'
    }
    response {
        status 200
        body('''{"status":"UP"}''')
    }
}
