package com.github.mangila.pokedex.webshop.inventory.model;

import com.github.mangila.pokedex.webshop.shared.model.BaseEntity;

import java.math.BigInteger;

public class InventoryEntity extends BaseEntity {
    private String id;
    private String productId;
    private BigInteger quantity;
}