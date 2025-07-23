package com.github.mangila.webshop.product.infrastructure.jpa.query;

import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductEntityQueryRepository extends JpaRepository<ProductEntity, UUID> {
}
