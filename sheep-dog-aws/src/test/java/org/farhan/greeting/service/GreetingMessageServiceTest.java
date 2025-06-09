package org.farhan.greeting.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.farhan.greeting.model.Greeting;
import org.farhan.greeting.repository.GreetingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class GreetingMessageServiceTest {

    @Autowired
    private GreetingMessageService messageService;
    
    @Autowired
    private GreetingRepository greetingRepository;
    
    @BeforeEach
    public void setup() {
        // Clear any existing data
        greetingRepository.deleteAll();
    }
    
    @Test
    public void testSendGreetingToQueue() {
        // Create a greeting
        Greeting greeting = new Greeting();
        greeting.setContent("Hello, Queue Test!");
        greeting.setCreatedTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        
        // Send to queue
        messageService.sendGreeting(greeting);
        
        // Wait for the message to be processed and saved to the database
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            List<Greeting> greetings = greetingRepository.findAllByOrderByCreatedTimeDesc();
            assertThat(greetings).isNotEmpty();
            assertThat(greetings.get(0).getContent()).isEqualTo("Hello, Queue Test!");
        });
        
        // Verify queue is empty
        boolean queueEmpty = messageService.waitForQueueToClear(2);
        assertThat(queueEmpty).isTrue();
    }
    
    @Test
    public void testMultipleGreetingsProcessedInOrder() {
        // Create and send multiple greetings
        for (int i = 1; i <= 3; i++) {
            Greeting greeting = new Greeting();
            greeting.setContent("Hello, Queue Test " + i + "!");
            greeting.setCreatedTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
            
            // Send to queue
            messageService.sendGreeting(greeting);
            
            // Small delay to ensure different timestamps
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Wait for all messages to be processed
        await().atMost(5, TimeUnit.SECONDS).untilAsserted(() -> {
            List<Greeting> greetings = greetingRepository.findAllByOrderByCreatedTimeDesc();
            assertThat(greetings).hasSize(3);
        });
        
        // Verify queue is empty
        boolean queueEmpty = messageService.waitForQueueToClear(2);
        assertThat(queueEmpty).isTrue();
        
        // Verify greetings were saved in the correct order
        List<Greeting> greetings = greetingRepository.findAllByOrderByCreatedTimeDesc();
        assertThat(greetings).hasSize(3);
        assertThat(greetings.get(0).getContent()).isEqualTo("Hello, Queue Test 3!");
        assertThat(greetings.get(1).getContent()).isEqualTo("Hello, Queue Test 2!");
        assertThat(greetings.get(2).getContent()).isEqualTo("Hello, Queue Test 1!");
    }
}
