package com.github.mangila.webshop.shared.registry.validation;

import com.github.mangila.webshop.shared.registry.RegistryService;
import com.github.mangila.webshop.shared.registry.model.Event;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class RegisteredEventValidator implements ConstraintValidator<RegisteredEvent, Event> {

    private final RegistryService registryService;

    public RegisteredEventValidator(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Override
    public boolean isValid(Event value, ConstraintValidatorContext context) {
        return registryService.isRegistered(value);
    }
}
