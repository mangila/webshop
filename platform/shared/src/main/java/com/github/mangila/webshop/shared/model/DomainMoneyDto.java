package com.github.mangila.webshop.shared.model;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.mangila.webshop.shared.annotation.Amount;
import com.github.mangila.webshop.shared.annotation.Currency;

import java.math.BigDecimal;

public record DomainMoneyDto(
        @Currency
        String currency,
        @Amount
        @JsonSerialize(using = ToStringSerializer.class)
        BigDecimal amount) {

    public static DomainMoneyDto from(DomainMoney money) {
        var currency = money.getCurrencyCode();
        var price = money.getAmount();
        return new DomainMoneyDto(currency, price);
    }
}
