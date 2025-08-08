package com.github.mangila.webshop.product.application.action.command;

import com.github.mangila.webshop.product.application.ProductOutboxEventMapper;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.cqrs.UpdateProductStatusCommand;
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
public class UpdateProductStatusCommandAction implements CommandAction<UpdateProductStatusCommand> {

    private final ProductOutboxEventMapper productOutboxEventMapper;
    private final ProductCommandRepository repository;
    private final SpringEventPublisher publisher;

    public UpdateProductStatusCommandAction(ProductOutboxEventMapper productOutboxEventMapper,
                                            ProductCommandRepository repository,
                                            SpringEventPublisher publisher) {
        this.productOutboxEventMapper = productOutboxEventMapper;
        this.repository = repository;
        this.publisher = publisher;
    }

    @Override
    public Event event() {
        return ProductEvent.PRODUCT_STATUS_UPDATED.toEvent();
    }

    @Transactional
    @Override
    public OutboxEvent execute(UpdateProductStatusCommand command) {
        Ensure.notNull(command, UpdateProductStatusCommand.class);
        return repository.updateStatus()
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
