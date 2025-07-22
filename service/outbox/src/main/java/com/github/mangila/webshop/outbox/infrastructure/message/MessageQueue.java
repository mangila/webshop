package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class MessageQueue {

    private final BlockingQueue<OutboxId> queue;

    public MessageQueue() {
        queue = new LinkedBlockingQueue<>();
    }

    public void add(OutboxId outboxId) {
        queue.add(outboxId);
    }

    public OutboxId poll() {
        return queue.poll();
    }

}
