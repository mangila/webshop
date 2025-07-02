package com.github.mangila.webshop.backend.inventory.domain.model;

import com.github.mangila.webshop.backend.inventory.domain.command.InventoryInsertCommand;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "inventory")
@EntityListeners(AuditingEntityListener.class)
public class Inventory {

    @EmbeddedId
    @AttributeOverride(
            name = "value",
            column = @Column(name = "product_id", nullable = false))
    private InventoryId id;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "quantity", nullable = false))
    private InventoryQuantity quantity;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant created;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updated;

    protected Inventory() {
    }

    private Inventory(InventoryId id, InventoryQuantity quantity, Instant created, Instant updated) {
        this.id = id;
        this.quantity = quantity;
        this.created = created;
        this.updated = updated;
    }

    private Inventory(InventoryId id, InventoryQuantity quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public static Inventory from(InventoryInsertCommand command) {
        return new Inventory(command.id(), command.quantity());
    }

    public InventoryId getId() {
        return id;
    }

    public void setId(InventoryId id) {
        this.id = id;
    }

    public InventoryQuantity getQuantity() {
        return quantity;
    }

    public void setQuantity(InventoryQuantity quantity) {
        this.quantity = quantity;
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