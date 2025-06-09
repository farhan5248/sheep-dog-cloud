package org.farhan.greeting.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.farhan.greeting.model.Greeting;
import org.farhan.greeting.repository.GreetingRepository;
import org.farhan.greeting.service.GreetingMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final Logger logger = LoggerFactory.getLogger(GreetingController.class);
    private static final String TEMPLATE = "Hello, %s!";
    private static final int QUEUE_WAIT_TIMEOUT_SECONDS = 10;

    private final GreetingRepository greetingRepository;
    private final GreetingMessageService messageService;

    @Autowired
    public GreetingController(GreetingRepository greetingRepository, GreetingMessageService messageService) {
        this.greetingRepository = greetingRepository;
        this.messageService = messageService;
    }

    @GetMapping("/greeting")
    public List<Greeting> greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        logger.info("Received greeting request for name: {}", name);

        // Create new greeting with current time truncated to seconds
        Greeting greeting = new Greeting();
        greeting.setContent(String.format(TEMPLATE, name));
        greeting.setCreatedTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        // Send to message queue instead of directly saving
        logger.info("Sending greeting to queue: {}", greeting.getContent());
        messageService.sendGreeting(greeting);

        // Wait for the queue to be cleared (message processed)
        logger.info("Waiting for queue to be cleared...");
        boolean queueCleared = messageService.waitForQueueToClear(QUEUE_WAIT_TIMEOUT_SECONDS);

        if (!queueCleared) {
            logger.warn("Queue processing timed out after {} seconds", QUEUE_WAIT_TIMEOUT_SECONDS);
        } else {
            logger.info("Queue cleared successfully");
        }

        // Wait for the save operation to complete
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Get all greetings sorted by time (newest first)
        List<Greeting> greetings = greetingRepository.findAllByOrderByCreatedTimeDesc();

        // Log the order for debugging
        logger.info("Greetings in order:");
        for (Greeting g : greetings) {
            logger.info("{}: {} - {}", g.getId(), g.getContent(), g.getCreatedTime());
        }

        return greetings;
    }
}
