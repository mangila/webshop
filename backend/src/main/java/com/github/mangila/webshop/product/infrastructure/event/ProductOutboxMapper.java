package com.github.mangila.webshop.product.infrastructure.event;

import com.github.mangila.webshop.product.domain.Product;
import com.github.mangila.webshop.shared.infrastructure.json.JsonMapper;
import com.github.mangila.webshop.shared.outbox.application.cqrs.OutboxInsertCommand;
import org.springframework.stereotype.Component;

@Component
public class ProductOutboxMapper {

    private final JsonMapper jsonMapper;

    public ProductOutboxMapper(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public ProductOutboxDto toDto(Product product) {
        return ProductOutboxDto.from(
                product.getId().value(),
                product.getName().value(),
                product.getPrice().value(),
                product.getAttributes(),
                product.getUnit(),
                product.getCreated()
        );
    }

    public OutboxInsertCommand toCommand(
            ProductTopic topic,
            ProductEvent event,
            ProductOutboxDto dto) {
        return OutboxInsertCommand.from(
                topic,
                event,
                dto.id(),
                dto.toJsonNode(jsonMapper)
        );
    }
}
