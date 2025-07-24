package com.github.mangila.webshop.shared.event;

import com.github.mangila.webshop.shared.annotation.ObservedComponent;
import org.springframework.context.ApplicationEventPublisher;

@ObservedComponent
public class SpringEventPublisher {

    private final ApplicationEventPublisher publisher;

    public SpringEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishEvent(DomainEvent event) {
        publisher.publishEvent(event);
    }

    public void publishMessage(DomainMessage message) {
        publisher.publishEvent(message);
    }
}
