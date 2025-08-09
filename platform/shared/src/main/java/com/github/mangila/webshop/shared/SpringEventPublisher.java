package com.github.mangila.webshop.shared;

import com.github.mangila.webshop.shared.model.InboxEvent;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.function.Function;

@Component
@Validated
public class SpringEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(SpringEventPublisher.class);
    private final ApplicationEventPublisher publisher;

    public SpringEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publishOutboxEvent(@NotNull OutboxEvent event) {
        log.debug("Publishing OutboxEvent: {}", event);
        publisher.publishEvent(event);
    }

    public Function<OutboxEvent, OutboxEvent> publishOutboxEvent() {
        return outboxEvent -> {
            publishOutboxEvent(outboxEvent);
            return outboxEvent;
        };
    }

    public void publishInboxEvent(@NotNull InboxEvent message) {
        log.debug("Publishing InboxEvent: {}", message);
        publisher.publishEvent(message);
    }
}
