package com.github.mangila.webshop.product.infrastructure.jpa;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductId;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import com.github.mangila.webshop.shared.domain.common.CqrsOperation;
import com.github.mangila.webshop.shared.domain.exception.CqrsException;
import com.github.mangila.webshop.shared.infrastructure.spring.annotation.ObservedRepository;
import io.micrometer.observation.annotation.Observed;

@ObservedRepository
public class ProductJpaQueryRepository implements ProductQueryRepository {

    private final ProductEntityMapper mapper;
    private final ProductEntityQueryRepository repository;

    public ProductJpaQueryRepository(ProductEntityMapper mapper,
                                     ProductEntityQueryRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Observed
    @Override
    public Product findById(ProductId productId) {
        return repository.findById(productId.value())
                .map(mapper::toDomain)
                .orElseThrow(() -> new CqrsException(
                        String.format("id not found: '%s'", productId.value()),
                        CqrsOperation.QUERY,
                        Product.class
                ));
    }

    @Observed
    @Override
    public boolean existsById(ProductId productId) {
        return repository.existsById(productId.value());
    }
}
