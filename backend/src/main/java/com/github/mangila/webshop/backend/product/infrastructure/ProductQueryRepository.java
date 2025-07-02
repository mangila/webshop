package com.github.mangila.webshop.backend.product.infrastructure;

import com.github.mangila.webshop.backend.product.domain.model.Product;
import com.github.mangila.webshop.backend.product.domain.model.ProductId;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductQueryRepository extends ListCrudRepository<Product, ProductId> {
}
