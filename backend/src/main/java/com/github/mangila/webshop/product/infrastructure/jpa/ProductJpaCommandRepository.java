package com.github.mangila.webshop.product.infrastructure.jpa;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.ProductId;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsert;
import io.vavr.collection.Stream;
import org.springframework.stereotype.Repository;

@Repository
public class ProductJpaCommandRepository implements ProductCommandRepository {

    private final ProductEntityMapper mapper;
    private final ProductEntityCommandRepository repository;

    public ProductJpaCommandRepository(ProductEntityMapper mapper,
                                       ProductEntityCommandRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public Product insert(ProductInsert command) {
        return Stream.of(command)
                .map(mapper::toEntity)
                .map(repository::save)
                .map(mapper::toDomain)
                .get();
    }

    @Override
    public void deleteById(ProductId productId) {
        repository.deleteById(productId.value());
    }
}
