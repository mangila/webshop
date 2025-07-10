package com.github.mangila.webshop.product.infrastructure.event;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.product.domain.types.ProductUnit;
import com.github.mangila.webshop.shared.application.dto.DomainMoneyDto;
import com.github.mangila.webshop.shared.domain.common.DomainMoney;
import com.github.mangila.webshop.shared.infrastructure.json.JsonMapper;

import java.time.Instant;
import java.util.UUID;

public record ProductOutboxDto(UUID id,
                               String name,
                               DomainMoneyDto price,
                               ObjectNode attributes,
                               ProductUnit unit,
                               Instant created) {

    public static ProductOutboxDto from(UUID id, String name, DomainMoney price, ObjectNode attributes, ProductUnit unit, Instant created) {
        return new ProductOutboxDto(id, name, DomainMoneyDto.from(price), attributes, unit, created);
    }

    public ObjectNode toObjectNode(JsonMapper jsonMapper) {
        return jsonMapper.toObjectNode(this);
    }
}
