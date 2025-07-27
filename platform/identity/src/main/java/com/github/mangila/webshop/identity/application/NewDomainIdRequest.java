package com.github.mangila.webshop.identity.application;

import com.github.mangila.webshop.shared.annotation.RegisteredDomain;
import com.github.mangila.webshop.shared.model.Domain;
import jakarta.validation.constraints.NotNull;

public record NewDomainIdRequest(@NotNull @RegisteredDomain Domain domain) {
}