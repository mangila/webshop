package com.github.mangila.webshop.outbox.infrastructure.message.task;

public sealed interface OutboxTask permits DeletePublishedOutboxTask, FillQueueOutboxTask, ProcessDlqOutboxTask, ProcessQueueOutboxTask {
    void execute();

    OutboxTaskKey key();
}
