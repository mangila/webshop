package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class InternalMessageQueue {

    private final Queue<OutboxId> queue;

    public InternalMessageQueue() {
        queue = new ConcurrentLinkedQueue<>();
    }

    public void add(OutboxId outboxId) {
        queue.add(outboxId);
    }

    public OutboxId poll() {
        return queue.poll();
    }
}
