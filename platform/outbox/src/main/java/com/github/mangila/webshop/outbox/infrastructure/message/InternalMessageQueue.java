package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class InternalMessageQueue {

    private final Queue<OutboxId> queue;
    private final Queue<OutboxId> dlq;

    public InternalMessageQueue() {
        this.queue = new ConcurrentLinkedQueue<>();
        this.dlq = new ConcurrentLinkedQueue<>();
    }

    public void add(OutboxId outboxId) {
        queue.add(outboxId);
    }

    public OutboxId poll() {
        return queue.poll();
    }

    public void addDlq(OutboxId outboxId) {
        dlq.add(outboxId);
    }

    public OutboxId pollDlq() {
        return dlq.poll();
    }
}
