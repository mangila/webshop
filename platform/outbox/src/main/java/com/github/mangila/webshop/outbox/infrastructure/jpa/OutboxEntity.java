
package com.github.mangila.webshop.outbox.infrastructure.jpa;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox")
public class OutboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String domain;

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
    private ObjectNode payload;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private OutboxStatusType status;

    @Column(nullable = false, updatable = false)
    private int sequence;

    @Column(nullable = false)
    private Instant updated;

    @Column(nullable = false, updatable = false)
    private Instant created;

    protected OutboxEntity() {
    }

    private OutboxEntity(String domain, String event, UUID aggregateId, ObjectNode payload, OutboxStatusType status, int sequence, Instant updated, Instant created) {
        this.domain = domain;
        this.event = event;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.sequence = sequence;
        this.status = status;
        this.updated = updated;
        this.created = created;
    }

    public static OutboxEntity from(String domain, String event, UUID aggregateId, ObjectNode payload, int sequence) {
        return new OutboxEntity(domain, event, aggregateId, payload, OutboxStatusType.PENDING, sequence, Instant.now(), Instant.now());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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

    public ObjectNode getPayload() {
        return payload;
    }

    public void setPayload(ObjectNode payload) {
        this.payload = payload;
    }

    public OutboxStatusType getStatus() {
        return status;
    }

    public void setStatus(OutboxStatusType status) {
        this.status = status;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}
