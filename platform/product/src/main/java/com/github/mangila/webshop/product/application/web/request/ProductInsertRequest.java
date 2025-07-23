package com.github.mangila.webshop.product.application.web.request;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.product.domain.types.ProductUnit;
import com.github.mangila.webshop.shared.validation.AlphaNumeric;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductInsertRequest(
        @NotBlank
        @Size(min = 2, max = 100)
        @AlphaNumeric(
                withSpace = true,
                withHyphen = true)
        String name,
        @NotNull ObjectNode attributes,
        @NotNull ProductUnit unit
) {
}
