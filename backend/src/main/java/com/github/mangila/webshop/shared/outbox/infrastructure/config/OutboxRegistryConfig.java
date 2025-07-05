package com.github.mangila.webshop.shared.outbox.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class OutboxRegistryConfig {

    @Bean
    public Map<String, String> eventRegistryMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, String> topicRegistryMap() {
        return new ConcurrentHashMap<>();
    }
}
