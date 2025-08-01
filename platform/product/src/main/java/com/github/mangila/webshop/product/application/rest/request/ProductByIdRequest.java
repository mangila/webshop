package com.github.mangila.webshop.product.application.rest.request;


import com.github.mangila.webshop.identity.application.validation.GeneratedIdentity;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductByIdRequest(@NotNull @GeneratedIdentity UUID value) {
}
