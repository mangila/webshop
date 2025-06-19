package com.github.mangila.webshop.product.model;

import com.github.mangila.webshop.common.util.annotation.AlphaNumeric;
import com.github.mangila.webshop.common.util.annotation.Json;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductCommand(
        @NotNull(message = "Command type must not be null")
        ProductCommandType type,
        @NotNull(message = "id must not be null")
        @AlphaNumeric(allowNull = false)
        @Size(min = 1, max = 36)
        String id,
        @NotNull(message = "name must not be null")
        @AlphaNumeric(allowNull = false)
        @Size(min = 1, max = 255)
        String name,
        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
        @DecimalMax(value = "99999.99", message = "Price must not exceed 99,999.99")
        @Digits(integer = 7, fraction = 2, message = "Price format is invalid (up to 7 digits and 2 decimal places)")
        BigDecimal price,
        @Json
        String attributes
) {
    public static final ProductCommand EMPTY = new ProductCommand(null, null, null, null, null);

    public boolean isEmpty() {
        return this.equals(EMPTY);
    }
}
