package com.github.mangila.webshop.shared.infrastructure.spring.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrencyValidator.class)
@Documented
public @interface Currency {
    String message() default "Unknown Currency";

    String[] currencies() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
