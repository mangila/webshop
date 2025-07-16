package com.github.mangila.webshop.shared.identity.domain;

import com.github.mangila.webshop.shared.application.registry.Domain;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class DomainId {

    private UUID id;
    private Domain domain;
    private String intent;
    private @Nullable Instant created;

    private DomainId(UUID id, Domain domain, String intent, @Nullable Instant created) {
        this.id = id;
        this.domain = domain;
        this.intent = intent;
        this.created = created;
    }

    public static DomainId from(UUID id, Domain domain, String intent, Instant created) {
        return new DomainId(id, domain, intent, created);
    }

    public static DomainId create(@NotNull Domain domain, String intent) {
        return new DomainId(UUID.randomUUID(), domain, intent, null);
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