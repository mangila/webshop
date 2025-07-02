package com.github.mangila.webshop.backend.inventory.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Embeddable
public record InventoryId(@NotNull UUID value) {
}
