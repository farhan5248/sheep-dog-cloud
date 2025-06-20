package org.farhan.runners.surefire.webmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@Configuration
@TestPropertySource("classpath:application-surefire.properties")
@ActiveProfiles("surefire")
@ComponentScan(basePackages = "org.farhan.mbt.controller")
public class WebMvcTestConfig {
    @Bean
    public JmsTemplate jmsTemplate() {
        return org.mockito.Mockito.mock(JmsTemplate.class);
    }
}
