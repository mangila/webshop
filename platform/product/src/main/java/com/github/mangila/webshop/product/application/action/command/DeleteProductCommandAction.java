package com.github.mangila.webshop.product.application.action.command;

import com.github.mangila.webshop.product.application.ProductOutboxEventMapper;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.cqrs.DeleteProductCommand;
import com.github.mangila.webshop.product.domain.event.ProductEvent;
import com.github.mangila.webshop.shared.CommandAction;
import com.github.mangila.webshop.shared.JavaOptionalUtil;
import com.github.mangila.webshop.shared.SpringEventPublisher;
import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class DeleteProductCommandAction implements CommandAction<DeleteProductCommand, OutboxEvent> {
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
    public OutboxEvent execute(@NotNull DeleteProductCommand command) {
        return repository.delete()
                .andThen(optionalProduct -> JavaOptionalUtil.orElseThrowResourceNotFound(optionalProduct, Product.class, command.id()))
                .andThen(product -> productOutboxEventMapper.toEvent(event(), product))
                .andThen(publisher.publishOutboxEvent())
                .apply(command);
    }
}
