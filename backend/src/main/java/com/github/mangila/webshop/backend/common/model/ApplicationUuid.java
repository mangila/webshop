package com.github.mangila.webshop.backend.common.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

@Embeddable
@NullMarked
public record ApplicationUuid(@NotNull UUID value) {

    public ApplicationUuid() {
        this(UUID.randomUUID());
    }
}

