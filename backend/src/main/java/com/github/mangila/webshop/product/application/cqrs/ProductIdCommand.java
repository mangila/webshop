package com.github.mangila.webshop.product.application.cqrs;

import com.github.mangila.webshop.shared.infrastructure.spring.validation.DomainId;

import java.util.UUID;

public record ProductIdCommand(@DomainId(message = "Invalid Product ID") UUID value) {
}
