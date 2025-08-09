package com.github.mangila.webshop.outbox.infrastructure.task;

import com.github.mangila.webshop.outbox.application.action.command.DeleteOutboxCommandAction;
import com.github.mangila.webshop.outbox.application.action.query.FindAllOutboxIdsByStatusQueryAction;
import com.github.mangila.webshop.outbox.domain.cqrs.command.DeleteOutboxCommand;
import com.github.mangila.webshop.outbox.domain.cqrs.query.FindAllOutboxIdByStatusQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.shared.SimpleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public record DeletePublishedOutboxTask(
        FindAllOutboxIdsByStatusQueryAction findAllOutboxIdsByStatusQueryAction,
        DeleteOutboxCommandAction deleteOutboxCommandAction) implements SimpleTask<OutboxTaskKey> {

    private static final Logger log = LoggerFactory.getLogger(DeletePublishedOutboxTask.class);

    @Override
    public void execute() {
        var query = new FindAllOutboxIdByStatusQuery(
                OutboxStatusType.PUBLISHED,
                50
        );
        List<OutboxId> ids = findAllOutboxIdsByStatusQueryAction.execute(query);
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
