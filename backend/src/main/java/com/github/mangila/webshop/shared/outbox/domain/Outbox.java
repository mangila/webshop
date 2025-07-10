
package com.github.mangila.webshop.shared.outbox.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.shared.application.registry.Domain;
import com.github.mangila.webshop.shared.application.registry.Event;
import com.github.mangila.webshop.shared.outbox.domain.primitive.*;

import java.time.Instant;
import java.util.UUID;

public class Outbox {

    private OutboxId id;
    private Domain domain;
    private Event event;
    private OutboxAggregateId aggregateId;
    private OutboxPayload payload;
    private OutboxPublished published;
    private OutboxCreated created;

    public Outbox(OutboxId id, Domain domain, Event event, OutboxAggregateId aggregateId, OutboxPayload payload, OutboxPublished published, OutboxCreated created) {
        this.id = id;
        this.domain = domain;
        this.event = event;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.published = published;
        this.created = created;
    }

    public static Outbox from(Long id, Domain domain, Event event, UUID aggregateId, JsonNode payload, boolean published, Instant instant) {
        return new Outbox(new OutboxId(id), domain, event, new OutboxAggregateId(aggregateId), new OutboxPayload(payload), new OutboxPublished(published), new OutboxCreated(instant));
    }

    public OutboxId getId() {
        return id;
    }

    public Domain getDomain() {
        return domain;
    }

    public Event getEvent() {
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