package com.github.mangila.webshop.outbox.infrastructure.spring;

import com.github.mangila.webshop.outbox.infrastructure.spring.event.OutboxEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class SpringEventPublisher {

    private final ApplicationEventPublisher publisher;

    public SpringEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(OutboxEvent event) {
        publisher.publishEvent(event);
    }
}
