package com.github.mangila.webshop.outbox.infrastructure.scheduler.job;

import com.github.mangila.webshop.shared.SimpleTask;

public record PublishFailedOutboxJob() implements SimpleTask<OutboxJobKey> {

    @Override
    public OutboxJobKey key() {
        return null;
    }

    @Override
    public void execute() {

    }
}
