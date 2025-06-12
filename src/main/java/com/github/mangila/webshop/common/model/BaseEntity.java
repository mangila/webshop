package com.github.mangila.webshop.common.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Instant;

public abstract class BaseEntity {

    private Instant updated;
    private JsonNode extensions;

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public JsonNode getExtensions() {
        return extensions;
    }

    public void setExtensions(JsonNode extensions) {
        this.extensions = extensions;
    }
}
