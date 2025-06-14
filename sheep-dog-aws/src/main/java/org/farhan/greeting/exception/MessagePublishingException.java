package org.farhan.greeting.exception;

public class MessagePublishingException extends RuntimeException {
    
    public MessagePublishingException(String message) {
        super(message);
    }
    
    public MessagePublishingException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}