package com.github.mangila.webshop.product.domain.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.shared.application.json.JsonMapper;
import com.github.mangila.webshop.product.domain.command.ProductInsertCommand;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @EmbeddedId
    @AttributeOverride(
            name = "value",
            column = @Column(name = "id", nullable = false))
    private ProductId id;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "name", nullable = false))
    private ProductName name;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "price", nullable = false))
    private ProductPrice price;

    @Type(JsonBinaryType.class)
    @Column(name = "attributes", columnDefinition = "jsonb", nullable = false)
    private JsonNode attributes;

    @Enumerated(EnumType.STRING)
    @Column(name = "unit", nullable = false)
    private ProductUnit unit;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant created;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updated;

    protected Product() {
    }

    private Product(ProductId id,
                    ProductName name,
                    ProductPrice price,
                    JsonNode attributes,
                    ProductUnit unit,
                    Instant created,
                    Instant updated) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.attributes = attributes;
        this.unit = unit;
        this.created = created;
        this.updated = updated;
    }

    private Product(ProductId id,
                    ProductName name,
                    ProductPrice price,
                    JsonNode attributes,
                    ProductUnit unit) {
        this(id, name, price, attributes, unit, null, null);
    }

    public static Product from(UUID uuid, ProductInsertCommand command) {
        return new Product(
                new ProductId(uuid),
                command.name(),
                command.price(),
                command.attributes(),
                command.unit()
        );
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

