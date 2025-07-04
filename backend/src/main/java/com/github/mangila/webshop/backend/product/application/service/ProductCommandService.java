package com.github.mangila.webshop.backend.product.application.service;

import com.github.mangila.webshop.backend.common.JsonMapper;
import com.github.mangila.webshop.backend.common.error.exception.CommandException;
import com.github.mangila.webshop.backend.outboxevent.application.gateway.OutboxEventServiceGateway;
import com.github.mangila.webshop.backend.outboxevent.domain.OutboxEvent;
import com.github.mangila.webshop.backend.outboxevent.domain.command.OutboxEventInsertCommand;
import com.github.mangila.webshop.backend.product.application.gateway.ProductRepositoryGateway;
import com.github.mangila.webshop.backend.product.domain.command.ProductDeleteCommand;
import com.github.mangila.webshop.backend.product.domain.command.ProductInsertCommand;
import com.github.mangila.webshop.backend.product.domain.event.ProductTopic;
import com.github.mangila.webshop.backend.product.domain.event.ProductEvent;
import com.github.mangila.webshop.backend.product.domain.model.Product;
import com.github.mangila.webshop.backend.uuid.application.UuidGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProductCommandService {

    private static final Logger log = LoggerFactory.getLogger(ProductCommandService.class);
    private final UuidGeneratorService uuidGenerator;
    private final OutboxEventServiceGateway eventServiceGateway;
    private final ProductRepositoryGateway productRepositoryGateway;
    private final JsonMapper jsonMapper;

    public ProductCommandService(UuidGeneratorService uuidGenerator,
                                 OutboxEventServiceGateway eventServiceGateway,
                                 ProductRepositoryGateway productRepositoryGateway,
                                 JsonMapper jsonMapper) {
        this.uuidGenerator = uuidGenerator;
        this.eventServiceGateway = eventServiceGateway;
        this.productRepositoryGateway = productRepositoryGateway;
        this.jsonMapper = jsonMapper;
    }

    @Transactional
    public OutboxEvent insert(ProductInsertCommand command) {
        UUID uuid = uuidGenerator.generate(command.getClass().getSimpleName());
        Product product = productRepositoryGateway.command().save(Product.from(uuid, command));
        OutboxEvent outboxEvent = eventServiceGateway.command().insert(
                OutboxEventInsertCommand.from(
                        ProductTopic.PRODUCT,
                        ProductEvent.PRODUCT_CREATE_NEW,
                        product.getId().value(),
                        product.toJsonNode(jsonMapper))
        );
        log.info("{} -- {} -- {}", outboxEvent.getType(), product, outboxEvent);
        return outboxEvent;
    }

    @Transactional
    public OutboxEvent delete(ProductDeleteCommand command) {
        Product product = productRepositoryGateway.query().findById(command.id()).orElseThrow(() -> new CommandException(
                command.getClass(),
                Product.class,
                HttpStatus.NOT_FOUND,
                String.format("id not found: '%s'", command.id())));
        productRepositoryGateway.command().delete(product);
        OutboxEvent outboxEvent = eventServiceGateway.command().insert(
                OutboxEventInsertCommand.from(
                        ProductTopic.PRODUCT,
                        ProductEvent.PRODUCT_DELETED,
                        product.getId().value(),
                        product.toJsonNode(jsonMapper))
        );
        log.info("{} -- {} -- {}", outboxEvent.getType(), product, outboxEvent);
        return outboxEvent;
    }
}
