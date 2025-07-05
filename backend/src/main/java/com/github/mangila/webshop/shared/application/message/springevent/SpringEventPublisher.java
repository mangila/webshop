package com.github.mangila.webshop.shared.application.message.springevent;

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

    public void publish(OutboxPgNotification event) {
        publisher.publishEvent(event);
    }
}
