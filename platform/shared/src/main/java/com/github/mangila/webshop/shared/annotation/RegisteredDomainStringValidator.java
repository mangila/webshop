package com.github.mangila.webshop.shared.annotation;

import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.registry.DomainRegistry;
import io.vavr.control.Try;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class RegisteredDomainStringValidator implements ConstraintValidator<RegisteredDomain, String> {

    private final DomainRegistry domainRegistry;

    public RegisteredDomainStringValidator(DomainRegistry domainRegistry) {
        this.domainRegistry = domainRegistry;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Try.of(() -> {
            new Domain(value, domainRegistry);
            return true;
        }).getOrElseGet(failure -> false);
    }
}
