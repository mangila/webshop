package com.github.mangila.webshop.product.model;

import java.math.BigDecimal;

public record ProductMutate(
        String id,
        String name,
        String description,
        BigDecimal price,
        String imageUrl,
        String category,
        String extensions
) {
}
