package com.github.mangila.webshop.product.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductEntityCommandRepository extends JpaRepository<ProductEntity, UUID> {
}
