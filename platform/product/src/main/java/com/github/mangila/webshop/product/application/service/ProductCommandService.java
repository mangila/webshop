package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.product.application.mapper.ProductEventMapper;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.event.ProductEvent;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.shared.annotation.ObservedService;
import com.github.mangila.webshop.shared.event.DomainEvent;
import com.github.mangila.webshop.shared.event.SpringEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@ObservedService
public class ProductCommandService {

    private final ProductEventMapper eventMapper;
    private final ProductCommandRepository repository;
    private final SpringEventPublisher publisher;

    public ProductCommandService(ProductEventMapper eventMapper,
                                 ProductCommandRepository repository,
                                 SpringEventPublisher publisher) {
        this.eventMapper = eventMapper;
        this.repository = repository;
        this.publisher = publisher;
    }

    @Transactional
    public Product insert(ProductInsertCommand command) {
        Product product = repository.insert(command);
        DomainEvent domainEvent = eventMapper.toEvent(ProductEvent.PRODUCT_CREATED, product);
        publisher.publishEvent(domainEvent);
        return product;
    }

    @Transactional
    public boolean deleteById(ProductId productId) {
        Optional<Product> optional = repository.deleteById(productId);
        if (optional.isPresent()) {
            Product product = optional.get();
            DomainEvent domainEvent = eventMapper.toEvent(ProductEvent.PRODUCT_DELETED, product);
            publisher.publishEvent(domainEvent);
            return true;
        }
        return false;
    }
}
