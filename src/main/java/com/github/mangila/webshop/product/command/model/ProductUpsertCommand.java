package com.github.mangila.webshop.product.command.model;

import com.github.mangila.webshop.common.util.annotation.Json;
import com.github.mangila.webshop.product.util.ProductId;
import com.github.mangila.webshop.product.util.ProductName;
import com.github.mangila.webshop.product.util.ProductPrice;

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


