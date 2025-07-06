package com.github.mangila.webshop.product.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.shared.infrastructure.json.JsonMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class Product {

    private ProductId id;
    private ProductName name;
    private ProductPrice price;
    private JsonNode attributes;
    private ProductUnit unit;
    private Instant created;
    private Instant updated;

    private Product(ProductId id, ProductName name, ProductPrice price, JsonNode attributes, ProductUnit unit, Instant created, Instant updated) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.attributes = attributes;
        this.unit = unit;
        this.created = created;
        this.updated = updated;
    }

    public static Product from(UUID id, String name, BigDecimal price, JsonNode attributes, ProductUnit unit, Instant created, Instant updated) {
        return new Product(new ProductId(id), new ProductName(name), new ProductPrice(price), attributes, unit, created, updated);
    }

    public JsonNode toJsonNode(JsonMapper jsonMapper) {
        return jsonMapper.toJsonNode(this);
    }

    public ProductId getId() {
        return id;
    }

    public void setId(ProductId id) {
        this.id = id;
    }

    public ProductName getName() {
        return name;
    }

    public void setName(ProductName name) {
        this.name = name;
    }

    public ProductPrice getPrice() {
        return price;
    }

    public void setPrice(ProductPrice price) {
        this.price = price;
    }

    public JsonNode getAttributes() {
        return attributes;
    }

    public void setAttributes(JsonNode attributes) {
        this.attributes = attributes;
    }

    public ProductUnit getUnit() {
        return unit;
    }

    public void setUnit(ProductUnit unit) {
        this.unit = unit;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getUpdated() {
        return updated;
    }

    public void setUpdated(Instant updated) {
        this.updated = updated;
    }
}

