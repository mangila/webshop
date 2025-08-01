package com.github.mangila.webshop.shared.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {
        RegisteredEventValidator.class,
        RegisteredEventStringValidator.class
})
@Documented
public @interface RegisteredEvent {
    String message() default "Event is not registered";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
