package com.github.mangila.webshop.outbox.config;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.actuator.OutboxJobActuatorEndpoint;
import com.github.mangila.webshop.outbox.infrastructure.actuator.OutboxPublishActuatorEndpoint;
import com.github.mangila.webshop.outbox.infrastructure.message.OutboxPublisher;
import com.github.mangila.webshop.outbox.infrastructure.scheduler.job.OutboxJobKey;
import com.github.mangila.webshop.outbox.infrastructure.scheduler.job.OutboxJobRunner;
import com.github.mangila.webshop.shared.DistinctQueue;
import com.github.mangila.webshop.shared.SimpleTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OutboxActuatorConfig {

    @Bean
    OutboxPublishActuatorEndpoint outboxPublishActuatorEndpoint(OutboxPublisher outboxPublisher,
                                                                DistinctQueue<OutboxId> eventQueue) {
        return new OutboxPublishActuatorEndpoint(outboxPublisher, eventQueue);
    }

    @Bean
    OutboxJobActuatorEndpoint outboxJobActuatorEndpoint(
            Map<OutboxJobKey, SimpleTask<OutboxJobKey>> outboxJobKeyToTJobs,
            OutboxJobRunner outboxJobRunner
    ) {
        return new OutboxJobActuatorEndpoint(outboxJobKeyToTJobs, outboxJobRunner);
    }

}
