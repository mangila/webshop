
package com.github.mangila.webshop.shared.domain.outbox.infrastructure.jpa;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.outboxevent.domain.command.OutboxEventInsertCommand;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
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
    private String type;

    @Column(name = "aggregate_id", nullable = false, updatable = false)
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
    private Instant created;

    protected OutboxEntity() {
    }

    private OutboxEntity(Long id, String topic, String type, UUID aggregateId, JsonNode payload, boolean published, Instant created) {
        this.id = id;
        this.topic = topic;
        this.type = type;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.published = published;
        this.created = created;
    }

    private OutboxEntity(String topic, String type, UUID aggregateId, JsonNode payload) {
        this(null, topic, type, aggregateId, payload, false, null);
    }

    public static OutboxEntity from(OutboxEventInsertCommand command) {
        return new OutboxEntity(command.topic(), command.type(), command.aggregateId(), command.payload());
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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