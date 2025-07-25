package com.github.mangila.webshop.outbox.infrastructure.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "outbox_sequence")
public class OutboxSequenceEntity {

    @Id
    @Column(name = "aggregate_id", columnDefinition = "uuid")
    private UUID aggregateId;

    @Column(name = "current_sequence")
    private int currentSequence;

    protected OutboxSequenceEntity() {
    }

    public OutboxSequenceEntity(UUID aggregateId, int currentSequence) {
        this.aggregateId = aggregateId;
        this.currentSequence = currentSequence;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    public int getCurrentSequence() {
        return currentSequence;
    }

    public void setCurrentSequence(int currentSequence) {
        this.currentSequence = currentSequence;
    }
}
