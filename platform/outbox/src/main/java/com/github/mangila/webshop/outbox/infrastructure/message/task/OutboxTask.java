package com.github.mangila.webshop.outbox.infrastructure.message.task;

public interface OutboxTask {
    void execute();

    OutboxTaskKey key();
}
