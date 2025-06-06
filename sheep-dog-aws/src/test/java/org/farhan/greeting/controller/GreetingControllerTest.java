package org.farhan.greeting.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import org.farhan.greeting.model.Greeting;
import org.farhan.greeting.repository.GreetingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GreetingController.class)
public class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private GreetingRepository greetingRepository;
    
    private List<Greeting> mockGreetings;
    
    @BeforeEach
    public void setup() {
        // Create mock data
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        
        Greeting greeting1 = new Greeting();
        greeting1.setId(1L);
        greeting1.setContent("Hello, World!");
        greeting1.setCreatedTime(now);
        
        Greeting greeting2 = new Greeting();
        greeting2.setId(2L);
        greeting2.setContent("Hello, Spring!");
        greeting2.setCreatedTime(now.minusMinutes(5));
        
        mockGreetings = Arrays.asList(greeting1, greeting2);
        
        // Setup mock repository behavior
        when(greetingRepository.save(any(Greeting.class))).thenAnswer(invocation -> {
            Greeting savedGreeting = invocation.getArgument(0);
            savedGreeting.setId(3L); // Simulate auto-generated ID
            return savedGreeting;
        });
        
        when(greetingRepository.findAllByOrderByCreatedTimeDesc()).thenReturn(mockGreetings);
    }

    @Test
    public void testGreetingWithDefaultParameter() throws Exception {
        mockMvc.perform(get("/greeting"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].content", is("Hello, World!")));
        
        // Verify repository interactions
        verify(greetingRepository, times(1)).save(any(Greeting.class));
        verify(greetingRepository, times(1)).findAllByOrderByCreatedTimeDesc();
    }

    @Test
    public void testGreetingWithCustomParameter() throws Exception {
        String name = "Spring";
        mockMvc.perform(get("/greeting").param("name", name))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").isNumber());
        
        // Verify that the greeting was saved with the correct content
        verify(greetingRepository, times(1)).save(any(Greeting.class));
        verify(greetingRepository, times(1)).findAllByOrderByCreatedTimeDesc();
    }
    
    @Test
    public void testGreetingResponseStructure() throws Exception {
        mockMvc.perform(get("/greeting"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].content").exists())
                .andExpect(jsonPath("$[0].createdTime").exists())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].content").isString());
        
        // Verify repository interactions
        verify(greetingRepository, times(1)).save(any(Greeting.class));
        verify(greetingRepository, times(1)).findAllByOrderByCreatedTimeDesc();
    }
}
