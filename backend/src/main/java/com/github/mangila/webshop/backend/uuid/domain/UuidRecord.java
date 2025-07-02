package com.github.mangila.webshop.backend.uuid.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "uuid_record")
@Immutable
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

    public void setId(UUID id) {
        this.id = id;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }
}