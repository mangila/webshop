package com.github.mangila.pokedex.webshop.shared;

import java.time.Instant;
import java.util.Map;

public abstract class BaseEntity {

    private Instant createdAt;
    private Instant updatedAt;
    private Map<String, Object> extensions;

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }
}
