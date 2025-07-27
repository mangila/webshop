package com.github.mangila.webshop.shared.model;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.math.BigDecimal;

public record DomainMoney(Money value) {
    private DomainMoney(String currency, BigDecimal amount) {
        this(Money.of(CurrencyUnit.of(currency), amount));
    }

    public static DomainMoney from(String currency, BigDecimal amount) {
        return new DomainMoney(currency, amount);
    }

    public BigDecimal getAmount() {
        return value.getAmount();
    }

    public String getCurrencyCode() {
        return value.getCurrencyUnit().getCode();
    }
}

