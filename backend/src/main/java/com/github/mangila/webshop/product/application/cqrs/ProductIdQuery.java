package com.github.mangila.webshop.product.application.cqrs;

import com.github.mangila.webshop.shared.infrastructure.spring.validation.DomainId;

import java.util.UUID;

public record ProductIdQuery(@DomainId(message = "Invalid Product ID") UUID value) {
}
