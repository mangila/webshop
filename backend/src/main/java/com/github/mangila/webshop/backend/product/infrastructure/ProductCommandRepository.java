package com.github.mangila.webshop.backend.product.infrastructure;

import com.github.mangila.webshop.backend.common.model.ApplicationUuid;
import com.github.mangila.webshop.backend.product.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCommandRepository extends JpaRepository<Product, ApplicationUuid> {
}
