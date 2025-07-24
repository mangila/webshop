package com.github.mangila.webshop.product.infrastructure.jpa.query;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntityMapper;
import com.github.mangila.webshop.shared.annotation.ObservedRepository;

import java.util.Optional;

@ObservedRepository
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
        return repository.findById(productId.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsById(ProductId productId) {
        return repository.existsById(productId.value());
    }
}
