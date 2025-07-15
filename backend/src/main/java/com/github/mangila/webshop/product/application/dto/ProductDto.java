package com.github.mangila.webshop.product.application.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.product.application.validation.ProductName;
import com.github.mangila.webshop.product.domain.types.ProductUnit;
import com.github.mangila.webshop.shared.application.dto.DomainMoneyDto;
import com.github.mangila.webshop.shared.domain.common.DomainMoney;
import com.github.mangila.webshop.shared.infrastructure.spring.validation.DomainId;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ProductDto(@NotNull @DomainId(message = "Not a valid Product ID") UUID id,
                         @NotNull @ProductName String name,
                         @NotNull @Valid DomainMoneyDto price,
                         @NotNull ObjectNode attributes,
                         @NotNull ProductUnit unit,
                         @NotNull Instant created,
                         @NotNull Instant updated) {

    public static ProductDto from(UUID id, String name, DomainMoney price, ObjectNode attributes, ProductUnit unit, Instant created, Instant updated) {
        return new ProductDto(id, name, DomainMoneyDto.from(price), attributes, unit, created, updated);
    }
}
