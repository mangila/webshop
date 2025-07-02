
package com.github.mangila.webshop.backend.event.domain.model;

import com.fasterxml.jackson.databind.JsonNode;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "event")
@EntityListeners(AuditingEntityListener.class)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String topic;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false, name = "aggregate_id")
    private UUID aggregateId;
    @Type(JsonBinaryType.class)
    @Column(name = "payload",
            columnDefinition = "jsonb",
            nullable = false)
    private JsonNode payload;
    @CreatedDate
    private Instant created;

    protected Event() {
    }

    public Event(String topic, String type, UUID aggregateId, JsonNode payload) {
        this(null, topic, type, aggregateId, payload, null);
    }

    private Event(Long id, String topic, String type, UUID aggregateId, JsonNode payload, Instant created) {
        this.id = id;
        this.topic = topic;
        this.type = type;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getType() {
        return type;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public JsonNode getPayload() {
        return payload;
    }

    public Instant getCreated() {
        return created;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setType(String eventType) {
        this.type = eventType;
    }

    public void setAggregateId(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    public void setPayload(JsonNode eventData) {
        this.payload = eventData;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}