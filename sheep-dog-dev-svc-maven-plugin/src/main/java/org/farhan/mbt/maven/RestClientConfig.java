package org.farhan.mbt.maven;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.policy.TimeoutRetryPolicy;
import org.springframework.web.client.RestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

@Configuration
public class RestClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(60000); // 60 seconds
        factory.setReadTimeout(60000);    // 60 seconds
        RestTemplate restTemplate = new RestTemplate(factory);
        return restTemplate;
    }
}