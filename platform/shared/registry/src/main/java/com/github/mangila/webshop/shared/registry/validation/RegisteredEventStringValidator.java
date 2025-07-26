package com.github.mangila.webshop.shared.registry.validation;

import com.github.mangila.webshop.shared.registry.RegistryService;
import com.github.mangila.webshop.shared.registry.model.Event;
import io.vavr.control.Try;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class RegisteredEventStringValidator implements ConstraintValidator<RegisteredDomain, String> {

    private final RegistryService registryService;

    public RegisteredEventStringValidator(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Try.of(() -> {
            new Event(value, registryService);
            return true;
        }).getOrElseGet(failure -> false);
    }
}
