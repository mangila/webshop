package com.github.mangila.webshop.backend.event.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class EventConfig {

    @Bean
    public Map<String, String> eventTypeRegistryMap() {
        return new ConcurrentHashMap<>();
    }

    @Bean
    public Map<String, String> eventTopicRegistryMap() {
        return new ConcurrentHashMap<>();
    }
}
