package com.github.mangila.webshop.product.infrastructure.jpa.command;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.cqrs.CreateProductCommand;
import com.github.mangila.webshop.product.domain.cqrs.DeleteProductCommand;
import com.github.mangila.webshop.product.domain.cqrs.UpdateProductStatusCommand;
import com.github.mangila.webshop.product.domain.types.ProductStatusType;
import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntityMapper;
import com.github.mangila.webshop.shared.Ensure;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

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
        return entityMapper.toEntity()
                .andThen(repository::save)
                .andThen(entityMapper::toDomain)
                .apply(command);
    }

    @Override
    public Optional<Product> delete(DeleteProductCommand command) {
        Ensure.notNull(command, DeleteProductCommand.class);
        return repository.findById(command.id().value())
                .map(entity -> {
                    entity.setUpdated(Instant.now());
                    repository.delete(entity);
                    return entityMapper.toDomain(entity);
                });
    }

    @Override
    public Optional<Product> updateStatus(UpdateProductStatusCommand command) {
        Ensure.notNull(command, UpdateProductStatusCommand.class);
        UUID id = command.id().value();
        ProductStatusType status = command.status();
        return repository.findById(id)
                .map(entity -> {
                    Ensure.notEquals(command.status(), entity.getStatus(), "Product status is already: %s".formatted(command.status()));
                    return entity;
                })
                .map(entity -> {
                    Instant updated = Instant.now();
                    repository.updateStatus(id, status, updated);
                    entity.setStatus(status);
                    entity.setUpdated(updated);
                    return entityMapper.toDomain(entity);
                });
    }
}
