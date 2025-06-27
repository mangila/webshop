package com.github.mangila.webshop.backend.product.command.model;

import com.github.mangila.webshop.backend.common.annotation.Json;
import com.github.mangila.webshop.backend.product.util.annotation.ProductId;
import com.github.mangila.webshop.backend.product.util.annotation.ProductName;
import com.github.mangila.webshop.backend.product.util.annotation.ProductPrice;

import java.math.BigDecimal;

public record ProductUpsertCommand(
        @ProductId
        String id,
        @ProductName
        String name,
        @ProductPrice
        BigDecimal price,
        @Json
        String attributes
) {
}


