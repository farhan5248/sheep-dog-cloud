package org.farhan.greeting.service;

import org.farhan.greeting.config.JmsConfig;
import org.farhan.greeting.model.Greeting;
import org.farhan.greeting.repository.GreetingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class GreetingMessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(GreetingMessageConsumer.class);
    
    private final GreetingRepository greetingRepository;
    
    @Autowired
    public GreetingMessageConsumer(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }
    
    /**
     * Listen for greeting messages on the queue and save them to the database
     * 
     * @param greeting The greeting message received from the queue
     */
    @JmsListener(destination = JmsConfig.GREETING_QUEUE)
    public void receiveGreeting(Greeting greeting) {
        logger.info("Received greeting: {}", greeting.getContent());
        
        try {
            // Save the greeting to the database
            greetingRepository.save(greeting);
            logger.info("Saved greeting to database: {}", greeting.getId());
        } catch (Exception e) {
            logger.error("Error saving greeting to database", e);
            // In a production system, we might want to implement retry logic
            // or move the message to a dead letter queue
            throw e; // Re-throw to let Spring handle the exception
        }
    }
}
