package org.farhan.greeting.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

public class GreetingModelTest {

    @Test
    public void testGreetingDefaultConstructor() {
        // Act
        Greeting greeting = new Greeting();
        
        // Assert
        assertNotNull(greeting);
    }
    
    @Test
    public void testGreetingConstructorAndGetters() {
        // Arrange
        String content = "Hello, Test!";
        LocalDateTime createdTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        
        // Act
        Greeting greeting = new Greeting(content, createdTime);
        
        // Assert
        assertNotNull(greeting);
        assertEquals(content, greeting.getContent());
        assertEquals(createdTime, greeting.getCreatedTime());
    }
    
    @Test
    public void testGreetingSettersAndGetters() {
        // Arrange
        Long id = 1L;
        String content = "Hello, Test!";
        LocalDateTime createdTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        
        // Act
        Greeting greeting = new Greeting();
        greeting.setId(id);
        greeting.setContent(content);
        greeting.setCreatedTime(createdTime);
        
        // Assert
        assertEquals(id, greeting.getId());
        assertEquals(content, greeting.getContent());
        assertEquals(createdTime, greeting.getCreatedTime());
    }
}
