package com.github.mangila.webshop.outbox.infrastructure.task;

import com.github.mangila.webshop.outbox.application.action.query.FindAllOutboxByDomainAndStatusQueryAction;
import com.github.mangila.webshop.outbox.domain.cqrs.query.FindAllOutboxByDomainAndStatusQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.shared.InternalQueue;
import com.github.mangila.webshop.shared.SimpleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record FillQueueOutboxTask(FindAllOutboxByDomainAndStatusQueryAction findAllOutboxByDomainAndStatusQueryAction,
                                  InternalQueue<OutboxId> queue) implements SimpleTask<OutboxTaskKey> {
    private static final Logger log = LoggerFactory.getLogger(FillQueueOutboxTask.class);

    @Override
    public void execute() {
        var query = new FindAllOutboxByDomainAndStatusQuery(
                queue.domain(),
                OutboxStatusType.PENDING,
                120);
        findAllOutboxByDomainAndStatusQueryAction.execute(query)
                .stream()
                .peek(message -> log.info("Queue Message: {} - {}", message.id(), message.domain()))
                .forEach(message -> queue.add(message.id()));
    }

    @Override
    public OutboxTaskKey key() {
        return new OutboxTaskKey(queue.domain().value()
                .concat("_")
                .concat("FILL_QUEUE")
        );
    }
}
