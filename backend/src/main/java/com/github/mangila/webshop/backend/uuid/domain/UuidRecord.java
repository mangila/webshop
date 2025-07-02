package com.github.mangila.webshop.backend.uuid.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "uuid_record")
@EntityListeners(AuditingEntityListener.class)
public class UuidRecord {

    @Id
    @Column(nullable = false, columnDefinition = "uuid")
    private UUID id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant created;

    @Column(nullable = false)
    private String intent;

    protected UuidRecord() {
    }

    public UuidRecord(String intent) {
        this.id = UUID.randomUUID();
        this.intent = intent;
    }

    public UUID getId() {
        return id;
    }

    public Instant getCreated() {
        return created;
    }

    public String getIntent() {
        return intent;
    }
}