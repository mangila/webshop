package com.github.mangila.webshop.shared.uuid.domain;

import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class UuidRecord {

    private UUID id;
    private String intent;
    private @Nullable Instant created;

    private UuidRecord(UUID id, String intent, @Nullable Instant created) {
        this.id = id;
        this.intent = intent;
        this.created = created;
    }

    public static UuidRecord from(UUID id, String intent, Instant created) {
        return new UuidRecord(id, intent, created);
    }

    public static UuidRecord create(String intent) {
        return new UuidRecord(UUID.randomUUID(), intent, null);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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