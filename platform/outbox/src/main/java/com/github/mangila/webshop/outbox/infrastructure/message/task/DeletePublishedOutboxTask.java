package com.github.mangila.webshop.outbox.infrastructure.message.task;

import com.github.mangila.webshop.outbox.application.service.OutboxCommandService;
import com.github.mangila.webshop.outbox.application.service.OutboxQueryService;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxStatusAndDateBeforeQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

public final class DeletePublishedOutboxTask implements OutboxTask {

    private static final Logger log = LoggerFactory.getLogger(DeletePublishedOutboxTask.class);
    private final OutboxCommandService commandService;
    private final OutboxQueryService queryService;

    public DeletePublishedOutboxTask(OutboxCommandService commandService,
                                     OutboxQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Override
    public void execute() {
        var query = new OutboxStatusAndDateBeforeQuery(
                OutboxStatusType.PUBLISHED,
                Instant.now().minus(7, DAYS),
                50
        );
        List<OutboxId> ids = queryService.findAllIdsByStatusAndDateBefore(query)
                .stream()
                .peek(outboxId -> log.debug("Delete Outbox: {}", outboxId))
                .toList();
        commandService.deleteByIds(ids);
    }

    @Override
    public OutboxTaskKey key() {
        return new OutboxTaskKey("DELETE_PUBLISHED");
    }
}
