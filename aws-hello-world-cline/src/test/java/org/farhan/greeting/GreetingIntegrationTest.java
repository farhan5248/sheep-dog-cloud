package org.farhan.greeting;

import static org.assertj.core.api.Assertions.assertThat;

import org.farhan.greeting.model.Greeting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GreetingIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        // Verify that the application context loads successfully
    }

    @Test
    public void testGreetingEndpoint() {
        ResponseEntity<Greeting> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/greeting", Greeting.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isPositive();
        assertThat(response.getBody().getContent()).isEqualTo("Hello, World!");
    }

    @Test
    public void testGreetingEndpointWithParameter() {
        ResponseEntity<Greeting> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/greeting?name=Integration", Greeting.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isPositive();
        assertThat(response.getBody().getContent()).isEqualTo("Hello, Integration!");
    }
    
    @Test
    public void testActuatorHealthEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/actuator/health", String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("UP");
    }
}
