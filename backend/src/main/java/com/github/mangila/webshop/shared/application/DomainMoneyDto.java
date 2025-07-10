package com.github.mangila.webshop.shared.application;

import com.github.mangila.webshop.product.application.validation.ProductCurrency;
import com.github.mangila.webshop.product.application.validation.ProductPrice;
import com.github.mangila.webshop.shared.domain.common.DomainMoney;

import java.math.BigDecimal;

public record DomainMoneyDto(@ProductPrice BigDecimal price,
                             @ProductCurrency String currency) {

    public static DomainMoneyDto from(DomainMoney money) {
        var price = money.getAmount();
        var currency = money.getCurrencyCode();
        return new DomainMoneyDto(price, currency);
    }
}
