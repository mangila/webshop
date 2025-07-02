package com.github.mangila.webshop.backend.product.application.service;

import com.github.mangila.webshop.backend.common.exception.CommandException;
import com.github.mangila.webshop.backend.common.util.JsonMapper;
import com.github.mangila.webshop.backend.event.application.gateway.EventServiceGateway;
import com.github.mangila.webshop.backend.event.domain.command.EventPublishCommand;
import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.product.application.gateway.ProductRepositoryGateway;
import com.github.mangila.webshop.backend.product.domain.command.ProductDeleteCommand;
import com.github.mangila.webshop.backend.product.domain.event.ProductEventTopicType;
import com.github.mangila.webshop.backend.product.domain.event.ProductEventType;
import com.github.mangila.webshop.backend.product.domain.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductDeleteCommandService {

    private static final Logger log = LoggerFactory.getLogger(ProductDeleteCommandService.class);

    private final EventServiceGateway eventGateway;
    private final ProductRepositoryGateway repositoryGateway;
    private final JsonMapper jsonMapper;

    public ProductDeleteCommandService(EventServiceGateway eventGateway,
                                       ProductRepositoryGateway repositoryGateway,
                                       JsonMapper jsonMapper) {
        this.eventGateway = eventGateway;
        this.repositoryGateway = repositoryGateway;
        this.jsonMapper = jsonMapper;
    }

    @Transactional
    public Event execute(ProductDeleteCommand command) {
        Product product = repositoryGateway.query().findById(command.id()).orElseThrow(() -> new CommandException(
                command.getClass(),
                Product.class,
                HttpStatus.NOT_FOUND,
                String.format("id not found: '%s'", command.id())));
        repositoryGateway.command().delete(product);
        Event event = eventGateway.publisher().save(
                new EventPublishCommand(
                        ProductEventTopicType.PRODUCT.name(),
                        ProductEventType.PRODUCT_DELETED.name(),
                        product.getId().value(),
                        product.toJsonNode(jsonMapper))
        );
        log.info("{} -- {} -- {}", event.getType(), product, event);
        return event;
    }
}
