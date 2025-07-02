package com.github.mangila.webshop.backend.product.domain.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NullMarked;

import java.math.BigDecimal;

@Embeddable
@NullMarked
public record ProductPrice(
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
        @DecimalMax(value = "99999.99", message = "Price must not exceed 99,999.99")
        @Digits(integer = 5, fraction = 2, message = "Price format is invalid (up to 5 digits and 2 decimal places)")
        BigDecimal value) {
}
