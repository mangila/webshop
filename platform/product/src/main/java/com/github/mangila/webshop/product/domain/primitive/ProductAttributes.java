package com.github.mangila.webshop.product.domain.primitive;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.shared.Ensure;

public record ProductAttributes(ObjectNode value) {
    public ProductAttributes {
        Ensure.notNull(value, "Product attributes must not be null");
    }
}
