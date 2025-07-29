package com.github.mangila.webshop.outbox.infrastructure.message.task;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxUpdated;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.outbox.infrastructure.message.InternalMessageQueue;
import com.github.mangila.webshop.outbox.infrastructure.message.MessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ProcessDlqOutboxTask implements OutboxTask {

    public static final OutboxTaskKey KEY = new OutboxTaskKey(ProcessDlqOutboxTask.class.getSimpleName());
    private static final Logger log = LoggerFactory.getLogger(ProcessDlqOutboxTask.class);
    private final InternalMessageQueue internalMessageQueue;
    private final OutboxCommandRepository commandRepository;
    private final MessageProcessor processor;

    public ProcessDlqOutboxTask(InternalMessageQueue internalMessageQueue, OutboxCommandRepository commandRepository, MessageProcessor processor) {
        this.internalMessageQueue = internalMessageQueue;
        this.commandRepository = commandRepository;
        this.processor = processor;
    }

    @Override
    public void execute() {
        OutboxId id = internalMessageQueue.pollDlq();
        if (Objects.isNull(id)) {
            return;
        }
        processor.tryProcess(id)
                .onSuccess(processed -> {
                    if (processed) {
                        log.info("Message: {} was successfully processed", id);
                    } else {
                        log.error("Failed to process message: {} mark as FAILED", id);
                        commandRepository.updateStatus(id, OutboxStatusType.FAILED, OutboxUpdated.now());
                    }
                })
                .onFailure(e -> {
                    log.error("Failed to process message: {} mark as FAILED", id, e);
                    commandRepository.updateStatus(id, OutboxStatusType.FAILED, OutboxUpdated.now());
                });
    }

    @Override
    public OutboxTaskKey key() {
        return KEY;
    }
}
