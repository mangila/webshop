package com.github.mangila.webshop.product.infrastructure.jpa.command;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.cqrs.CreateProductCommand;
import com.github.mangila.webshop.product.domain.cqrs.DeleteProductCommand;
import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntity;
import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntityMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public class ProductJpaCommandRepository implements ProductCommandRepository {

    private static final Logger log = LoggerFactory.getLogger(ProductJpaCommandRepository.class);

    private final ProductEntityCommandRepository repository;
    private final ProductEntityMapper entityMapper;

    public ProductJpaCommandRepository(ProductEntityCommandRepository repository,
                                       ProductEntityMapper entityMapper) {
        this.repository = repository;
        this.entityMapper = entityMapper;
    }

    @Override
    public Product create(CreateProductCommand command) {
        ProductEntity mappedEntity = entityMapper.toEntity(command);
        ProductEntity savedEntity = repository.save(mappedEntity);
        return entityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> delete(DeleteProductCommand command) {
        Optional<ProductEntity> optional = repository.findById(command.id().value());
        if (optional.isPresent()) {
            ProductEntity entity = optional.get();
            repository.delete(entity);
            entity.setUpdated(Instant.now());
            return Optional.of(entityMapper.toDomain(entity));
        } else {
            log.error("Product not found with id {}", command.id().value());
            return Optional.empty();
        }
    }
}
