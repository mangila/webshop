package com.github.mangila.webshop.shared.identity.domain;

import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class DomainId {

    private UUID id;
    private String domain;
    private String intent;
    private @Nullable Instant created;

    private DomainId(UUID id, String domain, String intent, @Nullable Instant created) {
        this.id = id;
        this.domain = domain;
        this.intent = intent;
        this.created = created;
    }

    public static DomainId from(UUID id, String domain, String intent, Instant created) {
        return new DomainId(id, domain, intent, created);
    }

    public static DomainId create(String domain, String intent) {
        return new DomainId(UUID.randomUUID(), domain, intent, null);
    }

    public UUID getId() {
        return id;
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

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public Optional<Instant> getCreated() {
        return Optional.ofNullable(created);
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}