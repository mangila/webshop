package com.github.mangila.webshop.shared.application.dto;

import com.github.mangila.webshop.shared.infrastructure.spring.validation.Amount;
import com.github.mangila.webshop.shared.domain.common.DomainMoney;
import com.github.mangila.webshop.shared.infrastructure.spring.validation.Currency;

import java.math.BigDecimal;

public record DomainMoneyDto(@Amount BigDecimal price,
                             @Currency String currency) {

    public static DomainMoneyDto from(DomainMoney money) {
        var price = money.getAmount();
        var currency = money.getCurrencyCode();
        return new DomainMoneyDto(price, currency);
    }
}
