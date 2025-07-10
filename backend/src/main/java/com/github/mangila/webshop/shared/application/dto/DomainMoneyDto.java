package com.github.mangila.webshop.shared.application.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.mangila.webshop.shared.domain.common.DomainMoney;
import com.github.mangila.webshop.shared.infrastructure.spring.validation.Amount;
import com.github.mangila.webshop.shared.infrastructure.spring.validation.Currency;

import java.math.BigDecimal;

public record DomainMoneyDto(
        @Currency String currency,
        @Amount
        @JsonSerialize(using = ToStringSerializer.class)
        BigDecimal amount) {

    public static DomainMoneyDto from(DomainMoney money) {
        var currency = money.getCurrencyCode();
        var price = money.getAmount();
        return new DomainMoneyDto(currency, price);
    }
}
