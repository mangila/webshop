package com.github.mangila.webshop.outbox.infrastructure.scheduler.job;

import com.github.mangila.webshop.outbox.application.action.command.DeleteOutboxCommandAction;
import com.github.mangila.webshop.outbox.application.action.query.FindAllOutboxIdsByStatusQueryAction;
import com.github.mangila.webshop.outbox.domain.cqrs.command.DeleteOutboxCommand;
import com.github.mangila.webshop.outbox.domain.cqrs.query.FindAllOutboxIdByStatusQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.shared.SimpleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * This class represents a scheduled task designed to delete outbox entries with a specific status.
 * It is implemented as a record and encapsulates actions for querying specific outbox entries
 * and performing deletions on them.
 * <p>
 * The DeletePublishedJob is primarily responsible for:
 * - Querying outbox entries with the status "PUBLISHED".
 * - Iterating over the retrieved outbox entries and attempting to delete them.
 * - Logging the outcome of each deletion operation, either success or failure.
 * <p>
 * This task is identified by the key "DELETE_PUBLISHED" and adheres to the {@code SimpleTask} interface,
 * allowing it to be executed as part of a broader task execution mechanism.
 */
public record DeletePublishedOutboxJob(FindAllOutboxIdsByStatusQueryAction findAllOutboxIdsByStatusQueryAction,
                                       DeleteOutboxCommandAction deleteOutboxCommandAction) implements SimpleTask<OutboxJobKey> {

    private static final Logger log = LoggerFactory.getLogger(DeletePublishedOutboxJob.class);

    private static final OutboxJobKey KEY = new OutboxJobKey("DELETE_PUBLISHED");
    private static final FindAllOutboxIdByStatusQuery QUERY = FindAllOutboxIdByStatusQuery.published(120);

    @Override
    public OutboxJobKey key() {
        return KEY;
    }

    @Override
    public void execute() {
        List<OutboxId> ids = findAllOutboxIdsByStatusQueryAction.execute(QUERY);
        for (OutboxId id : ids) {
            deleteOutboxCommandAction.tryExecute(new DeleteOutboxCommand(id))
                    .onSuccess(ok -> log.debug("Outbox: {} was successfully deleted", id))
                    .onFailure(e -> log.error("Failed to delete Outbox: {}", id, e));
        }
    }
}
