package com.github.mangila.webshop.backend.product.domain.model;

import com.github.mangila.webshop.backend.common.annotation.AlphaNumeric;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.NullMarked;

@Embeddable
@NullMarked
public record ProductName(
        @NotNull(message = "Product name must not be null")
        @AlphaNumeric(allowNull = false, message = "Product name must be alphanumeric")
        @Size(min = 1, max = 255, message = "Product name must be between 1 and 255 characters")
        String value
) {
}
