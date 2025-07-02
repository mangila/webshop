package com.github.mangila.webshop.backend.product.domain.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.common.model.ApplicationUuid;
import com.github.mangila.webshop.backend.common.util.JsonMapper;
import com.github.mangila.webshop.backend.product.domain.command.ProductInsertCommand;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private ApplicationUuid id;

    @Embedded
    @AttributeOverride(name = "value",
            column = @Column(
                    name = "name",
                    nullable = false))
    private ProductName name;

    @Embedded
    @AttributeOverride(name = "value",
            column = @Column(
                    name = "price",
                    nullable = false))
    private ProductPrice price;

    @CreatedDate
    private Instant created;

    @LastModifiedDate
    private Instant updated;

    @Type(JsonBinaryType.class)
    @Column(name = "attributes",
            columnDefinition = "jsonb",
            nullable = false)
    private JsonNode attributes;

    protected Product() {
    }

    public Product(ProductName name, ProductPrice price, JsonNode attributes) {
        this(new ApplicationUuid(), name, price, null, null, attributes);
    }

    private Product(ApplicationUuid id, ProductName name, ProductPrice price, Instant created, Instant updated, JsonNode attributes) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.created = created;
        this.updated = updated;
        this.attributes = attributes;
    }

    public static Product from(ProductInsertCommand command) {
        return new Product(command.name(), command.price(), command.attributes());
    }

    public JsonNode toJsonNode(JsonMapper jsonMapper) {
        return jsonMapper.toJsonNode(this);
    }

    public ApplicationUuid id() {
        return id;
    }

    public ProductName name() {
        return name;
    }

    public ProductPrice price() {
        return price;
    }

    public Instant created() {
        return created;
    }

    public Instant updated() {
        return updated;
    }

    public JsonNode attributes() {
        return attributes;
    }
}

