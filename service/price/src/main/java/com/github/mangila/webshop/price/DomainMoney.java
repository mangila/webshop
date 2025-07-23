package com.github.mangila.webshop.price;

import org.joda.money.CurrencyUnit;
import org.joda.money.IllegalCurrencyException;
import org.joda.money.Money;

import java.math.BigDecimal;

public final class DomainMoney {

    private final Money value;

    private DomainMoney(String currency, BigDecimal amount) throws ArithmeticException, IllegalCurrencyException {
        this.value = Money.of(CurrencyUnit.of(currency), amount);
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
