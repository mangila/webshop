package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.shared.model.Domain;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class OutboxDomainMessageQueue {

    private final Domain domain;
    private final Queue<OutboxId> queue;
    private final Queue<OutboxId> dlq;

    public OutboxDomainMessageQueue(Domain domain) {
        this.domain = domain;
        this.queue = new ConcurrentLinkedQueue<>();
        this.dlq = new ConcurrentLinkedQueue<>();
    }

    public Domain domain() {
        return domain;
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
