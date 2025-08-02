package com.github.mangila.webshop.product.domain.primitive;

import com.github.mangila.webshop.shared.model.Sku;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public record ProductVariants(List<ProductVariant> variants) {
    public Map<Sku, ProductVariant> asMap() {
        return variants.stream().collect(Collectors.toMap(
                ProductVariant::sku,
                Function.identity()));
    }
}
