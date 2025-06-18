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
        var product = new Product();
        product.setId(entity.getId());
        product.setName(entity.getName());
        product.setDescription(entity.getDescription());
        product.setPrice(entity.getPrice());
        String imageUrl = Objects.requireNonNullElse(entity.getImageUrl(), "");
        product.setImageUrl(URI.create(imageUrl));
        product.setCategory(entity.getCategory());
        product.setCreated(entity.getCreated().toInstant());
        product.setUpdated(entity.getUpdated().toInstant());
        product.setExtensions(jsonMapper.toJsonNode(entity.getExtensions()));
        return product;
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
