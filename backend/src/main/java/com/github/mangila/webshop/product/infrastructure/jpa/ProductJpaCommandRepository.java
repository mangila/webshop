package com.github.mangila.webshop.product.infrastructure.jpa;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.ProductId;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsert;
import com.github.mangila.webshop.product.infrastructure.event.ProductEvent;
import com.github.mangila.webshop.product.infrastructure.event.ProductOutboxMapper;
import com.github.mangila.webshop.product.infrastructure.event.ProductTopic;
import com.github.mangila.webshop.shared.domain.common.CqrsOperation;
import com.github.mangila.webshop.shared.domain.exception.CqrsException;
import com.github.mangila.webshop.shared.infrastructure.json.JsonMapper;
import com.github.mangila.webshop.shared.outbox.application.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.shared.outbox.application.dto.OutboxDto;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxServiceGateway;
import com.github.mangila.webshop.shared.uuid.application.GenerateNewUuidIntent;
import com.github.mangila.webshop.shared.uuid.application.UuidGeneratorService;
import io.vavr.collection.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public class ProductJpaCommandRepository implements ProductCommandRepository {

    private static final Logger log = LoggerFactory.getLogger(ProductJpaCommandRepository.class);

    private final ProductQueryRepository queryRepository;
    private final UuidGeneratorService uuidGeneratorService;
    private final JsonMapper jsonMapper;
    private final OutboxServiceGateway outboxServiceGateway;
    private final ProductOutboxMapper outboxMapper;
    private final ProductEntityMapper entityMapper;
    private final ProductEntityCommandRepository repository;

    public ProductJpaCommandRepository(ProductQueryRepository queryRepository,
                                       UuidGeneratorService uuidGeneratorService,
                                       JsonMapper jsonMapper,
                                       OutboxServiceGateway outboxServiceGateway,
                                       ProductOutboxMapper outboxMapper,
                                       ProductEntityMapper entityMapper,
                                       ProductEntityCommandRepository repository) {
        this.queryRepository = queryRepository;
        this.uuidGeneratorService = uuidGeneratorService;
        this.jsonMapper = jsonMapper;
        this.outboxServiceGateway = outboxServiceGateway;
        this.outboxMapper = outboxMapper;
        this.entityMapper = entityMapper;
        this.repository = repository;
    }

    @Transactional
    @Override
    public Product insert(ProductInsert command) {
        UUID id = uuidGeneratorService.generate(new GenerateNewUuidIntent("Create new Product"));
        Product product = Stream.of(id)
                .map(uuid -> entityMapper.toEntity(id, command))
                .map(repository::save)
                .map(entityMapper::toDomain)
                .get();
        OutboxDto outboxDto = Stream.of(product)
                .map(outboxMapper::toDto)
                .map(dto -> OutboxInsertCommand.from(
                        ProductTopic.PRODUCT,
                        ProductEvent.PRODUCT_CREATE_NEW,
                        dto.id(),
                        dto.toJsonNode(jsonMapper)
                ))
                .map(outboxServiceGateway.command()::insert)
                .get();
        log.debug("Inserted product with id: {} and event: {} in outbox with id: {}", product.getId(), ProductEvent.PRODUCT_CREATE_NEW, outboxDto.id());
        return product;
    }

    @Transactional
    @Override
    public void deleteById(ProductId productId) {
        Product product = queryRepository.findById(productId)
                .orElseThrow(() -> new CqrsException(String.format("id not found: %s",
                        productId.value()),
                        CqrsOperation.QUERY,
                        Product.class));
        repository.deleteById(productId.value());
        OutboxDto outboxDto = Stream.of(product)
                .map(outboxMapper::toDto)
                .map(dto -> OutboxInsertCommand.from(
                        ProductTopic.PRODUCT,
                        ProductEvent.PRODUCT_DELETED,
                        dto.id(),
                        dto.toJsonNode(jsonMapper)
                ))
                .map(outboxServiceGateway.command()::insert)
                .get();
        log.debug("Deleted product with id: {} and event: {} in outbox with id: {}", product.getId(), ProductEvent.PRODUCT_DELETED, outboxDto.id());
    }
}
