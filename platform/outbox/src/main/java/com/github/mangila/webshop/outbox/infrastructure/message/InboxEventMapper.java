package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.shared.model.EventSource;
import com.github.mangila.webshop.shared.model.InboxEvent;
import org.springframework.stereotype.Component;

@Component
public class InboxEventMapper {
    public InboxEvent toInboxEvent(Outbox outbox, EventSource source) {
        return new InboxEvent(
                outbox.id().value(),
                outbox.aggregateId().value(),
                outbox.domain(),
                outbox.event(),
                outbox.payload().value(),
                outbox.sequence().value(),
                source
        );
    }

}
