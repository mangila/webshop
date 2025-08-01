package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.model.DomainEvent;
import com.github.mangila.webshop.shared.model.DomainMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(SpringEventPublisher.class);
    private final ApplicationEventPublisher publisher;

    public SpringEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishDomainEvent(DomainEvent event) {
        Ensure.notNull(event, DomainEvent.class);
        log.debug("Publishing DomainEvent: {}", event);
        publisher.publishEvent(event);
    }

    public void publishDomainMessage(DomainMessage message) {
        Ensure.notNull(message, DomainMessage.class);
        log.debug("Publishing DomainMessage: {}", message);
        publisher.publishEvent(message);
    }
}
