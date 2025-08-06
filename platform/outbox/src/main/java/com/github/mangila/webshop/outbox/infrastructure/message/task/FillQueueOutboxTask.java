package com.github.mangila.webshop.outbox.infrastructure.message.task;

import com.github.mangila.webshop.outbox.application.service.OutboxQueryService;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxDomainAndStatusQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.shared.InternalQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FillQueueOutboxTask implements OutboxTask {
    private static final Logger log = LoggerFactory.getLogger(FillQueueOutboxTask.class);
    private final OutboxQueryService queryService;
    private final InternalQueue<OutboxId> queue;

    public FillQueueOutboxTask(OutboxQueryService queryService,
                               InternalQueue<OutboxId> queue) {
        this.queryService = queryService;
        this.queue = queue;
    }

    @Override
    public void execute() {
        var query = new OutboxDomainAndStatusQuery(
                queue.domain(),
                OutboxStatusType.PENDING,
                120);
        queryService.findAllByDomainAndStatus(query)
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
