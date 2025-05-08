package org.farhan.mbt.maven;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        
        // Create retry template
        RetryTemplate retryTemplate = new RetryTemplate();
        
        // Set timeout policy
        TimeoutRetryPolicy retryPolicy = new TimeoutRetryPolicy();
        retryPolicy.setTimeout(60000); // 60 seconds in milliseconds
        
        retryTemplate.setRetryPolicy(retryPolicy);
        
        return restTemplate;
    }
}