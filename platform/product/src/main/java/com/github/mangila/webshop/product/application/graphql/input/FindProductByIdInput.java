package com.github.mangila.webshop.product.application.graphql.input;

import com.github.mangila.webshop.shared.identity.application.validation.GeneratedIdentity;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FindProductByIdInput(@NotNull @GeneratedIdentity UUID value) {
}
