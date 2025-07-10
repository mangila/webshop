package com.github.mangila.webshop.shared.infrastructure.spring.validation;

import com.github.mangila.webshop.shared.application.registry.Domain;
import com.github.mangila.webshop.shared.application.registry.RegistryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class RegistredDomainValidator implements ConstraintValidator<RegistredDomain, Domain> {

    private final RegistryService registryService;

    public RegistredDomainValidator(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Override
    public boolean isValid(Domain value, ConstraintValidatorContext context) {
        return registryService.isRegistered(value);
    }
}
