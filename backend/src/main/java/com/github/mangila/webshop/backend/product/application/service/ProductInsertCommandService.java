package com.github.mangila.webshop.backend.product.application.service;

import com.github.mangila.webshop.backend.common.util.JsonMapper;
import com.github.mangila.webshop.backend.event.application.EventServiceGateway;
import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.event.domain.model.EventTopic;
import com.github.mangila.webshop.backend.product.domain.ProductEventType;
import com.github.mangila.webshop.backend.product.domain.command.ProductInsertCommand;
import com.github.mangila.webshop.backend.product.domain.model.Product;
import com.github.mangila.webshop.backend.product.infrastructure.ProductCommandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductInsertCommandService {

    private static final Logger log = LoggerFactory.getLogger(ProductInsertCommandService.class);

    private final EventServiceGateway eventServiceGateway;
    private final ProductCommandRepository commandRepository;
    private final JsonMapper jsonMapper;

    public ProductInsertCommandService(EventServiceGateway eventServiceGateway,
                                       ProductCommandRepository commandRepository,
                                       JsonMapper jsonMapper) {
        this.eventServiceGateway = eventServiceGateway;
        this.commandRepository = commandRepository;
        this.jsonMapper = jsonMapper;
    }

    @Transactional
    public Event execute(ProductInsertCommand command) {
        Product product = commandRepository.save(command.toProduct());
        Event event = eventServiceGateway.emit(
                EventTopic.PRODUCT,
                ProductEventType.PRODUCT_UPSERTED.name(),
                product.id(),
                product.toJsonData(jsonMapper)
        );
        log.info("{} -- {} -- {}", event.getEventType(), product, event);
        return event;
    }
}
