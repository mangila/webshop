package com.github.mangila.webshop.outbox.infrastructure.scheduler.job;

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

public record DeletePublishedJob(FindAllOutboxIdsByStatusQueryAction findAllOutboxIdsByStatusQueryAction,
                                 DeleteOutboxCommandAction deleteOutboxCommandAction) implements SimpleTask<OutboxJobKey> {

    private static final Logger log = LoggerFactory.getLogger(DeletePublishedJob.class);

    @Override
    public OutboxJobKey key() {
        return new OutboxJobKey("DELETE_PUBLISHED");
    }

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
}
