package com.github.mangila.webshop.shared.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class SpringEventPublisher {

    private final ApplicationEventPublisher publisher;

    public SpringEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishEvent(DomainEvent event) {
        publisher.publishEvent(event);
    }

    public void publishMessage(DomainMessage domainMessage) {
        publisher.publishEvent(domainMessage);
    }
}
