package com.github.mangila.webshop.product.application.action.command;

import com.github.mangila.webshop.product.application.ProductOutboxEventMapper;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.cqrs.CreateProductCommand;
import com.github.mangila.webshop.product.domain.event.ProductEvent;
import com.github.mangila.webshop.shared.CommandAction;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.SpringEventPublisher;
import com.github.mangila.webshop.shared.identity.application.IdentityService;
import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class CreateProductCommandAction implements CommandAction<CreateProductCommand, OutboxEvent> {

    private final ProductCommandRepository repository;
    private final ProductOutboxEventMapper productOutboxEventMapper;
    private final SpringEventPublisher publisher;
    private final IdentityService identityService;

    public CreateProductCommandAction(ProductCommandRepository repository,
                                      ProductOutboxEventMapper productOutboxEventMapper,
                                      SpringEventPublisher publisher,
                                      IdentityService identityService) {
        this.productOutboxEventMapper = productOutboxEventMapper;
        this.repository = repository;
        this.publisher = publisher;
        this.identityService = identityService;
    }

    @Override
    public Event event() {
        return ProductEvent.PRODUCT_CREATED.toEvent();
    }

    @Transactional
    @Override
    public OutboxEvent execute(@NotNull CreateProductCommand command) {
        Ensure.notNull(command, CreateProductCommand.class);
        identityService.ensureHasGenerated(command.id().value());
        return repository.create()
                .andThen(product -> productOutboxEventMapper.toEvent(event(), product))
                .andThen(publisher.publishOutboxEvent())
                .apply(command);
    }
}
