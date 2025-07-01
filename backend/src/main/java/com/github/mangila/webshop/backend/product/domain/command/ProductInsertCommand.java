package com.github.mangila.webshop.backend.product.domain.command;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.common.model.ApplicationUuid;
import com.github.mangila.webshop.backend.product.domain.model.Product;
import com.github.mangila.webshop.backend.product.domain.model.ProductName;
import com.github.mangila.webshop.backend.product.domain.model.ProductPrice;

public record ProductInsertCommand(
        ProductName name,
        ProductPrice price,
        JsonNode attributes
) {

    public Product toProduct() {
        return new Product(new ApplicationUuid(), name, price, null, null, attributes);
    }
}
