package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.infrastructure.message.spring.SpringEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageRelay {

    private static final Logger log = LoggerFactory.getLogger(MessageRelay.class);
    private final MessageQueue messageQueue;
    private final SpringEventPublisher publisher;

    public MessageRelay(MessageQueue messageQueue, SpringEventPublisher publisher) {
        this.messageQueue = messageQueue;
        this.publisher = publisher;
    }

    public void relay(OutboxMessage message) {
        messageQueue.add(message);
    }
}
