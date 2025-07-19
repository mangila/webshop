package com.github.mangila.webshop.outbox.infrastructure.message.spring;

import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class SpringEventPublisher {

    private final ApplicationEventPublisher publisher;

    public SpringEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(OutboxMessage event) {
        publisher.publishEvent(event);
    }
}
