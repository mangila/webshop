package com.github.mangila.webshop.shared.annotation;

import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.registry.RegistryService;
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
