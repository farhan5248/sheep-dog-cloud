package org.farhan.greeting.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class GreetingModelTest {

    @Test
    public void testGreetingConstructorAndGetters() {
        // Arrange
        long id = 1L;
        String content = "Hello, Test!";
        
        // Act
        Greeting greeting = new Greeting(id, content);
        
        // Assert
        assertNotNull(greeting);
        assertEquals(id, greeting.getId());
        assertEquals(content, greeting.getContent());
    }
}
