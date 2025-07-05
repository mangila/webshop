package com.github.mangila.webshop.product.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Embeddable
public record ProductId(@NotNull UUID value) {

    public static ProductId from(UUID productId) {
        return new ProductId(productId);
    }
}
