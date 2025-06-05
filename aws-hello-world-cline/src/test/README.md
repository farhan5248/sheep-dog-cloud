# Spring Boot JUnit Tests

This directory contains JUnit tests for the Spring Boot Greeting service.

## Test Structure

The tests are organized into the following categories:

1. **Unit Tests**
   - `GreetingModelTest`: Tests for the Greeting model class
   - `GreetingControllerTest`: Tests for the GreetingController using MockMVC

2. **Integration Tests**
   - `GreetingIntegrationTest`: End-to-end tests using TestRestTemplate

## Running the Tests

### Running All Tests

To run all tests, use the following Maven command:

```bash
mvn test
```

### Running Specific Test Classes

To run a specific test class, use:

```bash
mvn test -Dtest=GreetingControllerTest
```

### Running Specific Test Methods

To run a specific test method, use:

```bash
mvn test -Dtest=GreetingControllerTest#testGreetingWithDefaultParameter
```

## Test Configuration

The tests use the following configuration:

- `application-test.properties`: Contains test-specific configuration
  - Uses a random port for integration tests (`server.port=0`)
  - Configures Actuator endpoints for health checks

## Test Types

### Unit Tests

Unit tests focus on testing individual components in isolation:

- **GreetingModelTest**: Tests the Greeting model's constructor and getters
- **GreetingControllerTest**: Tests the GreetingController using MockMVC
  - Tests default parameter behavior
  - Tests custom parameter behavior
  - Tests response structure

### Integration Tests

Integration tests verify that the components work together correctly:

- **GreetingIntegrationTest**: Tests the full application
  - Verifies that the application context loads successfully
  - Tests the greeting endpoint with default parameters
  - Tests the greeting endpoint with custom parameters
  - Tests the Actuator health endpoint

## Test Frameworks and Libraries

The tests use the following frameworks and libraries:

- **JUnit 5**: The core testing framework
- **Spring Test**: Spring's testing support
- **MockMVC**: For testing Spring MVC controllers
- **TestRestTemplate**: For integration testing with real HTTP requests
- **AssertJ**: For fluent assertions
- **Hamcrest**: For matcher-based assertions
