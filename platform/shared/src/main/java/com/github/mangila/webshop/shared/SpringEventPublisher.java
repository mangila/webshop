package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.model.InboxEvent;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

/**
 * The {@code SpringEventPublisher} class is responsible for publishing
 * application events in a Spring-based application. It acts as a wrapper
 * around Spring's {@code ApplicationEventPublisher}, providing methods
 * to publish specific types of events
 * <p>
 * This class ensures that the events being published are not null before
 * delegating the event publishing to the underlying {@code ApplicationEventPublisher}.
 */
@Component
public class SpringEventPublisher {
    private final ApplicationEventPublisher publisher;

    public SpringEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishOutboxEvent(OutboxEvent outboxEvent) {
        Ensure.notNull(outboxEvent, OutboxEvent.class);
        publisher.publishEvent(outboxEvent);
    }

    public UnaryOperator<OutboxEvent> publishOutboxEvent() {
        return outboxEvent -> {
            publishOutboxEvent(outboxEvent);
            return outboxEvent;
        };
    }

    public void publishInboxEvent(InboxEvent inboxEvent) {
        Ensure.notNull(inboxEvent, InboxEvent.class);
        publisher.publishEvent(inboxEvent);
    }
}
