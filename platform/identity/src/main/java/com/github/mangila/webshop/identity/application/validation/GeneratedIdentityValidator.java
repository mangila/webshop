package com.github.mangila.webshop.identity.application.validation;

import com.github.mangila.webshop.identity.application.IdentityService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GeneratedIdentityValidator implements ConstraintValidator<GeneratedIdentity, UUID> {

    private final IdentityService identityService;

    public GeneratedIdentityValidator(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Override
    public boolean isValid(UUID value, ConstraintValidatorContext context) {
        return identityService.hasGenerated(value);
    }
}
