package com.github.mangila.webshop.outbox.infrastructure.message.task;

public enum OutboxTaskKey {
    FILL_QUEUE_OUTBOX_TASK,
    PROCESS_QUEUE_OUTBOX_TASK,
    PROCESS_DLQ_OUTBOX_TASK;
}

