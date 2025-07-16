package com.github.mangila.webshop.shared.infrastructure.spring.validation;

import com.github.mangila.webshop.shared.identity.application.DomainIdGeneratorService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@Component
public class DomainIdValidator implements ConstraintValidator<DomainId, UUID> {

    private final DomainIdGeneratorService domainIdGeneratorService;

    public DomainIdValidator(DomainIdGeneratorService domainIdGeneratorService) {
        this.domainIdGeneratorService = domainIdGeneratorService;
    }

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        return domainIdGeneratorService.hasGenerated(value);
    }
}
