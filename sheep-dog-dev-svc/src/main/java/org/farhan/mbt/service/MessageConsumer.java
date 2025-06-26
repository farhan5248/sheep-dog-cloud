package org.farhan.mbt.service;

import org.farhan.mbt.asciidoctor.ConvertAsciidoctorToUML;
import org.farhan.mbt.config.JmsConfig;
import org.farhan.mbt.controller.LoggerImpl;
import org.farhan.mbt.core.Converter;
import org.farhan.mbt.core.ObjectRepository;
import org.farhan.mbt.exception.MessageConsumingException;
import org.farhan.mbt.model.TransformableFile;
import org.farhan.mbt.service.cucumber.ConvertCucumberToUML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import jakarta.jms.JMSException;

@Component
public class MessageConsumer {

    // TODO until I can check if a doc is processed, will use this hack
    public static int IN_FLIGHT = 0;

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    private final ObjectRepository repository;

    @Autowired
    public MessageConsumer(ObjectRepository repository) {
        this.repository = repository;
    }

    @JmsListener(destination = JmsConfig.SOURCE_FILES_QUEUE)
    public void receiveSourceFile(TransformableFile file) throws JMSException {
        logger.info("Received source file: {}", file.getFileName());

        int retryCount = 0;
        int maxRetries = 3;

        while (retryCount < maxRetries) {
            try {
                Converter mojo;
                if (file.getFileName().endsWith(".asciidoc")) {
                    mojo = new ConvertAsciidoctorToUML(file.getTags(), repository, new LoggerImpl(logger));
                } else if (file.getFileName().endsWith(".feature")) {
                    mojo = new ConvertCucumberToUML(file.getTags(), repository, new LoggerImpl(logger));
                } else {
                    logger.warn("Unsupported file type: " + file.getFileName());
                    IN_FLIGHT = 0;
                    return; // Skip unsupported file types
                }
                mojo.convertFile(file.getFileName(), file.getFileContent() == null ? "" : file.getFileContent());
                logger.info("Transformed source file: {}", file.getFileName());
                IN_FLIGHT = 0;
                return;
            } catch (Exception e) {
                retryCount++;
                logger.warn("Error transforming source file (attempt {}/{})", retryCount, maxRetries, e);
                try {
                    Thread.sleep(100 * (long) Math.pow(2, retryCount));
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    logger.warn("Message retry interrupted", ie);
                    throw new MessageConsumingException("Message retry interrupted", ie);
                }
            }
        }
        if (retryCount >= maxRetries) {
            logger.error("Failed to transform source file after {} attempts", maxRetries);
            IN_FLIGHT = 0;
            // In a production system, we would move this to a dead letter queue
            throw new MessageConsumingException("Failed to transform source file after multiple attempts");
        }

    }
}
