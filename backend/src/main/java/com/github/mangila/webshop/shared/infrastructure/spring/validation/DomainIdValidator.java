package com.github.mangila.webshop.shared.infrastructure.spring.validation;

import com.github.mangila.webshop.shared.uuid.application.UuidGeneratorService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DomainIdValidator implements ConstraintValidator<DomainId, UUID> {

    private final UuidGeneratorService uuidGeneratorService;

    public DomainIdValidator(UuidGeneratorService uuidGeneratorService) {
        this.uuidGeneratorService = uuidGeneratorService;
    }

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        return uuidGeneratorService.hasGenerated(value);
    }
}
