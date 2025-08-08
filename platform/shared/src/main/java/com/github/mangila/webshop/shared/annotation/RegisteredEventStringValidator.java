package com.github.mangila.webshop.shared.annotation;

import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.registry.EventRegistry;
import io.vavr.control.Try;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class RegisteredEventStringValidator implements ConstraintValidator<RegisteredEvent, String> {

    private final EventRegistry eventRegistry;

    public RegisteredEventStringValidator(EventRegistry eventRegistry) {
        this.eventRegistry = eventRegistry;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Try.of(() -> {
            new Event(value, eventRegistry);
            return true;
        }).getOrElseGet(failure -> false);
    }
}
