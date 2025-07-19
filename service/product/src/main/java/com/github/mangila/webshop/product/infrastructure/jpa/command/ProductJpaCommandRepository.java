package com.github.mangila.webshop.product.infrastructure.jpa.command;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsert;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntity;
import com.github.mangila.webshop.product.infrastructure.jpa.ProductEntityMapper;
import io.vavr.collection.Stream;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.github.mangila.webshop.product.infrastructure.event.ProductEvent.PRODUCT_DELETED;

@ObservedRepository
public class ProductJpaCommandRepository implements ProductCommandRepository {

    private final ProductEntityCommandRepository repository;
    private final ProductQueryRepository queryRepository;
    private final ProductEntityMapper entityMapper;
    private final DomainIdGeneratorService domainIdGeneratorService;

    public ProductJpaCommandRepository(ProductEntityCommandRepository repository,
                                       @Qualifier("productJpaQueryRepository") ProductQueryRepository queryRepository,
                                       ProductEntityMapper entityMapper,
                                       DomainIdGeneratorService domainIdGeneratorService) {
        this.repository = repository;
        this.queryRepository = queryRepository;
        this.entityMapper = entityMapper;
        this.domainIdGeneratorService = domainIdGeneratorService;
    }

    @Transactional
    @Override
    public Product insert(ProductInsert command) {
        UUID id = domainIdGeneratorService.generate(GenerateDomainIdCommand.from(Product.class, "Create new Product"));
        ProductEntity entity = entityMapper.toEntity(id, command);
        Product product = Stream.of(entity)
                .map(repository::save)
                .map(entityMapper::toDomain)
                .get();
        eventPublisher.publish(new ProductCreatedEvent(product.getId().value()));
        return product;
    }

    @Transactional
    @Override
    public void deleteByIdOrThrow(ProductId productId) {
        Product product = queryRepository.findByIdOrThrow(productId);
        var entity = entityMapper.toEntity(product);
        // Entity is not managed, so we need to use the persistence flag
        entity.setNew(Boolean.FALSE);
        repository.delete(entity);
        eventPublisher.publish(new ProductDeletedEvent(entity.getId()));
        log.debug("Deleted product with id: {} and event: {} in outbox with id: {}", product.getId(), PRODUCT_DELETED, outboxDto.id());
    }
}
