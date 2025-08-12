package com.github.mangila.webshop.outbox.config;

import com.github.mangila.webshop.outbox.infrastructure.OutboxIdDistinctQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OutboxConfig {

    @Bean("eventDistinctQueue")
    OutboxIdDistinctQueue eventDistinctQueue() {
        return new OutboxIdDistinctQueue();
    }
}
