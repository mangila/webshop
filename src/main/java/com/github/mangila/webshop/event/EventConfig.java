package com.github.mangila.webshop.event;

import com.github.mangila.webshop.event.model.EventEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.DataClassRowMapper;

@Configuration
public class EventConfig {

    @Bean
    public DataClassRowMapper<EventEntity> eventEntityRowMapper() {
        return new DataClassRowMapper<>(EventEntity.class);
    }
}
