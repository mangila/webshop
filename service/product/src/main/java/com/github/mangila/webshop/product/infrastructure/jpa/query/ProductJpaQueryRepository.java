package com.github.mangila.webshop.product.infrastructure.jpa.query;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntityMapper;
import com.github.mangila.webshop.shared.exception.CqrsException;
import com.github.mangila.webshop.shared.model.CqrsOperation;
import org.springframework.stereotype.Repository;

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
    public Product findByIdOrThrow(ProductId productId) {
        return repository.findById(productId.value())
                .map(mapper::toDomain)
                .orElseThrow(() -> new CqrsException(
                        String.format("id not found: '%s'", productId.value()),
                        CqrsOperation.QUERY,
                        Product.class
                ));
    }

    @Override
    public boolean existsById(ProductId productId) {
        return repository.existsById(productId.value());
    }
}
