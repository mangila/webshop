package com.github.mangila.webshop.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;

import java.time.Duration;


@Configuration
@EnableRetry
public class RetryConfig {

    private static final Logger log = LoggerFactory.getLogger(RetryConfig.class);

    @Bean("outboxRetryTemplate")
    public RetryTemplate outboxRetryTemplate() {
        return new RetryTemplateBuilder()
                .maxAttempts(5)
                .exponentialBackoff(Duration.ofMillis(500), 2, Duration.ofSeconds(30))
                .retryOn(Exception.class)
                .withListener(new RetryListener() {
                    @Override
                    public <T, E extends Throwable> void onError(RetryContext context, RetryCallback<T, E> callback, Throwable throwable) {
                        log.warn("ERROR - Retry attempt: {}, outboxId: {}, error: {}",
                                context.getRetryCount(),
                                context.getAttribute("outboxId"),
                                throwable.getMessage());
                    }

                    @Override
                    public <T, E extends Throwable> void onSuccess(RetryContext context, RetryCallback<T, E> callback, T result) {
                        log.debug("SUCCESS - Retry attempt: {}, outboxId: {}",
                                context.getRetryCount(),
                                context.getAttribute("outboxId"));
                    }
                })
                .build();
    }
}
