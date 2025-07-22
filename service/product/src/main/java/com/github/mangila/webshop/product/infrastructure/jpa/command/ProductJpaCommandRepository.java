package com.github.mangila.webshop.product.infrastructure.jpa.command;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntity;
import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntityMapper;
import io.vavr.collection.Stream;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class ProductJpaCommandRepository implements ProductCommandRepository {

    private final ProductEntityCommandRepository repository;
    private final ProductQueryRepository queryRepository;
    private final ProductEntityMapper entityMapper;

    public ProductJpaCommandRepository(ProductEntityCommandRepository repository,
                                       @Qualifier("productJpaQueryRepository") ProductQueryRepository queryRepository,
                                       ProductEntityMapper entityMapper) {
        this.repository = repository;
        this.queryRepository = queryRepository;
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
        Product product = queryRepository.findByIdOrThrow(productId);
        var entity = entityMapper.toEntity(product);
        // Entity is not managed, so we need to use the persistence flag
        entity.setNew(Boolean.FALSE);
        repository.delete(entity);
    }
}
