package com.github.mangila.webshop.shared.registry.validation;

import com.github.mangila.webshop.shared.registry.RegistryService;
import com.github.mangila.webshop.shared.registry.model.Domain;
import io.vavr.control.Try;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class RegisteredDomainStringValidator implements ConstraintValidator<RegisteredDomain, String> {

    private final RegistryService registryService;

    public RegisteredDomainStringValidator(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Try.of(() -> {
            new Domain(value, registryService);
            return true;
        }).getOrElseGet(failure -> false);
    }
}
