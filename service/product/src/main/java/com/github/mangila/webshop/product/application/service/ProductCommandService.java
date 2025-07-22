package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.product.application.ProductMapper;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.event.ProductEvent;
import com.github.mangila.webshop.shared.event.DomainEvent;
import com.github.mangila.webshop.shared.event.SpringEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductCommandService {

    private final ProductMapper eventMapper;
    private final ProductCommandRepository repository;
    private final SpringEventPublisher publisher;

    public ProductCommandService(ProductMapper eventMapper,
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

}
