package com.github.mangila.webshop.backend.product.domain.command;

import com.github.mangila.webshop.backend.common.annotation.Json;
import com.github.mangila.webshop.backend.product.domain.util.ProductId;
import com.github.mangila.webshop.backend.product.domain.util.ProductName;
import com.github.mangila.webshop.backend.product.domain.util.ProductPrice;

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


