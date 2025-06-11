package com.github.mangila.pokedex.webshop.shared;

import java.time.Instant;
import java.util.Map;

public abstract class BaseEntity {

    private Instant createdAt;
    private Map<String, Object> extensions;

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }
}
