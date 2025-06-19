package com.github.mangila.webshop.product.util;

import com.github.mangila.webshop.common.util.JsonMapper;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductEntity;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Component
public class ProductRepositoryUtil {

    private final JsonMapper jsonMapper;
    private final DataClassRowMapper<ProductEntity> productEntityRowMapper;

    public ProductRepositoryUtil(JsonMapper jsonMapper,
                                 DataClassRowMapper<ProductEntity> productEntityRowMapper) {
        this.jsonMapper = jsonMapper;
        this.productEntityRowMapper = productEntityRowMapper;
    }

    public DataClassRowMapper<ProductEntity> productEntityRowMapper() {
        return productEntityRowMapper;
    }

    public Optional<Product> extractOneResult(List<ProductEntity> result) {
        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }
        ProductEntity entity = result.getFirst();
        Product product = Product.from(entity, jsonMapper);
        return Optional.of(product);
    }
}
