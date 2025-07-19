package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class MessageQueue {

    private final BlockingQueue<OutboxMessage> queue;

    public MessageQueue() {
        queue = new LinkedBlockingQueue<>();
    }

    public void add(OutboxMessage message) {
        queue.add(message);
    }

    public OutboxMessage poll() {
        return queue.poll();
    }
}
