package com.github.mangila.webshop.common.model;

import java.time.Instant;
import java.util.Map;

public abstract class BaseEntity {

    private Instant updated;
    private Map<String, Object> extensions;

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }
}
