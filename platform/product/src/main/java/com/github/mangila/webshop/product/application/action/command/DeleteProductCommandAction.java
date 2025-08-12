package com.github.mangila.webshop.product.application.action.command;

import com.github.mangila.webshop.product.application.ProductOutboxEventMapper;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import com.github.mangila.webshop.product.domain.cqrs.DeleteProductCommand;
import com.github.mangila.webshop.product.domain.cqrs.FindProductByIdQuery;
import com.github.mangila.webshop.product.domain.event.ProductEvent;
import com.github.mangila.webshop.shared.CommandAction;
import com.github.mangila.webshop.shared.JavaOptionalUtil;
import com.github.mangila.webshop.shared.SpringEventPublisher;
import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class DeleteProductCommandAction implements CommandAction<DeleteProductCommand, OutboxEvent> {
    private final ProductOutboxEventMapper productOutboxEventMapper;
    private final ProductCommandRepository commandRepository;
    private final ProductQueryRepository queryRepository;
    private final SpringEventPublisher publisher;

    public DeleteProductCommandAction(ProductOutboxEventMapper productOutboxEventMapper,
                                      ProductCommandRepository commandRepository, ProductQueryRepository queryRepository,
                                      SpringEventPublisher publisher) {
        this.productOutboxEventMapper = productOutboxEventMapper;
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
        this.publisher = publisher;
    }

    @Override
    public Event event() {
        return ProductEvent.PRODUCT_DELETED.toEvent();
    }

    @Override
    public OutboxEvent execute(@NotNull DeleteProductCommand command) {
        return queryRepository.findById()
                .andThen(optionalProduct -> JavaOptionalUtil.orElseThrowResourceNotFound(optionalProduct, Product.class, command.id()))
                .andThen(commandRepository::delete)
                .andThen(product -> productOutboxEventMapper.toEvent(event(), product))
                .andThen(publisher.publishOutboxEvent())
                .apply(new FindProductByIdQuery(command.id()));
    }
}
