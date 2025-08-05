package com.github.mangila.webshop.outbox.infrastructure.message.task;

import com.github.mangila.webshop.outbox.application.service.OutboxCommandService;
import com.github.mangila.webshop.outbox.application.service.OutboxQueryService;

public final class CleanUpPublishedOutboxTask implements OutboxTask {

    private final OutboxCommandService commandService;
    private final OutboxQueryService queryService;

    public CleanUpPublishedOutboxTask(OutboxCommandService commandService,
                                      OutboxQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @Override
    public void execute() {
    }

    @Override
    public OutboxTaskKey key() {
        return new OutboxTaskKey("CLEAN_UP_PUBLISHED");
    }
}
