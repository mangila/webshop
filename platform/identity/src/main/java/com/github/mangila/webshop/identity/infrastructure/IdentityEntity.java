package com.github.mangila.webshop.identity.infrastructure;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;
import org.springframework.data.domain.Persistable;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "identity_record")
@Immutable
public class IdentityEntity implements Persistable<UUID> {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    private String domain;

    @Column(nullable = false, updatable = false)
    private Instant created;

    @Transient
    private volatile boolean isNew = true;

    protected IdentityEntity() {
    }

    private IdentityEntity(UUID id, String domain, Instant created) {
        this.id = id;
        this.domain = domain;
        this.created = created;
    }

    public static IdentityEntity from(UUID id, String domain, Instant created) {
        return new IdentityEntity(id, domain, created);
    }

    public UUID getId() {
        return id;
    }

    @Transient
    @Override
    public boolean isNew() {
        return isNew;
    }

    @Transient
    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}
