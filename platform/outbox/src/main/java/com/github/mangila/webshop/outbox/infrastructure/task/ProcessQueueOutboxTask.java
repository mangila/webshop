package com.github.mangila.webshop.outbox.infrastructure.task;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.message.MessageProcessor;
import com.github.mangila.webshop.shared.InternalQueue;
import com.github.mangila.webshop.shared.SimpleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record ProcessQueueOutboxTask(MessageProcessor processor,
                                     InternalQueue<OutboxId> queue) implements SimpleTask<OutboxTaskKey> {

    private static final Logger log = LoggerFactory.getLogger(ProcessQueueOutboxTask.class);

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
