package org.farhan.greeting.service;

import org.farhan.greeting.config.JmsConfig;
import org.farhan.greeting.exception.MessageConsumingException;
import org.farhan.greeting.model.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import jakarta.jms.JMSException;
import jakarta.jms.Queue;
import java.util.concurrent.TimeUnit;

@Service
public class GreetingMessageService {

    private static final Logger logger = LoggerFactory.getLogger(GreetingMessageConsumer.class);

    private final JmsTemplate jmsTemplate;

    @Autowired
    public GreetingMessageService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    /**
     * Send a greeting to the queue
     * 
     * @param greeting The greeting to send
     */
    public void sendGreeting(Greeting greeting) {
        jmsTemplate.convertAndSend(JmsConfig.GREETING_QUEUE, greeting);
    }

    /**
     * Wait for the queue to be empty
     * 
     * @param timeoutSeconds Maximum time to wait in seconds
     * @return true if queue is empty, false if timeout occurred
     */
    public boolean waitForQueueToClear(int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeoutMillis = TimeUnit.SECONDS.toMillis(timeoutSeconds);

        try {
            Queue queue = jmsTemplate.getConnectionFactory()
                    .createConnection()
                    .createSession(false, jakarta.jms.Session.AUTO_ACKNOWLEDGE)
                    .createQueue(JmsConfig.GREETING_QUEUE);

            while (System.currentTimeMillis() - startTime < timeoutMillis) {
                // Check queue depth
                int queueSize = getQueueSize(queue);
                if (queueSize == 0) {
                    return true;
                }

                // Wait a bit before checking again
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.warn("Queue clearing interrupted", e);
                    return false;
                }
            }

            return false; // Timeout occurred
        } catch (JMSException e) {
            logger.error("Error checking queue status", e);
            throw new MessageConsumingException("Error checking queue status", e);
        }
    }

    /**
     * Get the current size of the queue
     * 
     * @param queue The queue to check
     * @return The number of messages in the queue
     * @throws JMSException If there's an error accessing the queue
     */
    private int getQueueSize(Queue queue) throws JMSException {
        return jmsTemplate.browse(queue, (session, browser) -> {
            int count = 0;
            while (browser.getEnumeration().hasMoreElements()) {
                browser.getEnumeration().nextElement();
                count++;
            }
            return count;
        });
    }
}
