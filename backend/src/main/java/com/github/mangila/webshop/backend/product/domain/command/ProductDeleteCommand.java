package com.github.mangila.webshop.backend.product.domain.command;

import com.github.mangila.webshop.backend.common.model.ApplicationUuid;

public record ProductDeleteCommand(ApplicationUuid id) {
}
