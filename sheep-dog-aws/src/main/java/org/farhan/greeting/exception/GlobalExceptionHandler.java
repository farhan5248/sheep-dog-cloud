package org.farhan.greeting.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import jakarta.jms.JMSException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(MessagePublishingException.class)
    public ResponseEntity<ErrorResponse> handleMessagePublishingException(
            MessagePublishingException ex, WebRequest request) {
        
        logger.error("Resource not found exception occurred: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Couldn't save greeting to database",
                ex.getMessage(),
                request.getDescription(false));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(JMSException.class)
    public ResponseEntity<ErrorResponse> handleJMSException(
            JMSException ex, WebRequest request) {
        
        logger.error("JMS exception occurred: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Message Queue Error",
                "An error occurred while consuming messages from the queue",
                request.getDescription(false));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception ex, WebRequest request) {
        
        logger.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "An unexpected error occurred",
                request.getDescription(false));
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
