package com.github.mangila.webshop.product;

import com.github.mangila.webshop.common.util.JsonMapper;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductCommand;
import com.github.mangila.webshop.product.model.ProductEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    private final JsonMapper jsonMapper;
    private final DataClassRowMapper<ProductEntity> rowMapper;

    public ProductMapper(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
        this.rowMapper = new DataClassRowMapper<>(ProductEntity.class);
    }

    @NotNull
    public Product toProduct(@NotNull ProductEntity entity) {
        return new Product(
                entity.id(),
                entity.name(),
                entity.price(),
                entity.created().toInstant(),
                entity.updated().toInstant(),
                jsonMapper.toJsonNode(entity.attributes())
        );
    }

    @NotNull
    public ProductEntity toEntity(@NotNull ProductCommand command) {
        return new ProductEntity(
                command.id(),
                command.name(),
                command.price(),
                null,
                null,
                command.attributes()
        );
    }

    public DataClassRowMapper<ProductEntity> getRowMapper() {
        return rowMapper;
    }

}
