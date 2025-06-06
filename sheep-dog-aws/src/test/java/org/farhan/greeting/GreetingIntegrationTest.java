package org.farhan.greeting;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.farhan.greeting.model.Greeting;
import org.farhan.greeting.repository.GreetingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
    
    @Autowired
    private GreetingRepository greetingRepository;

    @Test
    public void contextLoads() {
        // Verify that the application context loads successfully
    }

    @Test
    public void testGreetingEndpoint() {
        // Clear any existing data
        greetingRepository.deleteAll();
        
        // Make the request
        ResponseEntity<List<Greeting>> response = restTemplate.exchange(
                "http://localhost:" + port + "/greeting",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Greeting>>() {});
        
        // Verify response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
        
        // The first greeting should be the one we just created
        Greeting greeting = response.getBody().get(0);
        assertThat(greeting.getId()).isPositive();
        assertThat(greeting.getContent()).isEqualTo("Hello, World!");
        assertThat(greeting.getCreatedTime()).isNotNull();
    }

    @Test
    public void testGreetingEndpointWithParameter() {
        // Clear any existing data
        greetingRepository.deleteAll();
        
        // Make the request
        ResponseEntity<List<Greeting>> response = restTemplate.exchange(
                "http://localhost:" + port + "/greeting?name=Integration",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Greeting>>() {});
        
        // Verify response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
        
        // The first greeting should be the one we just created
        Greeting greeting = response.getBody().get(0);
        assertThat(greeting.getId()).isPositive();
        assertThat(greeting.getContent()).isEqualTo("Hello, Integration!");
        assertThat(greeting.getCreatedTime()).isNotNull();
    }
    
    @Test
    public void testGreetingsAreSortedByTimeDesc() throws InterruptedException {
        // Clear any existing data
        greetingRepository.deleteAll();
        
        // Make multiple requests to create greetings with a delay between each
        restTemplate.getForEntity("http://localhost:" + port + "/greeting?name=First", String.class);
        Thread.sleep(1000); // Wait 1 second
        
        restTemplate.getForEntity("http://localhost:" + port + "/greeting?name=Second", String.class);
        Thread.sleep(1000); // Wait 1 second
        
        restTemplate.getForEntity("http://localhost:" + port + "/greeting?name=Third", String.class);
        Thread.sleep(1000); // Wait 1 second
        
        // Get all greetings directly from the repository
        List<Greeting> dbGreetings = greetingRepository.findAllByOrderByCreatedTimeDesc();
        
        System.out.println("Greetings from database:");
        for (int i = 0; i < dbGreetings.size(); i++) {
            Greeting g = dbGreetings.get(i);
            System.out.println(i + ": " + g.getId() + " - " + g.getContent() + " - " + g.getCreatedTime());
        }
        
        // Make one more request to get all greetings via the API
        ResponseEntity<List<Greeting>> response = restTemplate.exchange(
                "http://localhost:" + port + "/greeting",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Greeting>>() {});
        
        // Verify response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThanOrEqualTo(4); // 3 created + 1 from this request
        
        // Debug: Print out the greetings in the response
        List<Greeting> greetings = response.getBody();
        System.out.println("Greetings in response:");
        for (int i = 0; i < greetings.size(); i++) {
            Greeting g = greetings.get(i);
            System.out.println(i + ": " + g.getId() + " - " + g.getContent() + " - " + g.getCreatedTime());
        }
        
        // Verify sorting (newest first)
        assertThat(greetings.get(0).getContent()).contains("Hello, World!");  // The most recent greeting
        
        // Verify timestamps are in descending order
        for (int i = 0; i < greetings.size() - 1; i++) {
            assertThat(greetings.get(i).getCreatedTime())
                .isAfterOrEqualTo(greetings.get(i + 1).getCreatedTime());
        }
        
        // Verify the order matches what we expect
        assertThat(greetings.get(1).getContent()).isEqualTo("Hello, Third!");
        assertThat(greetings.get(2).getContent()).isEqualTo("Hello, Second!");
        assertThat(greetings.get(3).getContent()).isEqualTo("Hello, First!");
    }
    
    @Test
    public void testActuatorHealthEndpoint() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/actuator/health", String.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("UP");
    }
}
