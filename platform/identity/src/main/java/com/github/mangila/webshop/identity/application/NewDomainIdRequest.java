package com.github.mangila.webshop.identity.application;

import com.github.mangila.webshop.shared.registry.model.Domain;
import com.github.mangila.webshop.shared.registry.validation.RegisteredDomain;
import jakarta.validation.constraints.NotNull;

public record NewDomainIdRequest(@NotNull @RegisteredDomain Domain domain) {
}