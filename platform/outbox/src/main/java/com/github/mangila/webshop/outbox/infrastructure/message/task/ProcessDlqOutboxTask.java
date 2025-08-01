package com.github.mangila.webshop.outbox.infrastructure.message.task;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxUpdated;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.outbox.infrastructure.message.MessageProcessor;
import com.github.mangila.webshop.outbox.infrastructure.message.OutboxDomainMessageQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ProcessDlqOutboxTask implements OutboxTask {
    private static final Logger log = LoggerFactory.getLogger(ProcessDlqOutboxTask.class);
    private final OutboxDomainMessageQueue queue;
    private final OutboxCommandRepository commandRepository;
    private final MessageProcessor processor;

    public ProcessDlqOutboxTask(OutboxDomainMessageQueue queue,
                                OutboxCommandRepository commandRepository,
                                MessageProcessor processor) {
        this.queue = queue;
        this.commandRepository = commandRepository;
        this.processor = processor;
    }

    @Override
    public void execute() {
        OutboxId id = queue.pollDlq();
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
        return new OutboxTaskKey(queue.domain().value()
                .concat("_")
                .concat("PROCESS_DLQ")
        );
    }
}
