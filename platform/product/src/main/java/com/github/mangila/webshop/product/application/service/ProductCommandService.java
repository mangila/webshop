package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.identity.application.DomainIdFacade;
import com.github.mangila.webshop.product.application.mapper.ProductEventMapper;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.event.ProductEvent;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.SpringEventPublisher;
import com.github.mangila.webshop.shared.model.DomainEvent;
import com.github.mangila.webshop.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductCommandService {

    private final ProductEventMapper eventMapper;
    private final ProductCommandRepository repository;
    private final SpringEventPublisher publisher;
    private final DomainIdFacade domainIdFacade;

    public ProductCommandService(ProductEventMapper eventMapper,
                                 ProductCommandRepository repository,
                                 SpringEventPublisher publisher,
                                 DomainIdFacade domainIdFacade) {
        this.eventMapper = eventMapper;
        this.repository = repository;
        this.publisher = publisher;
        this.domainIdFacade = domainIdFacade;
    }

    @Transactional
    public Product insert(ProductInsertCommand command) {
        Ensure.notNull(command, "ProductInsertCommand cannot be null");
        domainIdFacade.ensureHasGenerated(command.id().value());
        Product product = repository.insert(command);
        DomainEvent domainEvent = eventMapper.toEvent(ProductEvent.PRODUCT_CREATED, product);
        publisher.publishDomainEvent(domainEvent);
        return product;
    }

    @Transactional
    public void deleteByIdOrThrow(ProductId productId) {
        Ensure.notNull(productId, "ProductId cannot be null");
        Product product = repository.deleteById(productId).orElseThrow(() -> new ResourceNotFoundException(
                "Product not found with id: %s".formatted(productId.value()),
                Product.class
        ));
        DomainEvent domainEvent = eventMapper.toEvent(ProductEvent.PRODUCT_DELETED, product);
        publisher.publishDomainEvent(domainEvent);
    }
}
