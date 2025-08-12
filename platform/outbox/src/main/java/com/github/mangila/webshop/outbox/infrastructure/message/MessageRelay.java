package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.OutboxIdDistinctQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * The Message Relay (part of Transactional Outbox Pattern).
 * <a href="https://microservices.io/patterns/data/transactional-outbox.html">https://microservices.io/patterns/data/transactional-outbox.html</a>
 * <p>
 * MessageRelay is responsible for scheduling tasks to process outbox messages.
 * It facilitates publishing messages from the queue and dead-letter queue (DLQ)
 * through the {@link OutboxPublisher}.
 * <p>
 * This service is conditionally loaded based on the "app.outbox.message-relay.enabled"
 * property in the application's configuration.
 * <p>
 * The publishQueue method processes messages from the main queue at a fixed rate
 * specified by the "app.outbox.message-relay.process-queue.fixed-rate" property.
 * <p>
 * The publishDlq method processes messages from the dead-letter queue (DLQ)
 * at a fixed rate specified by the "app.outbox.message-relay.process-dlq.fixed-rate" property.
 */
@Service
@ConditionalOnProperty(name = "app.outbox.message-relay.enabled", havingValue = "true")
public class MessageRelay {

    private static final Logger log = LoggerFactory.getLogger(MessageRelay.class);
    private final OutboxPublisher publisher;
    private final OutboxIdDistinctQueue outboxIdDistinctQueue;

    public MessageRelay(OutboxPublisher publisher,
                        OutboxIdDistinctQueue outboxIdDistinctQueue) {
        this.publisher = publisher;
        this.outboxIdDistinctQueue = outboxIdDistinctQueue;
    }

    @Scheduled(fixedRateString = "${app.outbox.message-relay.process-queue.fixed-rate}")
    public void publishFromQueue() {
        OutboxId id = outboxIdDistinctQueue.poll();
        if (id != null) {
            publisher.tryPublish(id)
                    .onSuccess(logSuccess(id))
                    .onFailure(e -> {
                        log.error("Failed to process message: {} add to DLQ", id, e);
                        outboxIdDistinctQueue.addDlq(id);
                    });
        }
    }

    @Scheduled(fixedRateString = "${app.outbox.message-relay.process-dlq.fixed-rate}")
    public void publishFromDlq() {
        OutboxId id = outboxIdDistinctQueue.pollDlq();
        if (id != null) {
            publisher.tryPublish(id)
                    .onSuccess(logSuccess(id))
                    .onFailure(e -> log.error("Failed to process message: {}", id, e));
        }
    }

    private Consumer<Boolean> logSuccess(OutboxId outboxId) {
        return ok -> {
            if (ok) {
                log.debug("OutboxId: {} was successfully published", outboxId);
            } else {
                log.warn("Failed to process outbox, outbox might be locked or already processed: {}", outboxId);
            }
        };
    }
}
