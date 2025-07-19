package com.github.mangila.webshop.identity.domain;

import com.github.mangila.webshop.shared.model.Domain;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class DomainId {

    private UUID id;
    private Domain domain;
    private Instant created;

    private DomainId(UUID id, Domain domain, Instant created) {
        this.id = id;
        this.domain = domain;
        this.created = created;
    }

    public static DomainId from(UUID id, Domain domain, Instant created) {
        return new DomainId(id, domain, created);
    }

    public static DomainId create(Domain domain) {
        return new DomainId(UUID.randomUUID(), domain, null);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}