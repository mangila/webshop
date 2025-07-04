package com.github.mangila.webshop.backend.outboxevent.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class OutboxEventRegistryConfig {

    @Bean
    public Map<String, String> eventRegistryMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, String> topicRegistryMap() {
        return new ConcurrentHashMap<>();
    }
}
