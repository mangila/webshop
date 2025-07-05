
package com.github.mangila.webshop.shared.outbox.domain;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;
import java.util.UUID;

public class Outbox {

    private Long id;
    private String topic;
    private String event;
    private UUID aggregateId;
    private JsonNode payload;
    private boolean published;
    private Instant created;

    private Outbox(Long id, String topic, String event, UUID aggregateId, JsonNode payload, boolean published, Instant created) {
        this.id = id;
        this.topic = topic;
        this.event = event;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.published = published;
        this.created = created;
    }

    public static Outbox from(Long id, String topic, String event, UUID aggregateId, JsonNode payload, boolean published, Instant instant) {
        return new Outbox(id, topic, event, aggregateId, payload, published, instant);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    public JsonNode getPayload() {
        return payload;
    }

    public void setPayload(JsonNode payload) {
        this.payload = payload;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}