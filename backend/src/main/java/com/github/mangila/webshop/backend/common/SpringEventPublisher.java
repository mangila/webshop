package com.github.mangila.webshop.backend.common;

import com.github.mangila.webshop.backend.outboxevent.domain.springevent.OutboxEventPostgresListenerFailedEvent;
import com.github.mangila.webshop.backend.outboxevent.domain.springevent.OutboxEventPostgresNotification;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class SpringEventPublisher {

    private final ApplicationEventPublisher publisher;

    public SpringEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(OutboxEventPostgresListenerFailedEvent event) {
        publisher.publishEvent(event);
    }

    public void publish(OutboxEventPostgresNotification event) {
        publisher.publishEvent(event);
    }
}
