package com.github.mangila.webshop.outbox.infrastructure.scheduler.job;

import com.github.mangila.webshop.outbox.application.action.query.FindAllOutboxIdsByDomainAndStatusQueryAction;
import com.github.mangila.webshop.outbox.domain.cqrs.query.FindAllOutboxIdsByDomainAndStatusQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.shared.InternalQueue;
import com.github.mangila.webshop.shared.SimpleTask;
import com.github.mangila.webshop.shared.model.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public record FillQueuesJob(FindAllOutboxIdsByDomainAndStatusQueryAction findAllOutboxIdsByDomainAndStatusQueryAction,
                            Map<Domain, InternalQueue<OutboxId>> domainToOutboxIdQueue) implements SimpleTask<OutboxJobKey> {

    private static final Logger log = LoggerFactory.getLogger(FillQueuesJob.class);

    @Override
    public OutboxJobKey key() {
        return new OutboxJobKey("FILL_QUEUES");
    }

    @Override
    public void execute() {
        for (InternalQueue<OutboxId> queue : domainToOutboxIdQueue.values()) {
            findAllOutboxIdsByDomainAndStatusQueryAction.execute(new FindAllOutboxIdsByDomainAndStatusQuery(
                            queue.domain(),
                            OutboxStatusType.PENDING,
                            120))
                    .stream()
                    .peek(outboxId -> log.info("Queue Message: {} - {}", outboxId, queue.domain()))
                    .forEach(queue::add);
        }
    }
}
