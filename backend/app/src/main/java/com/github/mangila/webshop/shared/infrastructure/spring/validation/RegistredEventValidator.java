package com.github.mangila.webshop.shared.infrastructure.spring.validation;

import com.github.mangila.webshop.shared.application.registry.Event;
import com.github.mangila.webshop.shared.application.registry.RegistryService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class RegistredEventValidator implements ConstraintValidator<RegistredEvent, Event> {

    private final RegistryService registryService;

    public RegistredEventValidator(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Override
    public boolean isValid(Event value, ConstraintValidatorContext context) {
        return registryService.isRegistered(value);
    }
}
