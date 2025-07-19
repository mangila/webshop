package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.infrastructure.message.spring.SpringEventPublisher;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class MessageRelay {

    private static final Logger log = LoggerFactory.getLogger(MessageRelay.class);
    private final BlockingQueue<OutboxMessage> messageQueue;
    private final SpringEventPublisher publisher;
    private volatile boolean running = true;

    public MessageRelay(SpringEventPublisher publisher) {
        this.messageQueue = new LinkedBlockingQueue<>();
        this.publisher = publisher;
    }

    public void relay(OutboxMessage message) {
        messageQueue.add(message);
    }

    @Async
    @EventListener(ApplicationReadyEvent.class)
    void run() {
        Try.of(() -> {
            while (running) {
                OutboxMessage message = messageQueue.poll();
                if (message != null) {
                    publisher.publish(message);
                }
            }
            return null;
        }).onFailure(throwable -> {
            log.error("Error while relaying messages", throwable);
            running = false;
        });
    }
}
