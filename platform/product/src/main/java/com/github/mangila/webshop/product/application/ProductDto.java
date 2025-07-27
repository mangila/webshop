package com.github.mangila.webshop.product.application;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.identity.application.validation.GeneratedIdentity;
import com.github.mangila.webshop.product.domain.types.ProductUnitType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ProductDto(
        @NotNull @GeneratedIdentity UUID id,
        @NotBlank String name,
        @NotNull ObjectNode attributes,
        @NotNull ProductUnitType unit,
        @NotNull Instant created,
        @NotNull Instant updated) {
}
