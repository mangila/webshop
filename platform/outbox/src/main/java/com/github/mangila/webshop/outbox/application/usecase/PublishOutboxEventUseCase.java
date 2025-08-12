package com.github.mangila.webshop.outbox.application.usecase;

import com.github.mangila.webshop.outbox.application.action.command.UpdateOutboxStatusCommandAction;
import com.github.mangila.webshop.outbox.domain.cqrs.command.UpdateOutboxStatusCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.message.OutboxPublisher;
import com.github.mangila.webshop.shared.InternalDistinctQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PublishOutboxEventUseCase {
    private static final Logger log = LoggerFactory.getLogger(PublishOutboxEventUseCase.class);
    private final InternalDistinctQueue<OutboxId> eventQueue;
    private final OutboxPublisher publisher;
    private final UpdateOutboxStatusCommandAction updateOutboxStatusCommandAction;

    public PublishOutboxEventUseCase(InternalDistinctQueue<OutboxId> eventQueue,
                                     OutboxPublisher publisher,
                                     UpdateOutboxStatusCommandAction updateOutboxStatusCommandAction) {
        this.eventQueue = eventQueue;
        this.publisher = publisher;
        this.updateOutboxStatusCommandAction = updateOutboxStatusCommandAction;
    }

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
