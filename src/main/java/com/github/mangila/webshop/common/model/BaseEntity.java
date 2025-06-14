package com.github.mangila.webshop.common.model;

import java.time.Instant;

public abstract class BaseEntity {

    private Instant updated;
    private String extensions;

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }

    public String getExtensions() {
        return extensions;
    }

    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }
}
