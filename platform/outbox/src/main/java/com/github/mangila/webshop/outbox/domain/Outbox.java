
package com.github.mangila.webshop.outbox.domain;


import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.outbox.domain.primitive.*;
import com.github.mangila.webshop.shared.registry.model.Domain;
import com.github.mangila.webshop.shared.registry.model.Event;

import java.time.Instant;
import java.util.UUID;

public class Outbox {

    private OutboxId id;
    private Domain domain;
    private Event event;
    private OutboxAggregateId aggregateId;
    private OutboxPayload payload;
    private OutboxSequence sequence;
    private OutboxPublished published;
    private OutboxCreated created;

    public Outbox(OutboxId id, Domain domain, Event event, OutboxAggregateId aggregateId, OutboxPayload payload, OutboxSequence sequence, OutboxPublished published, OutboxCreated created) {
        this.id = id;
        this.domain = domain;
        this.event = event;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.sequence = sequence;
        this.published = published;
        this.created = created;
    }

    public static Outbox from(Long id, Domain domain, Event event, UUID aggregateId, ObjectNode payload, int sequence, boolean published, Instant instant) {
        var outboxAggregateId = new OutboxAggregateId(aggregateId);
        return new Outbox(
                new OutboxId(id),
                domain,
                event,
                outboxAggregateId,
                new OutboxPayload(payload),
                new OutboxSequence(outboxAggregateId, sequence),
                new OutboxPublished(published),
                new OutboxCreated(instant));
    }

    public OutboxId getId() {
        return id;
    }

    public void setId(OutboxId id) {
        this.id = id;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public OutboxAggregateId getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(OutboxAggregateId aggregateId) {
        this.aggregateId = aggregateId;
    }

    public OutboxPayload getPayload() {
        return payload;
    }

    public void setPayload(OutboxPayload payload) {
        this.payload = payload;
    }

    public OutboxSequence getSequence() {
        return sequence;
    }

    public void setSequence(OutboxSequence sequence) {
        this.sequence = sequence;
    }

    public OutboxPublished getPublished() {
        return published;
    }

    public void setPublished(OutboxPublished published) {
        this.published = published;
    }

    public OutboxCreated getCreated() {
        return created;
    }

    public void setCreated(OutboxCreated created) {
        this.created = created;
    }
}