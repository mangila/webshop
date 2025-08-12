package com.github.mangila.webshop.outbox.infrastructure.scheduler.job;

import com.github.mangila.webshop.outbox.application.action.query.FindAllOutboxIdsByStatusQueryAction;
import com.github.mangila.webshop.outbox.domain.cqrs.query.FindAllOutboxIdByStatusQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.outbox.infrastructure.message.OutboxPublisher;
import com.github.mangila.webshop.shared.SimpleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public record PublishFailedOutboxJob(FindAllOutboxIdsByStatusQueryAction findAllOutboxIdsByStatusQueryAction,
                                     OutboxPublisher outboxPublisher) implements SimpleTask<OutboxJobKey> {

    private static final Logger log = LoggerFactory.getLogger(PublishFailedOutboxJob.class);

    @Override
    public OutboxJobKey key() {
        return new OutboxJobKey("PUBLISH_FAILED");
    }

    @Override
    public void execute() {
        List<OutboxId> ids = findAllOutboxIdsByStatusQueryAction.execute(new FindAllOutboxIdByStatusQuery(
                OutboxStatusType.FAILED,
                50
        ));
        for (OutboxId id : ids) {
            outboxPublisher.tryPublish(id)
                    .onSuccess(ok -> {
                        if (!ok) {
                            log.error("Failed to process message: {}", id);
                        }
                    })
                    .onFailure(throwable -> log.error("Failed to process message: {}", id, throwable));
        }
    }
}
