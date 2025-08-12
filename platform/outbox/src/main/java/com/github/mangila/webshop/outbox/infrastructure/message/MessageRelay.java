package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.application.action.command.UpdateOutboxStatusCommandAction;
import com.github.mangila.webshop.outbox.domain.cqrs.command.UpdateOutboxStatusCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.shared.InternalDistinctQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
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
    private final InternalDistinctQueue<OutboxId> eventQueue;
    private final UpdateOutboxStatusCommandAction updateOutboxStatusCommandAction;


    public MessageRelay(OutboxPublisher publisher,
                        InternalDistinctQueue<OutboxId> eventQueue,
                        UpdateOutboxStatusCommandAction updateOutboxStatusCommandAction) {
        this.publisher = publisher;
        this.eventQueue = eventQueue;
        this.updateOutboxStatusCommandAction = updateOutboxStatusCommandAction;
    }

    @Scheduled(fixedRateString = "${app.outbox.message-relay.process-queue.fixed-rate}")
    public void publishFromQueue() {
        OutboxId id = eventQueue.poll();
        if (id != null) {
            publisher.tryPublish(id)
                    .onSuccess(ok -> {
                        if (!ok) {
                            log.error("Failed to process message: {} add to DLQ", id);
                            eventQueue.addDlq(id);
                        }
                    })
                    .onFailure(e -> {
                        log.error("Failed to process message: {} add to DLQ", id, e.getCause());
                        eventQueue.addDlq(id);
                    });
        }
    }

    @Scheduled(fixedRateString = "${app.outbox.message-relay.process-dlq.fixed-rate}")
    public void publishFromDlq() {
        OutboxId id = eventQueue.pollDlq();
        if (id != null) {
            publisher.tryPublish(id)
                    .onSuccess(ok -> {
                        if (!ok) {
                            log.error("Failed to process message: {} mark as FAILED", id);
                            updateOutboxStatusCommandAction.execute(UpdateOutboxStatusCommand.failed(id));
                        }
                    })
                    .onFailure(e -> {
                        log.error("Failed to process message: {} mark as FAILED", id, e);
                        updateOutboxStatusCommandAction.execute(UpdateOutboxStatusCommand.failed(id));
                    });
        }
    }
}
