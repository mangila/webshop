package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.identity.application.DomainIdGeneratorService;
import com.github.mangila.webshop.product.application.mapper.ProductEventMapper;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.event.ProductEvent;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.shared.event.DomainEvent;
import com.github.mangila.webshop.shared.event.SpringEventPublisher;
import com.github.mangila.webshop.shared.util.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductCommandService {

    private final ProductEventMapper eventMapper;
    private final ProductCommandRepository repository;
    private final DomainIdGeneratorService idGenerator;
    private final SpringEventPublisher publisher;

    public ProductCommandService(ProductEventMapper eventMapper,
                                 ProductCommandRepository repository,
                                 DomainIdGeneratorService idGenerator,
                                 SpringEventPublisher publisher) {
        this.eventMapper = eventMapper;
        this.repository = repository;
        this.idGenerator = idGenerator;
        this.publisher = publisher;
    }

    @Transactional
    public Product insert(ProductInsertCommand command) {
        idGenerator.ensureHasGenerated(command.id().value());
        Product product = repository.insert(command);
        DomainEvent domainEvent = eventMapper.toEvent(ProductEvent.PRODUCT_CREATED, product);
        publisher.publishDomainEvent(domainEvent);
        return product;
    }

    @Transactional
    public void deleteByIdOrThrow(ProductId productId) {
        idGenerator.ensureHasGenerated(productId.value());
        Product product = repository.deleteById(productId).orElseThrow(() -> new ResourceNotFoundException(
                "Product not found with id: %s".formatted(productId.value()),
                Product.class
        ));
        DomainEvent domainEvent = eventMapper.toEvent(ProductEvent.PRODUCT_DELETED, product);
        publisher.publishDomainEvent(domainEvent);
    }
}
