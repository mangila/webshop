package com.github.mangila.webshop.product.application.rest.request;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.shared.annotation.AlphaNumeric;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateProductRequest(
        @NotBlank
        @Size(min = 2, max = 100)
        @AlphaNumeric(
                withSpace = true,
                withHyphen = true)
        String name,
        @NotNull ObjectNode attributes
) {
}
