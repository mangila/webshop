package com.github.mangila.webshop.product.infrastructure;

import com.github.mangila.webshop.product.domain.model.Product;
import com.github.mangila.webshop.product.domain.model.ProductId;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductQueryRepository extends ListCrudRepository<Product, ProductId> {
}
