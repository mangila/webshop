package com.github.mangila.webshop.product.infrastructure.jpa;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.ProductId;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsert;
import com.github.mangila.webshop.product.infrastructure.event.ProductOutboxMapper;
import com.github.mangila.webshop.shared.identity.application.DomainIdGeneratorService;
import com.github.mangila.webshop.shared.identity.application.cqrs.GenerateDomainIdCommand;
import com.github.mangila.webshop.shared.infrastructure.spring.annotation.ObservedRepository;
import com.github.mangila.webshop.shared.outbox.application.dto.OutboxDto;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxServiceGateway;
import io.vavr.collection.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.github.mangila.webshop.product.infrastructure.event.ProductEvent.PRODUCT_CREATE_NEW;
import static com.github.mangila.webshop.product.infrastructure.event.ProductEvent.PRODUCT_DELETED;

@ObservedRepository
public class ProductJpaCommandRepository implements ProductCommandRepository {

    private static final Logger log = LoggerFactory.getLogger(ProductJpaCommandRepository.class);

    private final ProductEntityCommandRepository repository;
    private final ProductQueryRepository queryRepository;
    private final ProductOutboxMapper outboxMapper;
    private final ProductEntityMapper entityMapper;
    private final DomainIdGeneratorService domainIdGeneratorService;
    private final OutboxServiceGateway outboxServiceGateway;

    public ProductJpaCommandRepository(ProductEntityCommandRepository repository,
                                       ProductQueryRepository queryRepository,
                                       ProductOutboxMapper outboxMapper,
                                       ProductEntityMapper entityMapper,
                                       DomainIdGeneratorService domainIdGeneratorService,
                                       OutboxServiceGateway outboxServiceGateway) {
        this.repository = repository;
        this.queryRepository = queryRepository;
        this.outboxMapper = outboxMapper;
        this.entityMapper = entityMapper;
        this.domainIdGeneratorService = domainIdGeneratorService;
        this.outboxServiceGateway = outboxServiceGateway;
    }

    @Transactional
    @Override
    public Product insert(ProductInsert command) {
        UUID id = domainIdGeneratorService.generate(GenerateDomainIdCommand.from(Product.class, "Create new Product"));
        Product product = Stream.of(id)
                .map(_ -> entityMapper.toEntity(id, command))
                .map(repository::save)
                .map(entityMapper::toDomain)
                .get();
        OutboxDto outboxDto = Stream.of(product)
                .map(outboxMapper::toDto)
                .map(dto -> outboxMapper.toCommand(PRODUCT_CREATE_NEW, dto))
                .map(outboxServiceGateway.command()::insert)
                .get();
        log.debug("Inserted product with id: {} and event: {} in outbox with id: {}", product.getId(), PRODUCT_CREATE_NEW, outboxDto.id());
        return product;
    }

    @Transactional
    @Override
    public void deleteById(ProductId productId) {
        Product product = queryRepository.findById(productId);
        repository.deleteById(productId.value());
        OutboxDto outboxDto = Stream.of(product)
                .map(outboxMapper::toDto)
                .map(dto -> outboxMapper.toCommand(PRODUCT_DELETED, dto))
                .map(outboxServiceGateway.command()::insert)
                .get();
        log.debug("Deleted product with id: {} and event: {} in outbox with id: {}", product.getId(), PRODUCT_DELETED, outboxDto.id());
    }
}
