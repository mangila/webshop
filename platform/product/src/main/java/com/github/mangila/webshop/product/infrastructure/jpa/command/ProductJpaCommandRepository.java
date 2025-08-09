package com.github.mangila.webshop.product.infrastructure.jpa.command;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.cqrs.CreateProductCommand;
import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntityMapper;
import com.github.mangila.webshop.shared.Ensure;
import io.vavr.collection.Stream;
import org.springframework.stereotype.Repository;

@Repository
public class ProductJpaCommandRepository implements ProductCommandRepository {
    private final ProductEntityCommandRepository repository;
    private final ProductEntityMapper entityMapper;

    public ProductJpaCommandRepository(ProductEntityCommandRepository repository,
                                       ProductEntityMapper entityMapper) {
        this.repository = repository;
        this.entityMapper = entityMapper;
    }

    @Override
    public Product create(CreateProductCommand command) {
        Ensure.notNull(command, CreateProductCommand.class);
        return Stream.of(entityMapper.toEntity(command))
                .map(repository::save)
                .map(entityMapper::toDomain)
                .get();
    }

    @Override
    public Product delete(Product product) {
        Ensure.notNull(product, Product.class);
        return Stream.of(entityMapper.toEntity(product))
                .map(productEntity -> {
                    repository.delete(productEntity);
                    return productEntity;
                })
                .map(entityMapper::toDomain)
                .get();
    }

    @Override
    public Product updateStatus(Product product) {
        Ensure.notNull(product, Product.class);
        repository.updateStatus(product.id().value(), product.status(), product.updated().value());
        return product;
    }
}
