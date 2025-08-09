package com.github.mangila.webshop.outbox.infrastructure.task;

import com.github.mangila.webshop.outbox.application.action.command.DeleteOutboxCommandAction;
import com.github.mangila.webshop.outbox.application.action.query.FindAllOutboxIdsByStatusAndDateBeforeQueryAction;
import com.github.mangila.webshop.outbox.domain.cqrs.command.DeleteOutboxCommand;
import com.github.mangila.webshop.outbox.domain.cqrs.query.FindAllOutboxIdsByStatusAndDateBeforeQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.shared.SimpleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public record DeletePublishedOutboxTask(
        FindAllOutboxIdsByStatusAndDateBeforeQueryAction findAllOutboxIdsByStatusAndDateBeforeQueryAction,
        DeleteOutboxCommandAction deleteOutboxCommandAction) implements SimpleTask<OutboxTaskKey> {

    private static final Logger log = LoggerFactory.getLogger(DeletePublishedOutboxTask.class);

    @Override
    public void execute() {
        var query = new FindAllOutboxIdsByStatusAndDateBeforeQuery(
                OutboxStatusType.PUBLISHED,
                Instant.now().minus(7, DAYS),
                50
        );
        List<OutboxId> ids = findAllOutboxIdsByStatusAndDateBeforeQueryAction.execute(query);
        for (OutboxId id : ids) {
            deleteOutboxCommandAction.tryExecute(new DeleteOutboxCommand(id))
                    .onSuccess(processed -> log.info("Outbox: {} was successfully deleted", id))
                    .onFailure(e -> log.error("Failed to delete Outbox: {}", id, e));
        }
    }

    @Override
    public OutboxTaskKey key() {
        return new OutboxTaskKey("DELETE_PUBLISHED");
    }
}
