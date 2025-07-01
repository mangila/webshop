package com.github.mangila.webshop.backend.common.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

@Embeddable
@NullMarked
public class ApplicationUuid {

    @NotNull
    private UUID value;

    public ApplicationUuid() {
        this(UUID.randomUUID());
    }

    public ApplicationUuid(@NotNull UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }

    public void setValue(UUID value) {
        this.value = value;
    }
}

