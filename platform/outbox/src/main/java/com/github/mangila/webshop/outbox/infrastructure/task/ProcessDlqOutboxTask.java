package com.github.mangila.webshop.outbox.infrastructure.task;

import com.github.mangila.webshop.outbox.application.action.command.UpdateOutboxStatusCommandAction;
import com.github.mangila.webshop.outbox.domain.cqrs.command.UpdateOutboxStatusCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.message.MessageProcessor;
import com.github.mangila.webshop.shared.InternalQueue;
import com.github.mangila.webshop.shared.SimpleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record ProcessDlqOutboxTask(UpdateOutboxStatusCommandAction updateOutboxStatusCommandAction,
                                   MessageProcessor processor,
                                   InternalQueue<OutboxId> queue) implements SimpleTask<OutboxTaskKey> {
    private static final Logger log = LoggerFactory.getLogger(ProcessDlqOutboxTask.class);

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
                        updateOutboxStatusCommandAction.execute(UpdateOutboxStatusCommand.failed(id));
                    }
                })
                .onFailure(e -> {
                    log.error("Failed to process message: {} mark as FAILED", id, e);
                    updateOutboxStatusCommandAction.execute(UpdateOutboxStatusCommand.failed(id));
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
