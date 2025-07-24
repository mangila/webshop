package com.github.mangila.webshop.shared.validation;

import com.github.mangila.webshop.shared.registry.RegistryService;
import com.github.mangila.webshop.shared.registry.model.Event;
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
