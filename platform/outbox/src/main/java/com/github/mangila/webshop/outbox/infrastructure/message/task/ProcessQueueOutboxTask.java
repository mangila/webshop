package com.github.mangila.webshop.outbox.infrastructure.message.task;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.message.MessageProcessor;
import com.github.mangila.webshop.outbox.infrastructure.message.OutboxQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProcessQueueOutboxTask implements OutboxTask {

    private static final Logger log = LoggerFactory.getLogger(ProcessQueueOutboxTask.class);
    private final OutboxQueue queue;
    private final MessageProcessor processor;

    public ProcessQueueOutboxTask(OutboxQueue queue,
                                  MessageProcessor processor) {
        this.queue = queue;
        this.processor = processor;
    }

    @Override
    public void execute() {
        OutboxId id = queue.poll();
        if (id == null) {
            return;
        }
        processor.tryProcess(id)
                .onSuccess(processed -> {
                    if (processed) {
                        log.debug("Message: {} was successfully processed", id);
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
