package com.github.mangila.webshop.product;

import com.github.mangila.webshop.common.util.JsonMapper;
import com.github.mangila.webshop.product.model.ProductCommand;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.Objects;

@Component
public class ProductMapper {

    private final JsonMapper jsonMapper;
    private final BeanPropertyRowMapper<ProductEntity> rowMapper;

    public ProductMapper(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
        this.rowMapper = new BeanPropertyRowMapper<>(ProductEntity.class);
    }

    @NotNull
    public Product toProduct(@NotNull ProductEntity entity) {
        String imageUrl = Objects.requireNonNullElse(entity.getImageUrl(), "");
        return new Product(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getPrice(),
            URI.create(imageUrl),
            entity.getCategory(),
            entity.getCreated().toInstant(),
            entity.getUpdated().toInstant(),
            jsonMapper.toJsonNode(entity.getExtensions())
        );
    }

    @NotNull
    public ProductEntity toEntity(@NotNull ProductCommand command) {
        var entity = new ProductEntity();
        entity.setId(command.id());
        entity.setName(command.name());
        entity.setDescription(command.description());
        entity.setPrice(command.price());
        entity.setImageUrl(command.imageUrl());
        entity.setCategory(command.category());
        entity.setExtensions(command.extensions());
        return entity;
    }

    public BeanPropertyRowMapper<ProductEntity> getRowMapper() {
        return rowMapper;
    }

}
