package com.github.mangila.webshop.product.infrastructure.jpa;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductId;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductJpaQueryRepository implements ProductQueryRepository {

    private final ProductEntityMapper mapper;
    private final ProductEntityQueryRepository repository;

    public ProductJpaQueryRepository(ProductEntityMapper mapper,
                                     ProductEntityQueryRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public Optional<Product> findById(ProductId productId) {
        var e = repository.findById(productId.value());
        return e.map(mapper::toDomain);
    }

    @Override
    public boolean existsById(ProductId productId) {
        return repository.existsById(productId.value());
    }
}
