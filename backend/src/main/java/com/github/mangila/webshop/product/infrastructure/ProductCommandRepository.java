package com.github.mangila.webshop.product.infrastructure;

import com.github.mangila.webshop.product.domain.model.Product;
import com.github.mangila.webshop.product.domain.model.ProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCommandRepository extends JpaRepository<Product, ProductId> {
}
