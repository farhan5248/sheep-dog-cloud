package org.farhan.greeting.service;

import org.farhan.greeting.config.JmsConfig;
import org.farhan.greeting.exception.MessageConsumingException;
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

        int retryCount = 0;
        int maxRetries = 3;
        boolean saved = false;

        while (!saved && retryCount < maxRetries) {
            try {
                greetingRepository.save(greeting);
                logger.info("Saved greeting to database: {}", greeting.getId());
                saved = true;
            } catch (Exception e) {
                retryCount++;
                logger.warn("Error saving greeting to database (attempt {}/{})", retryCount, maxRetries, e);

                if (retryCount >= maxRetries) {
                    logger.error("Failed to save greeting after {} attempts", maxRetries, e);
                    // In a production system, we would move this to a dead letter queue
                    throw new MessageConsumingException("Failed to save greeting after multiple attempts", e);
                }

                try {
                    // Exponential backoff
                    Thread.sleep(100 * (long) Math.pow(2, retryCount));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    logger.warn("Message retry interrupted", ie);
                    throw new MessageConsumingException("Message retry interrupted", ie);
                }
            }
        }
    }
}
