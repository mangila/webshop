package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.model.InboxEvent;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * Convenience class for publishing events.
 * All ApplicationEventPublisher calls are delegated to this class.
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

    public Function<OutboxEvent, OutboxEvent> publishOutboxEvent() {
        return outboxEvent -> {
            publishOutboxEvent(outboxEvent);
            return outboxEvent;
        };
    }

    public void publishInboxEvent(@NotNull InboxEvent inboxEvent) {
        Ensure.notNull(inboxEvent, InboxEvent.class);
        publisher.publishEvent(inboxEvent);
    }
}
