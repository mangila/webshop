package com.github.mangila.webshop.product.application.cqrs;

import com.github.mangila.webshop.shared.infrastructure.spring.validation.DomainId;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductIdQuery(@NotNull @DomainId(message = "Not a valid Product ID") UUID value) {
}
