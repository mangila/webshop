package com.github.mangila.webshop.outbox.infrastructure.scheduler.job;

import com.github.mangila.webshop.outbox.application.action.query.FindAllOutboxByDomainAndStatusQueryAction;
import com.github.mangila.webshop.outbox.domain.cqrs.query.FindAllOutboxByDomainAndStatusQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.shared.InternalQueue;
import com.github.mangila.webshop.shared.SimpleTask;
import com.github.mangila.webshop.shared.model.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public record FillQueuesJob(FindAllOutboxByDomainAndStatusQueryAction findAllOutboxByDomainAndStatusQueryAction,
                            Map<Domain, InternalQueue<OutboxId>> domainToOutboxIdQueue) implements SimpleTask<OutboxJobKey> {

    private static final Logger log = LoggerFactory.getLogger(FillQueuesJob.class);

    @Override
    public OutboxJobKey key() {
        return new OutboxJobKey("FILL_QUEUES");
    }

    @Override
    public void execute() {
        for (InternalQueue<OutboxId> queue : domainToOutboxIdQueue.values()) {
            findAllOutboxByDomainAndStatusQueryAction.execute(new FindAllOutboxByDomainAndStatusQuery(
                            queue.domain(),
                            OutboxStatusType.PENDING,
                            120))
                    .stream()
                    .peek(outbox -> log.info("Queue Message: {} - {}", outbox.id(), outbox.domain()))
                    .forEach(outbox -> {
                        queue.add(outbox.id());
                    });
        }
    }
}
