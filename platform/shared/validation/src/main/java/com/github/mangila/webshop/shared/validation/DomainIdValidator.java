package com.github.mangila.webshop.shared.validation;

import com.github.mangila.webshop.identity.application.DomainIdGeneratorService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

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
