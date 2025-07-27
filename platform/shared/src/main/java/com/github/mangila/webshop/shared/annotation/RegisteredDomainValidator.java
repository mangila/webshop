package com.github.mangila.webshop.shared.annotation;

import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.registry.DomainRegistry;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class RegisteredDomainValidator implements ConstraintValidator<RegisteredDomain, Domain> {

    private final DomainRegistry domainRegistry;

    public RegisteredDomainValidator(DomainRegistry domainRegistry) {
        this.domainRegistry = domainRegistry;
    }

    @Override
    public boolean isValid(Domain value, ConstraintValidatorContext context) {
        return domainRegistry.isRegistered(value);
    }
}
