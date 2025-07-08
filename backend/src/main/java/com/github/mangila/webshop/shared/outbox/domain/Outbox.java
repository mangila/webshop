
package com.github.mangila.webshop.shared.outbox.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.shared.outbox.domain.primitive.*;

import java.time.Instant;
import java.util.UUID;

public class Outbox {

    private OutboxId id;
    private OutboxDomain domain;
    private OutboxEvent event;
    private OutboxAggregateId aggregateId;
    private OutboxPayload payload;
    private OutboxPublished published;
    private OutboxCreated created;

    public Outbox(OutboxId id, OutboxDomain domain, OutboxEvent event, OutboxAggregateId aggregateId, OutboxPayload payload, OutboxPublished published, OutboxCreated created) {
        this.id = id;
        this.domain = domain;
        this.event = event;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.published = published;
        this.created = created;
    }

    public static Outbox from(Long id, String domain, String event, UUID aggregateId, JsonNode payload, boolean published, Instant instant) {
        return new Outbox(new OutboxId(id), new OutboxDomain(domain), new OutboxEvent(event), new OutboxAggregateId(aggregateId), new OutboxPayload(payload), new OutboxPublished(published), new OutboxCreated(instant));
    }

    public OutboxId getId() {
        return id;
    }

    public OutboxDomain getDomain() {
        return domain;
    }

    public OutboxEvent getEvent() {
        return event;
    }

    public OutboxAggregateId getAggregateId() {
        return aggregateId;
    }

    public OutboxPayload getPayload() {
        return payload;
    }

    public OutboxPublished getPublished() {
        return published;
    }

    public OutboxCreated getCreated() {
        return created;
    }
}