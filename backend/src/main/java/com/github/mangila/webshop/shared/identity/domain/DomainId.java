package com.github.mangila.webshop.shared.identity.domain;

import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class DomainId {

    private UUID id;
    private String domainKey;
    private String intent;
    private @Nullable Instant created;

    private DomainId(UUID id, String domainKey, String intent, @Nullable Instant created) {
        this.id = id;
        this.domainKey = domainKey;
        this.intent = intent;
        this.created = created;
    }

    public static DomainId from(UUID id, String domainKey, String intent, Instant created) {
        return new DomainId(id, domainKey, intent, created);
    }

    public static DomainId create(String domainKey, String intent) {
        return new DomainId(UUID.randomUUID(), domainKey, intent, null);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDomainKey() {
        return domainKey;
    }

    public void setDomainKey(String domainKey) {
        this.domainKey = domainKey;
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