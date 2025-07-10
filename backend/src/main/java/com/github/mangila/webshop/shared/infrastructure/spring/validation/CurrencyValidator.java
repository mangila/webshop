package com.github.mangila.webshop.shared.infrastructure.spring.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.joda.money.CurrencyUnit;

import java.util.Arrays;
import java.util.List;

public class CurrencyValidator implements ConstraintValidator<Currency, String> {

    private List<CurrencyUnit> validCurrencies;

    @Override
    public void initialize(Currency constraintAnnotation) {
        if (constraintAnnotation.currencies().length > 0) {
            this.validCurrencies = Arrays.stream(constraintAnnotation.currencies())
                    .map(CurrencyUnit::of)
                    .toList();
        } else {
            this.validCurrencies = CurrencyUnit.registeredCurrencies();
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return validCurrencies.stream()
                .anyMatch(currencyUnit -> currencyUnit.getCode().equals(value));
    }
}
