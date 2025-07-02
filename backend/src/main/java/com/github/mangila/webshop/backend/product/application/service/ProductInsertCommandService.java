package com.github.mangila.webshop.backend.product.application.service;

import com.github.mangila.webshop.backend.common.util.JsonMapper;
import com.github.mangila.webshop.backend.event.application.gateway.EventServiceGateway;
import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.product.application.gateway.ProductRepositoryGateway;
import com.github.mangila.webshop.backend.product.domain.command.ProductInsertCommand;
import com.github.mangila.webshop.backend.product.domain.event.ProductEventType;
import com.github.mangila.webshop.backend.product.domain.event.ProductTopicType;
import com.github.mangila.webshop.backend.product.domain.model.Product;
import com.github.mangila.webshop.backend.uuid.application.UuidGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProductInsertCommandService {

    private static final Logger log = LoggerFactory.getLogger(ProductInsertCommandService.class);

    private final UuidGeneratorService uuidGenerator;
    private final EventServiceGateway eventServiceGateway;
    private final ProductRepositoryGateway repositoryGateway;
    private final JsonMapper jsonMapper;

    public ProductInsertCommandService(UuidGeneratorService uuidGenerator,
                                       EventServiceGateway eventServiceGateway,
                                       ProductRepositoryGateway repositoryGateway,
                                       JsonMapper jsonMapper) {
        this.uuidGenerator = uuidGenerator;
        this.eventServiceGateway = eventServiceGateway;
        this.repositoryGateway = repositoryGateway;
        this.jsonMapper = jsonMapper;
    }

    @Transactional
    public Event execute(ProductInsertCommand command) {
        UUID uuid = uuidGenerator.generate(command.getClass().getSimpleName());
        Product product = repositoryGateway.save(Product.from(uuid, command));
        Event event = eventServiceGateway.publish(
                ProductTopicType.PRODUCT.name(),
                ProductEventType.PRODUCT_INSERTED.name(),
                product.getId().value(),
                product.toJsonNode(jsonMapper)
        );
        log.info("{} -- {} -- {}", event.getType(), product, event);
        return event;
    }
}
