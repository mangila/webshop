package com.github.mangila.webshop.outbox.config;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.shared.DistinctQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OutboxConfig {

    @Bean("eventQueue")
    DistinctQueue<OutboxId> eventQueue() {
        return new DistinctQueue<>();
    }
}
