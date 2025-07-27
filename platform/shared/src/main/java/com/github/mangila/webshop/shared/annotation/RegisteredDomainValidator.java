package com.github.mangila.webshop.shared.annotation;

import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.registry.RegistryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class RegisteredDomainValidator implements ConstraintValidator<RegisteredDomain, Domain> {

    private final RegistryService registryService;

    public RegisteredDomainValidator(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Override
    public boolean isValid(Domain value, ConstraintValidatorContext context) {
        return registryService.isRegistered(value);
    }
}
