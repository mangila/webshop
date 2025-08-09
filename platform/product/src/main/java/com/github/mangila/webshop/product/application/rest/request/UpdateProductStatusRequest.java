package com.github.mangila.webshop.product.application.rest.request;

import com.github.mangila.webshop.product.domain.types.ProductStatusType;
import com.github.mangila.webshop.shared.identity.application.validation.GeneratedIdentity;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateProductStatusRequest(
        @NotNull @GeneratedIdentity UUID value,
        @NotNull ProductStatusType status
) {
}
