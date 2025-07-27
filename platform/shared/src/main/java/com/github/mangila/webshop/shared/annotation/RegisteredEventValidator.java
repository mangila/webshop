package com.github.mangila.webshop.shared.annotation;

import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.registry.EventRegistry;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class RegisteredEventValidator implements ConstraintValidator<RegisteredEvent, Event> {

    private final EventRegistry eventRegistry;

    public RegisteredEventValidator(EventRegistry eventRegistry) {
        this.eventRegistry = eventRegistry;
    }

    @Override
    public boolean isValid(Event value, ConstraintValidatorContext context) {
        return eventRegistry.isRegistered(value);
    }
}
