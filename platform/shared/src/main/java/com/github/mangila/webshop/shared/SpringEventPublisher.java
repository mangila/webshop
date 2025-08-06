package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.model.InboxEvent;
import com.github.mangila.webshop.shared.model.OutboxEvent;
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

    public void publishOutboxEvent(OutboxEvent event) {
        Ensure.notNull(event, OutboxEvent.class);
        log.debug("Publishing OutboxEvent: {}", event);
        publisher.publishEvent(event);
    }

    public void publishInboxEvent(InboxEvent message) {
        Ensure.notNull(message, InboxEvent.class);
        log.debug("Publishing InboxEvent: {}", message);
        publisher.publishEvent(message);
    }
}
