package com.github.mangila.webshop.product.application.web.request;


import com.github.mangila.webshop.identity.application.validation.DomainId;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductByIdRequest(@NotNull @DomainId UUID value) {
}
