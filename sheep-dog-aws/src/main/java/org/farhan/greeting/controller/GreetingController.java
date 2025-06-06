package org.farhan.greeting.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.farhan.greeting.model.Greeting;
import org.farhan.greeting.repository.GreetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private static final String TEMPLATE = "Hello, %s!";
    private final GreetingRepository greetingRepository;
    
    @Autowired
    public GreetingController(GreetingRepository greetingRepository) {
        this.greetingRepository = greetingRepository;
    }

    @GetMapping("/greeting")
    public List<Greeting> greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        // Create new greeting with current time truncated to seconds
        Greeting greeting = new Greeting();
        greeting.setContent(String.format(TEMPLATE, name));
        greeting.setCreatedTime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        
        // Save to database
        greetingRepository.save(greeting);
        
        // Get all greetings sorted by time (newest first)
        List<Greeting> greetings = greetingRepository.findAllByOrderByCreatedTimeDesc();
        
        // Log the order for debugging
        System.out.println("Greetings in order:");
        for (Greeting g : greetings) {
            System.out.println(g.getId() + ": " + g.getContent() + " - " + g.getCreatedTime());
        }
        
        return greetings;
    }
}
