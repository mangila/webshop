package com.github.mangila.webshop.backend.inventory.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NullMarked;

import java.math.BigDecimal;

@Embeddable
@NullMarked
public record InventoryQuantity(
        @NotNull(message = "Quantity is required")
        @DecimalMin(value = "0.00", message = "Quantity must be at least 0.00")
        @DecimalMax(value = "99999.99", message = "Quantity must not exceed 99,999.99")
        @Digits(integer = 5, fraction = 2, message = "Quantity format is invalid (up to 5 digits and 2 decimal places)")
        BigDecimal value) {
    public static final InventoryQuantity DEFAULT = new InventoryQuantity(BigDecimal.ZERO);
}

