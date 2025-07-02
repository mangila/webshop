package com.github.mangila.webshop.backend.product.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NullMarked;

import java.util.UUID;

@Embeddable
@NullMarked
public record ProductId(@NotNull UUID value) {
}
