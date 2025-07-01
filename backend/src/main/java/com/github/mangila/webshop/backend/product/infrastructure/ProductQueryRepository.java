package com.github.mangila.webshop.backend.product.infrastructure;

import com.github.mangila.webshop.backend.common.model.ApplicationUuid;
import com.github.mangila.webshop.backend.product.domain.model.Product;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductQueryRepository extends ListCrudRepository<Product, ApplicationUuid> {
}
