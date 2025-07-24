package com.github.mangila.webshop.product.application.web.request;


import com.github.mangila.webshop.shared.validation.DomainId;

import java.util.UUID;

public record ProductByIdRequest(@DomainId UUID value) {
}
