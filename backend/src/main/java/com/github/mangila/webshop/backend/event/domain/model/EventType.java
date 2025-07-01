package com.github.mangila.webshop.backend.event.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NullMarked;

@Embeddable
@NullMarked
public record EventType(@NotNull String value) {
}
