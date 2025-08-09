package com.github.mangila.webshop.outbox.config;

import com.github.mangila.webshop.outbox.infrastructure.actuator.OutboxTaskActuatorEndpoint;
import com.github.mangila.webshop.outbox.infrastructure.task.OutboxTaskKey;
import com.github.mangila.webshop.outbox.infrastructure.task.OutboxSimpleTaskRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OutboxActuatorConfig {

    @Bean
    OutboxTaskActuatorEndpoint outboxTaskActuatorEndpoint(
            Map<String, OutboxTaskKey> outboxTaskKeys,
            OutboxSimpleTaskRunner outboxSimpleTaskRunner
    ) {
        return new OutboxTaskActuatorEndpoint(outboxTaskKeys, outboxSimpleTaskRunner);
    }

}
