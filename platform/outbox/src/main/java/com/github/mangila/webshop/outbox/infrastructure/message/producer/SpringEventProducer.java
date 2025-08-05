package com.github.mangila.webshop.outbox.infrastructure.message.producer;

import com.github.mangila.webshop.outbox.domain.projection.OutboxProjection;
import com.github.mangila.webshop.outbox.infrastructure.message.InboxEventMapper;
import com.github.mangila.webshop.shared.SpringEventPublisher;
import com.github.mangila.webshop.shared.model.EventSource;
import com.github.mangila.webshop.shared.model.InboxEvent;
import org.springframework.stereotype.Component;

@Component
public final class SpringEventProducer implements Producer {

    private final InboxEventMapper eventMapper;
    private final SpringEventPublisher publisher;

    public SpringEventProducer(InboxEventMapper eventMapper,
                               SpringEventPublisher publisher) {
        this.eventMapper = eventMapper;
        this.publisher = publisher;
    }

    @Override
    public void produce(OutboxProjection projection) {
        InboxEvent inboxEvent = eventMapper.toInboxEvent(projection, source());
        publisher.publishInboxEvent(inboxEvent);
    }

    @Override
    public EventSource source() {
        return EventSource.SPRING_EVENT;
    }
}
