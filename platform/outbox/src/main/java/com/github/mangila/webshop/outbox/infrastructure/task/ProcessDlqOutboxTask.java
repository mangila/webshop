package com.github.mangila.webshop.outbox.infrastructure.task;

import com.github.mangila.webshop.outbox.application.service.OutboxCommandService;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxUpdateStatusCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.message.MessageProcessor;
import com.github.mangila.webshop.shared.InternalQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProcessDlqOutboxTask implements OutboxTask {
    private static final Logger log = LoggerFactory.getLogger(ProcessDlqOutboxTask.class);

    private final OutboxCommandService commandService;
    private final MessageProcessor processor;
    private final InternalQueue<OutboxId> queue;

    public ProcessDlqOutboxTask(OutboxCommandService commandService,
                                MessageProcessor processor,
                                InternalQueue<OutboxId> queue) {
        this.commandService = commandService;
        this.processor = processor;
        this.queue = queue;
    }

    @Override
    public void execute() {
        OutboxId id = queue.pollDlq();
        if (id == null) {
            return;
        }
        processor.tryProcess(id)
                .onSuccess(processed -> {
                    if (processed) {
                        log.info("Message: {} was successfully processed", id);
                    } else {
                        log.error("Failed to process message: {} mark as FAILED", id);
                        commandService.updateStatus(OutboxUpdateStatusCommand.failed(id));
                    }
                })
                .onFailure(e -> {
                    log.error("Failed to process message: {} mark as FAILED", id, e);
                    commandService.updateStatus(OutboxUpdateStatusCommand.failed(id));
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
