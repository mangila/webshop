package com.github.mangila.webshop.product.application.cqrs;

import com.github.mangila.webshop.product.application.validation.ProductId;

import java.util.UUID;

public record ProductIdQuery(@ProductId UUID value) {
}
