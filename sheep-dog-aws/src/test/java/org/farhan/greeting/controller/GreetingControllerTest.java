package org.farhan.greeting.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
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

import org.farhan.greeting.exception.MessagePublishingException;
import org.farhan.greeting.model.Greeting;
import org.farhan.greeting.repository.GreetingRepository;
import org.farhan.greeting.service.GreetingMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GreetingController.class)
public class GreetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GreetingRepository greetingRepository;

    @MockitoBean
    private GreetingMessageService messageService;

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
        when(greetingRepository.findAllByOrderByCreatedTimeDesc()).thenReturn(mockGreetings);

        // Setup mock message service behavior
        doNothing().when(messageService).sendGreeting(any(Greeting.class));
        when(messageService.waitForQueueToClear(anyInt())).thenReturn(true);
    }

    @Test
    public void testGreetingWithDefaultParameter() throws Exception {
        mockMvc.perform(get("/greeting"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].content", is("Hello, World!")));

        // Verify message service and repository interactions
        verify(messageService, times(1)).sendGreeting(any(Greeting.class));
        verify(messageService, times(1)).waitForQueueToClear(anyInt());
        verify(greetingRepository, times(1)).findAllByOrderByCreatedTimeDesc();
    }

    @Test
    public void testResourceNotFoundException() throws Exception {
        // Mock repository to throw exception
        when(greetingRepository.findAllByOrderByCreatedTimeDesc())
                .thenThrow(new MessagePublishingException("Failed to get queue data"));

        mockMvc.perform(get("/greeting"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Failed to get queue data")));
    }

    @Test
    public void testGreetingWithCustomParameter() throws Exception {
        mockMvc.perform(get("/greeting").param("name", "Spring"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").isNumber());

        // Verify message service and repository interactions
        verify(messageService, times(1)).sendGreeting(any(Greeting.class));
        verify(messageService, times(1)).waitForQueueToClear(anyInt());
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

        // Verify message service and repository interactions
        verify(messageService, times(1)).sendGreeting(any(Greeting.class));
        verify(messageService, times(1)).waitForQueueToClear(anyInt());
        verify(greetingRepository, times(1)).findAllByOrderByCreatedTimeDesc();
    }
}
