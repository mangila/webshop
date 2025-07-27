package com.github.mangila.webshop.shared.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.ArrayUtils;
import org.joda.money.CurrencyUnit;

import java.util.Arrays;
import java.util.List;

public class CurrencyValidator implements ConstraintValidator<Currency, String> {

    private List<CurrencyUnit> validCurrencies;

    @Override
    public void initialize(Currency constraintAnnotation) {
        if (!ArrayUtils.isEmpty(constraintAnnotation.currencies())) {
            this.validCurrencies = Arrays.stream(constraintAnnotation.currencies())
                    .map(CurrencyUnit::of)
                    .toList();
        } else {
            this.validCurrencies = CurrencyUnit.registeredCurrencies();
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validCurrencies.stream()
                .anyMatch(currencyUnit -> currencyUnit.getCode().equals(value));
    }
}
