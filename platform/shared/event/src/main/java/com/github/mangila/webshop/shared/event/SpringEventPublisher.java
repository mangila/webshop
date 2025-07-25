package com.github.mangila.webshop.shared.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringEventPublisher {

    private final ApplicationEventPublisher publisher;

    public SpringEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishDomainEvent(DomainEvent event) {
        publisher.publishEvent(event);
    }

    public void publishDomainMessage(DomainMessage message) {
        publisher.publishEvent(message);
    }
}
