package com.github.mangila.webshop.shared.infrastructure.spring.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RegistredEventValidator.class)
@Documented
public @interface RegistredEvent {
    String message() default "Event is not registred";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
