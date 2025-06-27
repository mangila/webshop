package com.github.mangila.webshop.backend.event;

import com.github.mangila.webshop.backend.event.model.EventEntity;
import com.github.mangila.webshop.backend.event.model.EventTopic;
import com.github.mangila.webshop.backend.event.model.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.DataClassRowMapper;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class EventConfig {

    private static final Logger log = LoggerFactory.getLogger(EventConfig.class);

    @Bean
    public Map<EventTopic, Map<String, EventType>> eventTypes() {
        return new HashMap<>();
    }

    @Bean
    public DataClassRowMapper<EventEntity> eventEntityRowMapper() {
        return new DataClassRowMapper<>(EventEntity.class);
    }
}
