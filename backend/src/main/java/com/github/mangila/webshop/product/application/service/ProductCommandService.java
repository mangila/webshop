package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.product.application.cqrs.ProductIdCommand;
import com.github.mangila.webshop.product.application.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.application.dto.ProductDto;
import com.github.mangila.webshop.product.application.event.ProductEvent;
import com.github.mangila.webshop.product.application.event.ProductTopic;
import com.github.mangila.webshop.product.application.gateway.ProductMapperGateway;
import com.github.mangila.webshop.product.application.gateway.ProductRepositoryGateway;
import com.github.mangila.webshop.product.domain.Product;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProductCommandService {

    private static final Logger log = LoggerFactory.getLogger(ProductCommandService.class);

    private final UuidGeneratorService uuidGenerator;
    private final OutboxServiceGateway outboxServiceGateway;
    private final ProductRepositoryGateway productRepositoryGateway;
    private final ProductMapperGateway productMapperGateway;
    private final JsonMapper jsonMapper;

    public ProductCommandService(UuidGeneratorService uuidGenerator,
                                 OutboxServiceGateway outboxServiceGateway,
                                 ProductRepositoryGateway productRepositoryGateway,
                                 ProductMapperGateway productMapperGateway,
                                 JsonMapper jsonMapper) {
        this.uuidGenerator = uuidGenerator;
        this.outboxServiceGateway = outboxServiceGateway;
        this.productRepositoryGateway = productRepositoryGateway;
        this.productMapperGateway = productMapperGateway;
        this.jsonMapper = jsonMapper;
    }

    @Transactional
    public ProductDto insert(ProductInsertCommand command) {
        UUID id = uuidGenerator.generate(
                new GenerateNewUuidIntent("Create new Product")
        );
        ProductDto dto = Stream.of(id)
                .peek(uuid -> log.debug("Insert product: {}", uuid))
                .map(uuid -> productMapperGateway.command().toDomain(uuid, command))
                .map(productRepositoryGateway.command()::insert)
                .map(productMapperGateway.dto()::toDto)
                .get();
        OutboxInsertCommand outboxInsertCommand = OutboxInsertCommand.from(
                ProductTopic.PRODUCT,
                ProductEvent.PRODUCT_CREATE_NEW,
                dto.id(),
                dto.toJsonNode(jsonMapper)
        );
        OutboxDto outboxDto = outboxServiceGateway.command()
                .insert(outboxInsertCommand);
        log.debug("Created product with id: {} and event: {} in outbox with id: {}", dto.id(), ProductEvent.PRODUCT_CREATE_NEW, outboxDto.id());
        return dto;
    }

    @Transactional
    public ProductDto delete(ProductIdCommand command) {
        ProductDto dto = Stream.of(command)
                .peek(c -> log.debug("Delete product: {}", c))
                .map(productMapperGateway.command()::toDomain)
                .map(productRepositoryGateway.query()::findById)
                .map(product -> {
                    if (product.isEmpty()) {
                        throw new CqrsException(
                                String.format("id not found: '%s'", command),
                                CqrsOperation.QUERY,
                                Product.class
                        );
                    }
                    return product.get();
                })
                .map(product -> {
                    productRepositoryGateway.command().deleteById(product.getId());
                    return product;
                })
                .map(productMapperGateway.dto()::toDto)
                .get();
        OutboxInsertCommand outboxInsertCommand = OutboxInsertCommand.from(
                ProductTopic.PRODUCT,
                ProductEvent.PRODUCT_DELETED,
                dto.id(),
                dto.toJsonNode(jsonMapper)
        );
        OutboxDto outboxDto = outboxServiceGateway.command()
                .insert(outboxInsertCommand);
        log.debug("Deleted product with id: {} and event: {} in outbox with id: {}", dto.id(), ProductEvent.PRODUCT_DELETED, outboxDto.id());
        return dto;
    }
}
