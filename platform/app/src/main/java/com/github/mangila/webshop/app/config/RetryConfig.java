package com.github.mangila.webshop.app.config;

import com.github.mangila.webshop.shared.util.ApplicationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;

import java.time.Duration;

@Configuration
@EnableRetry
public class RetryConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        return new RetryTemplateBuilder()
                .maxAttempts(5)
                .fixedBackoff(Duration.ofSeconds(2))
                .retryOn(ApplicationException.class)
                .build();
    }
}
