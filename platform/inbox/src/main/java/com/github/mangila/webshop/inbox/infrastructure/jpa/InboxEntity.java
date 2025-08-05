package com.github.mangila.webshop.inbox.infrastructure.jpa;

import com.fasterxml.jackson.databind.node.ObjectNode;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.Immutable;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "inbox")
@Immutable
public class InboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String domain;

    @Column(nullable = false)
    private String event;

    @Column(name = "aggregate_id",
            columnDefinition = "uuid",
            nullable = false)
    private UUID aggregateId;

    @Type(JsonBinaryType.class)
    @Column(name = "payload",
            columnDefinition = "jsonb",
            nullable = false)
    private ObjectNode payload;
    @Column(nullable = false)
    private int sequence;
    @Column(nullable = false)
    private Instant created;

    protected InboxEntity() {
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

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}
