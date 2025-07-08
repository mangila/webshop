package com.github.mangila.webshop.shared.outbox.domain.cqrs;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxDomain;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxEvent;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxPayload;

import java.util.UUID;

public record OutboxInsert(
        OutboxDomain domain,
        OutboxEvent event,
        OutboxAggregateId aggregateId,
        OutboxPayload payload
) {

    public static OutboxInsert from(String domain, String event, UUID aggregateId, JsonNode payload) {
        return new OutboxInsert(new OutboxDomain(domain), new OutboxEvent(event), new OutboxAggregateId(aggregateId), new OutboxPayload(payload));
    }
}
