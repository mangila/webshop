package com.github.mangila.webshop.product.application.web.request;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.product.domain.types.ProductUnit;

public record ProductInsertRequest(
        String name,
        ObjectNode attributes,
        ProductUnit unit
) {
}
