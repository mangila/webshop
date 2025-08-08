package com.github.mangila.webshop.product.application.action.command;

import com.github.mangila.webshop.product.application.ProductOutboxEventMapper;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.cqrs.DeleteProductCommand;
import com.github.mangila.webshop.product.domain.event.ProductEvent;
import com.github.mangila.webshop.shared.CommandAction;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.SpringEventPublisher;
import com.github.mangila.webshop.shared.exception.ResourceNotFoundException;
import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteProductCommandAction implements CommandAction<DeleteProductCommand> {
    private final ProductOutboxEventMapper productOutboxEventMapper;
    private final ProductCommandRepository repository;
    private final SpringEventPublisher publisher;

    public DeleteProductCommandAction(ProductOutboxEventMapper productOutboxEventMapper,
                                      ProductCommandRepository repository,
                                      SpringEventPublisher publisher) {
        this.productOutboxEventMapper = productOutboxEventMapper;
        this.repository = repository;
        this.publisher = publisher;
    }

    @Override
    public Event event() {
        return ProductEvent.PRODUCT_DELETED.toEvent();
    }

    @Transactional
    @Override
    public OutboxEvent execute(DeleteProductCommand command) {
        Ensure.notNull(command, DeleteProductCommand.class);
        return repository.delete()
                .andThen(product -> {
                    return product.orElseThrow(() -> new ResourceNotFoundException(
                            Product.class,
                            command.id()
                    ));
                })
                .andThen(product -> productOutboxEventMapper.toEvent(event(), product))
                .andThen(publisher.publishOutboxEvent())
                .apply(command);
    }
}
