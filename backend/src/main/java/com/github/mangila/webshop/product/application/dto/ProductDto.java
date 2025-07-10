package com.github.mangila.webshop.product.application.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.product.domain.types.ProductUnit;
import com.github.mangila.webshop.shared.application.dto.DomainMoneyDto;
import com.github.mangila.webshop.shared.domain.common.DomainMoney;

import java.time.Instant;
import java.util.UUID;

public record ProductDto(UUID id,
                         String name,
                         DomainMoneyDto price,
                         JsonNode attributes,
                         ProductUnit unit,
                         Instant created,
                         Instant updated) {

    public static ProductDto from(UUID id, String name, DomainMoney price, JsonNode attributes, ProductUnit unit, Instant created, Instant updated) {
        return new ProductDto(id, name, DomainMoneyDto.from(price), attributes, unit, created, updated);
    }
}
