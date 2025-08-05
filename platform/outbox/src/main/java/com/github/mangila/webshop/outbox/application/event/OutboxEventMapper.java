package com.github.mangila.webshop.outbox.application.event;

import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPayload;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OutboxEventMapper {

    public OutboxInsertCommand toCommand(OutboxEvent event, OutboxSequence sequence) {
        UUID aggregateId = event.aggregateId();
        return new OutboxInsertCommand(
                event.domain(),
                event.event(),
                new OutboxAggregateId(aggregateId),
                new OutboxPayload(event.payload()),
                sequence
        );
    }

}
