package com.github.mangila.webshop.identity.infrastructure;

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
@Table(name = "domain_id")
@Immutable
@EntityListeners(AuditingEntityListener.class)
public class DomainIdEntity implements Persistable<UUID> {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private String domain;

    @CreatedDate
    @Column(nullable = false)
    private @Nullable Instant created;

    @Transient
    private volatile boolean isNew = true;

    protected DomainIdEntity() {
    }

    private DomainIdEntity(UUID id, String domain) {
        this.id = id;
        this.domain = domain;
    }

    public static DomainIdEntity from(UUID id, String domain) {
        return new DomainIdEntity(id, domain);
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domainKey) {
        this.domain = domainKey;
    }
}
