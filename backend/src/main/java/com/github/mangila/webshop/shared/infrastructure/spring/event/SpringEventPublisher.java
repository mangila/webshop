package com.github.mangila.webshop.shared.infrastructure.spring.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class SpringEventPublisher {

    private final ApplicationEventPublisher publisher;

    public SpringEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(OutboxPgListenerFailedEvent event) {
        publisher.publishEvent(event);
    }

}
