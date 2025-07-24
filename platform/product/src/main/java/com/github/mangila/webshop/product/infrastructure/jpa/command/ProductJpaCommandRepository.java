package com.github.mangila.webshop.product.infrastructure.jpa.command;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntity;
import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntityMapper;
import com.github.mangila.webshop.shared.annotation.ObservedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@ObservedRepository
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
    public Product insert(ProductInsertCommand command) {
        ProductEntity entity = repository.save(entityMapper.toEntity(command));
        return entityMapper.toDomain(entity);
    }

    @Override
    public boolean deleteById(ProductId productId) {
        Optional<ProductEntity> optional = repository.findById(productId.value());
        if (optional.isPresent()) {
            ProductEntity entity = optional.get();
            repository.delete(entity);
            return true;
        } else {
            log.error("Product not found with id {}", productId.value());
            return false;
        }
    }
}
