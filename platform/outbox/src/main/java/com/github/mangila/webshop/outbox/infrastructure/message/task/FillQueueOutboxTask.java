package com.github.mangila.webshop.outbox.infrastructure.message.task;

import com.github.mangila.webshop.outbox.application.service.OutboxQueryService;
import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.outbox.infrastructure.message.OutboxQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FillQueueOutboxTask implements OutboxTask {
    private static final Logger log = LoggerFactory.getLogger(FillQueueOutboxTask.class);
    private final OutboxQueryService queryService;
    private final OutboxQueue queue;

    public FillQueueOutboxTask(OutboxQueryService queryService,
                               OutboxQueue queue) {
        this.queryService = queryService;
        this.queue = queue;
    }

    @Override
    public void execute() {
        queryService.findAllByDomainAndStatus(queue.domain(), OutboxStatusType.PENDING, 120)
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
