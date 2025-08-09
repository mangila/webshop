package com.github.mangila.webshop.product.application.action.command;

import com.github.mangila.webshop.product.application.ProductOutboxEventMapper;
import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.product.domain.ProductCommandRepository;
import com.github.mangila.webshop.product.domain.ProductQueryRepository;
import com.github.mangila.webshop.product.domain.cqrs.FindProductByIdQuery;
import com.github.mangila.webshop.product.domain.cqrs.UpdateProductStatusCommand;
import com.github.mangila.webshop.product.domain.event.ProductEvent;
import com.github.mangila.webshop.product.domain.primitive.ProductUpdated;
import com.github.mangila.webshop.shared.CommandAction;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.JavaOptionalUtil;
import com.github.mangila.webshop.shared.SpringEventPublisher;
import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class UpdateProductStatusCommandAction implements CommandAction<UpdateProductStatusCommand, OutboxEvent> {

    private final ProductOutboxEventMapper productOutboxEventMapper;
    private final ProductCommandRepository commandRepository;
    private final ProductQueryRepository queryRepository;
    private final SpringEventPublisher publisher;

    public UpdateProductStatusCommandAction(ProductOutboxEventMapper productOutboxEventMapper,
                                            ProductCommandRepository commandRepository, ProductQueryRepository queryRepository,
                                            SpringEventPublisher publisher) {
        this.productOutboxEventMapper = productOutboxEventMapper;
        this.commandRepository = commandRepository;
        this.queryRepository = queryRepository;
        this.publisher = publisher;
    }

    @Override
    public Event event() {
        return ProductEvent.PRODUCT_STATUS_UPDATED.toEvent();
    }

    @Override
    public OutboxEvent execute(@NotNull UpdateProductStatusCommand command) {
        return queryRepository.findById()
                .andThen(optionalProduct -> JavaOptionalUtil.orElseThrowResourceNotFound(optionalProduct, Product.class, command.id()))
                .andThen(product -> {
                    Ensure.notEquals(command.status(), product.status(), "Product status is already: %s".formatted(command.status()));
                    return product;
                })
                .andThen(product -> toUpdatedProduct(product, command))
                .andThen(commandRepository::updateStatus)
                .andThen(product -> productOutboxEventMapper.toEvent(event(), product))
                .andThen(publisher.publishOutboxEvent())
                .apply(new FindProductByIdQuery(command.id()));
    }

    private static Product toUpdatedProduct(Product currentProduct, UpdateProductStatusCommand command) {
        return new Product(
                currentProduct.id(),
                currentProduct.name(),
                currentProduct.attributes(),
                command.status(),
                currentProduct.variants(),
                currentProduct.created(),
                ProductUpdated.now()
        );
    }

}
