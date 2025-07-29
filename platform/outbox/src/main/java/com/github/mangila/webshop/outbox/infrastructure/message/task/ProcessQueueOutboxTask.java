package com.github.mangila.webshop.outbox.infrastructure.message.task;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.message.InternalMessageQueue;
import com.github.mangila.webshop.outbox.infrastructure.message.MessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ProcessQueueOutboxTask implements OutboxTask {

    private static final Logger log = LoggerFactory.getLogger(ProcessQueueOutboxTask.class);
    public static final OutboxTaskKey KEY = new OutboxTaskKey(ProcessQueueOutboxTask.class.getSimpleName());
    private final InternalMessageQueue internalMessageQueue;
    private final MessageProcessor processor;

    public ProcessQueueOutboxTask(InternalMessageQueue internalMessageQueue, OutboxCommandRepository commandRepository, MessageProcessor processor) {
        this.internalMessageQueue = internalMessageQueue;
        this.processor = processor;
    }

    @Override
    public void execute() {
        OutboxId id = internalMessageQueue.poll();
        if (Objects.isNull(id)) {
            return;
        }
        processor.tryProcess(id)
                .onSuccess(processed -> {
                    if (processed) {
                        log.info("Message: {} was successfully processed", id);
                    } else {
                        log.error("Failed to process message: {} add to DLQ", id);
                        internalMessageQueue.addDlq(id);
                    }
                })
                .onFailure(e -> {
                    log.error("Failed to process message: {} add to DLQ", id, e.getCause());
                    internalMessageQueue.addDlq(id);
                });
    }

    @Override
    public OutboxTaskKey key() {
        return KEY;
    }
}
