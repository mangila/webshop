package com.github.mangila.webshop.inventory.model;

import com.github.mangila.webshop.shared.model.BaseEntity;

import java.math.BigInteger;

public class InventoryEntity extends BaseEntity {
    private String id;
    private String productId;
    private BigInteger quantity;
}