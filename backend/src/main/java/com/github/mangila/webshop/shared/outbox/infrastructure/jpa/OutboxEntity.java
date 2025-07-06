
package com.github.mangila.webshop.shared.outbox.infrastructure.jpa;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "outbox_event")
@EntityListeners(AuditingEntityListener.class)
public class OutboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String topic;

    @Column(nullable = false, updatable = false)
    private String event;

    @Column(name = "aggregate_id",
            columnDefinition = "uuid",
            nullable = false,
            updatable = false)
    private UUID aggregateId;

    @Type(JsonBinaryType.class)
    @Column(name = "payload",
            columnDefinition = "jsonb",
            nullable = false,
            updatable = false)
    private JsonNode payload;

    @Column(nullable = false)
    private boolean published;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private @Nullable Instant created;

    protected OutboxEntity() {
    }

    private OutboxEntity(String topic, String event, UUID aggregateId, JsonNode payload, boolean published) {
        this.topic = topic;
        this.event = event;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.published = published;
    }

    public static OutboxEntity from(String topic, String event, UUID aggregateId, JsonNode payload) {
        return new OutboxEntity(topic, event, aggregateId, payload, false);
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

    public void setEvent(String type) {
        this.event = type;
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

    public Optional<Instant> getCreated() {
        return Optional.ofNullable(created);
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}