package com.github.mangila.webshop.outbox.infrastructure.message.task;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.message.MessageProcessor;
import com.github.mangila.webshop.outbox.infrastructure.message.OutboxDomainMessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ProcessQueueOutboxTask implements OutboxTask {

    private static final Logger log = LoggerFactory.getLogger(ProcessQueueOutboxTask.class);
    private final OutboxDomainMessageQueue queue;
    private final MessageProcessor processor;

    public ProcessQueueOutboxTask(OutboxDomainMessageQueue queue,
                                  MessageProcessor processor) {
        this.queue = queue;
        this.processor = processor;
    }

    @Override
    public void execute() {
        OutboxId id = queue.poll();
        if (Objects.isNull(id)) {
            return;
        }
        processor.tryProcess(id)
                .onSuccess(processed -> {
                    if (processed) {
                        log.info("Message: {} was successfully processed", id);
                    } else {
                        log.error("Failed to process message: {} add to DLQ", id);
                        queue.addDlq(id);
                    }
                })
                .onFailure(e -> {
                    log.error("Failed to process message: {} add to DLQ", id, e.getCause());
                    queue.addDlq(id);
                });
    }

    @Override
    public OutboxTaskKey key() {
        return new OutboxTaskKey(queue.domain().value()
                .concat("_")
                .concat("PROCESS_QUEUE")
        );
    }
}
