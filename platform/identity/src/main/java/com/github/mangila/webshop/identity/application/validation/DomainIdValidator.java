package com.github.mangila.webshop.identity.application.validation;

import com.github.mangila.webshop.identity.application.DomainIdService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DomainIdValidator implements ConstraintValidator<DomainId, UUID> {

    private final DomainIdService domainIdService;

    public DomainIdValidator(DomainIdService domainIdService) {
        this.domainIdService = domainIdService;
    }

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return domainIdService.hasGenerated(value);
    }
}
