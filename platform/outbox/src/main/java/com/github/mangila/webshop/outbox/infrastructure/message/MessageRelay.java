package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.application.usecase.PublishOutboxEventUseCase;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * MessageRelay is responsible for scheduling tasks to process outbox messages.
 * It facilitates publishing messages from the queue and dead-letter queue (DLQ)
 * through the {@link PublishOutboxEventUseCase}.
 *
 * This service is conditionally loaded based on the "app.outbox.message-relay.enabled"
 * property in the application's configuration.
 *
 * The publishQueue method processes messages from the main queue at a fixed rate
 * specified by the "app.outbox.message-relay.process-queue.fixed-rate" property.
 *
 * The publishDlq method processes messages from the dead-letter queue (DLQ)
 * at a fixed rate specified by the "app.outbox.message-relay.process-dlq.fixed-rate" property.
 */
@Service
@ConditionalOnProperty(name = "app.outbox.message-relay.enabled", havingValue = "true")
public class MessageRelay {
    private final PublishOutboxEventUseCase publishOutboxEventUseCase;

    public MessageRelay(PublishOutboxEventUseCase publishOutboxEventUseCase) {
        this.publishOutboxEventUseCase = publishOutboxEventUseCase;
    }

    @Scheduled(fixedRateString = "${app.outbox.message-relay.process-queue.fixed-rate}")
    public void publishFromQueue() {
        publishOutboxEventUseCase.publishFromQueue();
    }

    @Scheduled(fixedRateString = "${app.outbox.message-relay.process-dlq.fixed-rate}")
    public void publishFromDlq() {
        publishOutboxEventUseCase.publishFromDlq();
    }
}
