package com.github.mangila.webshop.outbox.infrastructure.message.task;

public enum OutboxTaskKey {
    FILL_QUEUE,
    PROCESS_QUEUE,
    PROCESS_DLQ;
}

