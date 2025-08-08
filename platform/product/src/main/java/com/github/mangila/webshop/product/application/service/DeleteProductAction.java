package com.github.mangila.webshop.product.application.service;

import com.github.mangila.webshop.product.application.ProductOutboxMapper;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.cqrs.DeleteProductCommand;
import com.github.mangila.webshop.product.domain.event.ProductEvent;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.SpringEventPublisher;
import com.github.mangila.webshop.shared.exception.ResourceNotFoundException;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteProductAction {
    private final ProductOutboxMapper outboxMapper;
    private final ProductCommandRepository repository;
    private final SpringEventPublisher publisher;

    public DeleteProductAction(ProductOutboxMapper outboxMapper,
                               ProductCommandRepository repository,
                               SpringEventPublisher publisher) {
        this.outboxMapper = outboxMapper;
        this.repository = repository;
        this.publisher = publisher;
    }

    @Transactional
    public OutboxEvent execute(DeleteProductCommand command) {
        Ensure.notNull(command, DeleteProductCommand.class);
        Product product = repository.delete(command).orElseThrow(() -> new ResourceNotFoundException(
                Product.class,
                command.id()
        ));
        OutboxEvent outboxEvent = outboxMapper.toOutboxEvent(
                ProductEvent.PRODUCT_DELETED,
                product);
        publisher.publishOutboxEvent(outboxEvent);
        return outboxEvent;
    }
}
