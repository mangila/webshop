
package com.github.mangila.webshop.shared.outbox.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.shared.outbox.domain.primitive.*;

import java.time.Instant;
import java.util.UUID;

public class Outbox {

    private OutboxId id;
    private OutboxTopic topic;
    private OutboxEvent event;
    private OutboxAggregateId aggregateId;
    private OutboxPayload payload;
    private OutboxPublished published;
    private OutboxCreated created;

    public Outbox(OutboxId id, OutboxTopic topic, OutboxEvent event, OutboxAggregateId aggregateId, OutboxPayload payload, OutboxPublished published, OutboxCreated created) {
        this.id = id;
        this.topic = topic;
        this.event = event;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.published = published;
        this.created = created;
    }

    public static Outbox from(Long id, String topic, String event, UUID aggregateId, JsonNode payload, boolean published, Instant instant) {
        return new Outbox(new OutboxId(id), new OutboxTopic(topic), new OutboxEvent(event), new OutboxAggregateId(aggregateId), new OutboxPayload(payload), new OutboxPublished(published), new OutboxCreated(instant));
    }

    public OutboxId getId() {
        return id;
    }

    public OutboxTopic getTopic() {
        return topic;
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