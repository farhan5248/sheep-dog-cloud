package org.farhan.mbt.service;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.farhan.mbt.config.JmsConfig;
import org.farhan.mbt.core.Converter;
import org.farhan.mbt.exception.PublishingException;
import org.farhan.mbt.exception.TransformationException;
import org.farhan.mbt.model.TransformableFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import jakarta.jms.JMSException;
import jakarta.jms.Queue;

@Service
public class TransformationService {

    private static final Logger logger = LoggerFactory.getLogger(TransformationService.class);
    private final JmsTemplate jmsTemplate;

    @Autowired
    public TransformationService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public TransformableFile convertObject(Converter mojo, String fileName, String contents) {
        try {
            // The replace \r is because when run on a windows machine the tests return a \r
            // but not on a linux machine
            TransformableFile mtr = new TransformableFile(fileName,
                    mojo.convertFile(fileName, contents == null ? "" : contents).replace("\r", ""));
            logger.debug("response: " + mtr.toString());
            return mtr;
        } catch (Exception e) {
            throw new TransformationException(fileName, e);
        }
    }

    public void clearObjects(Converter mojo) {
        try {
            mojo.clearProjects();
        } catch (Exception e) {
            throw new TransformationException("Clearing objects", e);
        }
    }

    public ArrayList<TransformableFile> getObjectNames(Converter mojo, String tags) {
        ArrayList<TransformableFile> fileList = new ArrayList<TransformableFile>();
        try {
            // TODO this is temp, there should be a separate class like the ObjectRepository
            // if not the object repo itself. For a given tag, it should keep track of the
            // source files and output files checksums
            for (String fileName : (mojo).getFileNames()) {
                fileList.add(new TransformableFile(fileName, ""));
                logger.debug("fileName: " + fileName);
            }
            return fileList;
        } catch (Exception e) {
            throw new TransformationException("Getting object names for tags: " + tags, e);
        }
    }

    public void convertSourceObject(Converter mojo, TransformableFile mtr) {
        try {
            jmsTemplate.convertAndSend(JmsConfig.SOURCE_FILES_QUEUE, mtr);
            MessageConsumer.IN_FLIGHT = 1;
            while (MessageConsumer.IN_FLIGHT == 1) {
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.warn("Thread interrupted while waiting for message processing to complete", e);
                }
            }
        } catch (Exception e) {
            throw new PublishingException(mtr.getFileName(), e);
        }
        if (MessageConsumer.IN_FLIGHT == -1) {
            // This is only temp, remove it when IN_FLIGHT is removed
            throw new TransformationException(mtr.getFileName());
        }
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
                    .createQueue(JmsConfig.SOURCE_FILES_QUEUE);

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
            throw new PublishingException("Waiting for queue to clear", e);
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
