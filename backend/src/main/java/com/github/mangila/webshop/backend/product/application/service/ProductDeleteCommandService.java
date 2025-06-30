package com.github.mangila.webshop.backend.product.application.service;

import com.github.mangila.webshop.backend.common.exception.CommandException;
import com.github.mangila.webshop.backend.common.util.JsonMapper;
import com.github.mangila.webshop.backend.event.EventServiceGateway;
import com.github.mangila.webshop.backend.event.model.Event;
import com.github.mangila.webshop.backend.event.model.EventTopic;
import com.github.mangila.webshop.backend.product.domain.command.ProductDeleteCommand;
import com.github.mangila.webshop.backend.product.domain.ProductDomain;
import com.github.mangila.webshop.backend.product.domain.ProductEventType;
import com.github.mangila.webshop.backend.product.infrastructure.ProductCommandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductDeleteCommandService {

    private static final Logger log = LoggerFactory.getLogger(ProductDeleteCommandService.class);

    private final EventServiceGateway eventServiceGateway;
    private final ProductCommandRepository commandRepository;
    private final JsonMapper jsonMapper;

    public ProductDeleteCommandService(EventServiceGateway eventServiceGateway,
                                       ProductCommandRepository commandRepository,
                                       JsonMapper jsonMapper) {
        this.eventServiceGateway = eventServiceGateway;
        this.commandRepository = commandRepository;
        this.jsonMapper = jsonMapper;
    }

    @Transactional
    public Event execute(ProductDeleteCommand command) {
        var product = commandRepository.delete(command).orElseThrow(() -> new CommandException(
                command.getClass(),
                ProductDomain.class,
                HttpStatus.NOT_FOUND,
                String.format("id not found: '%s'", command.id())));
        Event event = eventServiceGateway.emit(
                EventTopic.PRODUCT,
                product.id(),
                ProductEventType.PRODUCT_DELETED.name(),
                jsonMapper.toJsonNode(product)
        );
        log.info("{} -- {} -- {}", event.type(), product, event);
        return event;
    }
}
