package com.github.mangila.webshop.outbox.infrastructure.message.task;

public sealed interface OutboxTask permits CleanUpPublishedOutboxTask, FillQueueOutboxTask, ProcessDlqOutboxTask, ProcessQueueOutboxTask {
    void execute();

    OutboxTaskKey key();
}
