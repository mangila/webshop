package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.projection.OutboxProjection;
import com.github.mangila.webshop.shared.model.EventSource;
import com.github.mangila.webshop.shared.model.InboxEvent;
import org.springframework.stereotype.Component;

@Component
public class InboxEventMapper {
    public InboxEvent toInboxEvent(OutboxProjection outboxProjection, EventSource source) {
        return new InboxEvent(
                outboxProjection.id().value(),
                outboxProjection.aggregateId().value(),
                outboxProjection.domain(),
                outboxProjection.event(),
                outboxProjection.payload().value(),
                outboxProjection.sequence().value(),
                source
        );
    }

}
