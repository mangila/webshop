package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.product.application.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.application.dto.ProductDto;
import com.github.mangila.webshop.product.application.event.ProductEvent;
import com.github.mangila.webshop.product.application.event.ProductTopic;
import com.github.mangila.webshop.product.application.gateway.ProductMapperGateway;
import com.github.mangila.webshop.product.application.gateway.ProductRepositoryGateway;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductId;
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
        Product product = Stream.of(command)
                .map(ProductInsertCommand::name)
                .map(GenerateNewUuidIntent::new)
                .map(uuidGenerator::generate)
                .map(uuid -> productMapperGateway.command().toDomain(uuid, command))
                .map(productRepositoryGateway.command()::insert)
                .get();
        OutboxDto outboxDto = outboxServiceGateway.command().insert(
                OutboxInsertCommand.from(
                        ProductTopic.PRODUCT,
                        ProductEvent.PRODUCT_CREATE_NEW,
                        product.getId().value(),
                        product.toJsonNode(jsonMapper))
        );
        log.info("{} -- {} -- {}", outboxDto.event(), product, outboxDto);
        return productMapperGateway.dto().toDto(product);
    }

    @Transactional
    public ProductDto delete(UUID id) {
        ProductId productId = new ProductId(id);
        Product product = productRepositoryGateway.query()
                .findById(productId)
                .orElseThrow(() -> new CqrsException(
                        String.format("value not found: '%s'", id),
                        CqrsOperation.QUERY,
                        Product.class
                ));
        productRepositoryGateway.command().deleteById(productId);
        OutboxDto outboxDto = outboxServiceGateway.command().insert(
                OutboxInsertCommand.from(
                        ProductTopic.PRODUCT,
                        ProductEvent.PRODUCT_DELETED,
                        product.getId().value(),
                        product.toJsonNode(jsonMapper))
        );
        log.info("{} -- {} -- {}", outboxDto.event(), product, outboxDto);
        return productMapperGateway.dto().toDto(product);
    }
}
