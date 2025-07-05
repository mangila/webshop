package com.github.mangila.webshop.product.domain.model;

import com.github.mangila.webshop.shared.application.validation.AlphaNumeric;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Embeddable
public record ProductName(
        @NotNull(message = "Product name must not be null")
        @AlphaNumeric(allowNull = false, message = "Product name must be alphanumeric")
        @Size(min = 1, max = 255, message = "Product name must be between 1 and 255 characters")
        String value
) {
}
