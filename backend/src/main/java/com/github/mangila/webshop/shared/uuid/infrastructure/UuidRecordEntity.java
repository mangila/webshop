package com.github.mangila.webshop.shared.uuid.infrastructure;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "uuid_record")
@Immutable
@EntityListeners(AuditingEntityListener.class)
public class UuidRecordEntity implements Persistable<UUID> {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private String intent;

    @CreatedDate
    @Column(nullable = false)
    private @Nullable Instant created;

    private volatile boolean isNew = true;

    protected UuidRecordEntity() {
    }

    private UuidRecordEntity(UUID id, String intent) {
        this.id = id;
        this.intent = intent;
    }

    public static UuidRecordEntity from(UUID id, String intent) {
        return new UuidRecordEntity(id, intent);
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public Optional<Instant> getCreated() {
        return Optional.ofNullable(created);
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
