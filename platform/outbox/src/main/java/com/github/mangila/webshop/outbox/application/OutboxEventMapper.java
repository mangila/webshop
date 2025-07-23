package com.github.mangila.webshop.outbox.application;

import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPayload;
import com.github.mangila.webshop.shared.event.DomainEvent;
import org.springframework.stereotype.Component;

@Component
public class OutboxEventMapper {

    public OutboxInsertCommand toCommand(DomainEvent event) {
        return new OutboxInsertCommand(
                event.domain(),
                event.event(),
                new OutboxAggregateId(event.aggregateId()),
                new OutboxPayload(event.payload())
        );
    }
}
