package com.github.mangila.webshop.product.application.mapper;

import com.github.mangila.webshop.product.application.cqrs.ProductIdCommand;
import com.github.mangila.webshop.product.application.cqrs.ProductInsertCommand;
import com.github.mangila.webshop.product.domain.cqrs.ProductInsert;
import com.github.mangila.webshop.product.domain.primitive.ProductId;
import com.github.mangila.webshop.shared.domain.common.DomainMoney;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductCommandMapper {

    public ProductInsert toDomain(ProductInsertCommand command) {
        String currency = command.price().currency();
        BigDecimal price = command.price().amount();
        return ProductInsert.from(
                command.name(),
                DomainMoney.from(currency, price),
                command.attributes(),
                command.unit()
        );
    }

    public ProductId toDomain(ProductIdCommand id) {
        return new ProductId(id.value());
    }
}
