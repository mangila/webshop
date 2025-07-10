package com.github.mangila.webshop.product.application.cqrs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.product.application.validation.ProductName;
import com.github.mangila.webshop.product.domain.types.ProductUnit;
import com.github.mangila.webshop.shared.application.dto.DomainMoneyDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record ProductInsertCommand(
        @ProductName String name,
        @NotNull @Valid DomainMoneyDto price,
        @NotNull ObjectNode attributes,
        @NotNull ProductUnit unit
) {
}
