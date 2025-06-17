package com.github.mangila.webshop.product.model;

import java.math.BigDecimal;

public record ProductDto(
        String id,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        String category,
        String created,
        String updated,
        String extensions
) {
}

