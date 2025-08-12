package com.github.mangila.webshop.outbox.config;

import com.github.mangila.webshop.outbox.infrastructure.actuator.OutboxJobActuatorEndpoint;
import com.github.mangila.webshop.outbox.infrastructure.scheduler.job.OutboxJobKey;
import com.github.mangila.webshop.outbox.infrastructure.scheduler.job.OutboxJobRunner;
import com.github.mangila.webshop.shared.SimpleTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OutboxActuatorConfig {

    @Bean
    OutboxJobActuatorEndpoint outboxJobActuatorEndpoint(
            Map<OutboxJobKey, SimpleTask<OutboxJobKey>> outboxJobKeyToTJobs,
            OutboxJobRunner outboxJobRunner
    ) {
        return new OutboxJobActuatorEndpoint(outboxJobKeyToTJobs, outboxJobRunner);
    }

}
