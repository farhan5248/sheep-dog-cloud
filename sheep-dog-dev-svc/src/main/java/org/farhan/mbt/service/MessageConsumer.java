package org.farhan.mbt.service;

import org.farhan.mbt.config.JmsConfig;
import org.farhan.mbt.controller.LoggerImpl;
import org.farhan.mbt.convert.ConvertAsciidoctorToUML;
import org.farhan.mbt.convert.ConvertCucumberToUML;
import org.farhan.mbt.convert.Converter;
import org.farhan.mbt.convert.ObjectRepository;
import org.farhan.mbt.exception.MessageConsumingException;
import org.farhan.mbt.model.TransformableFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import jakarta.jms.JMSException;

@Component
public class MessageConsumer {

    // TODO until I can check if a doc is processed, will use this hack
    public static boolean IN_FLIGHT = false;

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
        boolean saved = false;

        while (!saved && retryCount < maxRetries) {
            try {
                Converter mojo;
                if (file.getFileName().endsWith(".asciidoc")) {
                    mojo = new ConvertAsciidoctorToUML(file.getTags(), repository, new LoggerImpl(logger));
                } else if (file.getFileName().endsWith(".feature")) {
                    mojo = new ConvertCucumberToUML(file.getTags(), repository, new LoggerImpl(logger));
                } else {
                    throw new MessageConsumingException("Unsupported file type: " + file.getFileName());
                }
                mojo.convertFile(file.getFileName(), file.getFileContent() == null ? "" : file.getFileContent());
                logger.info("Transformed source file: {}", file.getFileName());
                saved = true;
                IN_FLIGHT = false;
            } catch (Exception e) {
                retryCount++;
                logger.warn("Error transforming source file (attempt {}/{})", retryCount, maxRetries, e);

                if (retryCount >= maxRetries) {
                    logger.error("Failed to transform source file after {} attempts", maxRetries, e);
                    // In a production system, we would move this to a dead letter queue
                    throw new MessageConsumingException("Failed to transform source file after multiple attempts", e);
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
