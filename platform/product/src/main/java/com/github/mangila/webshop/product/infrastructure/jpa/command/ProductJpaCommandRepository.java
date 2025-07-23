package com.github.mangila.webshop.product.infrastructure.jpa.command;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntity;
import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntityMapper;
import com.github.mangila.webshop.shared.exception.CqrsException;
import com.github.mangila.webshop.shared.model.CqrsOperation;
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
    public Product insert(ProductInsertCommand command) {
        ProductEntity entity = entityMapper.toEntity(command);
        Product product = Stream.of(entity)
                .map(repository::save)
                .map(entityMapper::toDomain)
                .get();
        return product;
    }

    @Override
    public void deleteByIdOrThrow(ProductId productId) {
        repository.findById(productId.value())
                .ifPresentOrElse(repository::delete, () -> {
                    throw new CqrsException(String.format("id not found: '%s'", productId.value()),
                            CqrsOperation.COMMAND,
                            Product.class);
                });
    }
}
